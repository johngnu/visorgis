/*
 * ICG SRL - International Consulting Group 2011
 */
package bo.gob.ine.security;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.log4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class MDCFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            MDC.put("userName", authentication.getName());
        } else {
            MDC.put("userName", "[client]");
        }
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            MDC.remove("userName");
        }
    }

    @Override
    public void destroy() {
    }

}
