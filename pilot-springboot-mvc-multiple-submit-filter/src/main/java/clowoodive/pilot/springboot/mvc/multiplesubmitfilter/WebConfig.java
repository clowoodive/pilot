package clowoodive.pilot.springboot.mvc.multiplesubmitfilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Autowired
    private Interceptor interceptor;

    public WebConfig(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**");
        // registry.addInterceptor(interceptor);
    }

    // public void addViewControllers(ViewControllerRegistry registry) {
    //     registry.addViewController("/").setViewName("home");
    //     registry.addViewController("/home").setViewName("home");
        
    //     registry.addViewController("/hello").setViewName("hello");
    //     registry.addViewController("/greeting").setViewName("greeting");

    //     registry.addViewController("/login").setViewName("login");
    // }
    
    // @Bean
    // public MessageSource messageSource() {
    //     ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    //     messageSource.setBasename("classpath:/i18N/messages");
    //     messageSource.setDefaultEncoding("UTF-8");
    //     messageSource.setCacheSeconds(0);
       
    //     return messageSource;
    // }
}
