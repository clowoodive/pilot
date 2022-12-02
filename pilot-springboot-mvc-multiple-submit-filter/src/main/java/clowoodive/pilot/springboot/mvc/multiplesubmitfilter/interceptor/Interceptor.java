package clowoodive.pilot.springboot.mvc.multiplesubmitfilter.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class Interceptor implements HandlerInterceptor {

    private final Log logger = LogFactory.getLog(getClass());
    private final int denyGapTimeMs = 300;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("preHandle, " + request.getRequestURI() + ", " + request.getQueryString());

        HttpSession session = request.getSession(false);
        if (session != null && request.getMethod().equals("POST")) {
            String oldMethod = (String) session.getAttribute("req_method");
            String oldPath = (String) session.getAttribute("req_path");
            Long oldTime = (Long) session.getAttribute("req_time");

            long curMillis = System.currentTimeMillis();

            if (Strings.isEmpty(oldMethod) || Strings.isEmpty(oldPath) || oldTime == null
                    || !request.getMethod().equals(oldMethod) || !request.getRequestURI().equals(oldPath)) {
                session.setAttribute("req_method", request.getMethod());
                session.setAttribute("req_path", request.getRequestURI());
            } else {
                long gap = curMillis - oldTime;
                if (gap < denyGapTimeMs) {
                    logger.warn(oldTime + " - " + curMillis + " = " + gap + " millis");
                    throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "same request was received in a short time");
                }
            }
            session.setAttribute("req_time", curMillis);
        }

        return true;
    }
}
