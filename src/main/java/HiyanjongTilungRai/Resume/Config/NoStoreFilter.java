// src/main/java/HiyanjongTilungRai/Resume/Config/NoStoreFilter.java
package HiyanjongTilungRai.Resume.Config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@Order(1)
public class NoStoreFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;


        String uri = req.getRequestURI();
        if (uri.startsWith("/admin") || uri.startsWith("/api/")) {
            res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            res.setHeader("Pragma", "no-cache");
            res.setDateHeader("Expires", 0);
        }
        chain.doFilter(request, response);
    }
}
