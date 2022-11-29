package clowoodive.pilot.springboot.mvc.multiplesubmitfilter;

import org.assertj.core.api.Assert;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@WebMvcTest
@WithMockUser
class FmsFilterTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	FmsFilter fmsFilter;

	@BeforeEach
	public void setup() {
		fmsFilter.enable();
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
//				.apply(sharedHttpSession())
//				.build();
	}

	@Test
	public void settingCheck() {
		ServletContext servletContext = webApplicationContext.getServletContext();
		assertNotNull(servletContext);
		assertTrue(servletContext instanceof MockServletContext);
		assertNotNull(webApplicationContext.getBean("messageController"));
	}

	@Test
	public void getForm() throws Exception {
		mockMvc.perform(get("/filter/message"))
				.andDo(print())
				.andExpect(view().name("message-form"));
	}

	@Test
	public void postFormWithInvalidCsrf() throws Exception {
		// given
		fmsFilter.disable();

		// when
		ResultActions result = mockMvc.perform(post("/filter/message")
						.with(csrf().useInvalidToken())
						.param("message", "with Invalid CSRF")
		);

		// then
		result.andDo(print())
				.andExpect(status().isForbidden());
	}

	@Test
	public void postFormWithValidCsrf() throws Exception {
		// given
		fmsFilter.disable();

		// when
		ResultActions result = mockMvc.perform(post("/filter/message")
				.with(csrf())
				.param("message", "with valid CSRF")
		);

		// then
		result.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void postMFormWithValidCsrfAndFms() throws Exception {
		// given
		UUID genUuid = UUID.randomUUID();

		try (MockedStatic<UUID> uuidMockedStatic = mockStatic(UUID.class)) {
			when(UUID.randomUUID()).thenReturn(genUuid);

			// when
			ResultActions result = mockMvc.perform(post("/filter/message")
					.with(csrf())
					.param(FmsFilter.FMS_PARAMETER_NAME, genUuid.toString())
					.param("message", "with CSRF and FMS")
			);

			// then
			result.andDo(print())
					.andExpect(status().isOk());
		}
	}

	@Test
	public void postFormWithValidCsrfAndInvalidFms() throws Exception {
		// given

		// when
		ResultActions result = mockMvc.perform(post("/filter/message")
				.with(csrf())
				.param(FmsFilter.FMS_PARAMETER_NAME, "INVALID-FMS-TOKEN")
				.param("message", "with CSRF and FMS")
		);

		// then
		result.andDo(print())
				.andExpect(status().isNotAcceptable());
	}

	@Test
	public void postFormDoubleSubmit() throws Exception {
		// given
		User user = new User("user", "1234", Collections.singletonList(new SimpleGrantedAuthority("USER")));
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(user.getUsername(), user);

		ResultActions getResult = mockMvc.perform(get("/filter/message")
				.session(session));
		getResult
				.andDo(print())
				.andExpect(view().name("message-form"));

		String fmsToken = getResult.andReturn().getRequest().getAttribute(FmsFilter.FMS_PARAMETER_NAME).toString();

		// when
		ResultActions firstResult = mockMvc.perform(post("/filter/message")
				.session(session)
				.with(csrf())
				.param(FmsFilter.FMS_PARAMETER_NAME, fmsToken)
				.param("message", "first submit")
		);

		Thread.sleep(10);
		ResultActions secondResult = mockMvc.perform(post("/filter/message")
				.session(session)
				.with(csrf())
				.param(FmsFilter.FMS_PARAMETER_NAME, fmsToken)
				.param("message", "second submit")
		);

		// then
		firstResult.andDo(print())
				.andExpect(status().isOk());
		secondResult.andDo(print())
				.andExpect(status().isNotAcceptable());
	}

	@RepeatedTest(30)
	public void postFormMultipleSubmit(RepetitionInfo info) throws Exception {
		// given
		User user = new User("user", "1234", Collections.singletonList(new SimpleGrantedAuthority("USER")));
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(user.getUsername(), user);

		ResultActions getResult = mockMvc.perform(get("/filter/message")
				.session(session));
		getResult
				.andDo(print())
				.andExpect(view().name("message-form"));

		String fmsToken = getResult.andReturn().getRequest().getAttribute(FmsFilter.FMS_PARAMETER_NAME).toString();

		List<Integer> statusList = new CopyOnWriteArrayList<>();
		int numOfThread = 10;
		ExecutorService executorService = Executors.newFixedThreadPool(numOfThread);
		CountDownLatch latch = new CountDownLatch(numOfThread);
		for( int i = 0; i < numOfThread ; i++) {
			int finalI = i;
			executorService.execute(() -> {
				try {
					Thread.sleep(10*finalI);
					ResultActions result = mockMvc.perform(post("/filter/message")
							.session(session)
							.with(csrf())
							.param(FmsFilter.FMS_PARAMETER_NAME, fmsToken)
							.param("message", finalI + " thread submit"));
					statusList.add(result.andReturn().getResponse().getStatus());
					latch.countDown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		latch.await();
		System.out.println(Arrays.toString(statusList.toArray()));

		assertThat(statusList).hasSize(numOfThread);
		assertThat(statusList).haveAtMost(1, new Condition<Integer>(status-> status == 200, "isOK"));
	}
}
