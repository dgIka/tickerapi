package org.dgika.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.dgika.security.auth.UserDetailsImpl;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String HDR_REQUEST_ID = "X-Request-Id";
    private static final String MDC_REQUEST_ID = "rid";
    private static final String MDC_USER_ID = "uid";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        long startNs = System.nanoTime();

        String requestId = Optional.ofNullable(request.getHeader(HDR_REQUEST_ID))
                .filter(s -> !s.isBlank())
                .orElse(UUID.randomUUID().toString());

        MDC.put(MDC_REQUEST_ID, requestId);

        putUserIdToMdcIfPresent();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = (System.nanoTime() - startNs) / 1_000_000;

            String path = request.getRequestURI();
            String query = request.getQueryString();
            String fullPath = (query == null || query.isBlank()) ? path : (path + "?" + query);

            log.info("event=http_request method={} path={} status={} durationMs={}",
                    request.getMethod(),
                    fullPath,
                    response.getStatus(),
                    durationMs
            );

            MDC.remove(MDC_REQUEST_ID);
            MDC.remove(MDC_USER_ID);
        }
    }

    private void putUserIdToMdcIfPresent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return;

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetailsImpl u) {
            MDC.put(MDC_USER_ID, String.valueOf(u.getUserId()));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/actuator");
    }
}
