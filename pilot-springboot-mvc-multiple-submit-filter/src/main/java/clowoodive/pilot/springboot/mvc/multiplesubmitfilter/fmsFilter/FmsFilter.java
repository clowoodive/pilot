package clowoodive.pilot.springboot.mvc.multiplesubmitfilter.fmsFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

@Component
public class FmsFilter extends OncePerRequestFilter {

    public static final String FMS_PARAMETER_NAME = "_fms";

    private final Log logger = LogFactory.getLog(getClass());

    private final RequestMatcher requireFmsProtectionMatcher = new DefaultRequiresFmsMatcher();

    private boolean enabled = true;


    private static boolean equalsConstantTime(String expected, String actual) {
        if (expected == actual) {
            return true;
        }
        if (expected == null || actual == null) {
            return false;
        }
        // Encode after ensure that the string is not null
        byte[] expectedBytes = Utf8.encode(expected);
        byte[] actualBytes = Utf8.encode(actual);
        return MessageDigest.isEqual(expectedBytes, actualBytes);
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        request.setAttribute(HttpServletResponse.class.getName(), response);

        if (!this.enabled) {
            this.logger.warn("FMS filter disabled.");
            filterChain.doFilter(request, response);
            return;
        }

        String fmsToken = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            fmsToken = (String) session.getAttribute(FMS_PARAMETER_NAME);
            if (fmsToken == null) {
                fmsToken = UUID.randomUUID().toString();
                session.setAttribute(FMS_PARAMETER_NAME, fmsToken);
            }
        }
        request.setAttribute(FMS_PARAMETER_NAME, fmsToken);

        if (!this.requireFmsProtectionMatcher.matches(request)) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Did not protect against FMS since request did not match "
                        + this.requireFmsProtectionMatcher);
            }
            filterChain.doFilter(request, response);
            return;
        }

        var actualToken = request.getParameter(FMS_PARAMETER_NAME);
        if (equalsConstantTime(fmsToken, actualToken)) {
            if (session != null) {
                var newFmsToken = UUID.randomUUID().toString();
                session.setAttribute(FMS_PARAMETER_NAME, newFmsToken);
                request.setAttribute(FMS_PARAMETER_NAME, newFmsToken);
            }
        } else {
            this.logger.debug(
                    LogMessage.of(() -> "Invalid FMS token found for " + UrlUtils.buildFullRequestUrl(request)));
            response.sendError(HttpStatus.NOT_ACCEPTABLE.value(), HttpStatus.NOT_ACCEPTABLE.getReasonPhrase());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private static final class DefaultRequiresFmsMatcher implements RequestMatcher {

        private final HashSet<String> allowedMethods = new HashSet<>(Arrays.asList("POST", "PUT", "DELETE"));

        @Override
        public boolean matches(HttpServletRequest request) {
            return this.allowedMethods.contains(request.getMethod());
        }

        @Override
        public String toString() {
            return "FmsNotRequired " + this.allowedMethods;
        }

    }
}
