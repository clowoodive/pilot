package clowoodive.pilot.springboot.mvc.multiplesubmitfilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest
@WithMockUser
class InterceptorTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private FmsFilter fmsFilter;


	@BeforeEach
	public void setup() {
		fmsFilter.disable();
	}

	@RepeatedTest(10)
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

		// when
		ResultActions firstResult = mockMvc.perform(post("/filter/message")
				.session(session)
				.with(csrf())
				.param("message", "first submit")
		);

		Thread.sleep(180);

		ResultActions secondResult = mockMvc.perform(post("/filter/message")
				.session(session)
				.with(csrf())
				.param("message", "second submit")
		);

		Thread.sleep(140);

		ResultActions thirdResult = mockMvc.perform(post("/filter/message")
				.session(session)
				.with(csrf())
				.param("message", "third submit")
		);

		// then
		firstResult.andDo(print())
				.andExpect(status().isOk());
		secondResult.andDo(print())
				.andExpect(status().isTooManyRequests());
		thirdResult.andDo(print())
				.andExpect(status().isOk());
	}
}
