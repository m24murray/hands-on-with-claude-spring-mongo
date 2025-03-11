package com.example.userservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Combined filter for logging both HTTP request and response details.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            logRequest(requestWrapper);
            logResponse(requestWrapper, responseWrapper);
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String method = request.getMethod();
        String path = request.getRequestURI();
        String queryString = request.getQueryString();
        
        // Get request content
        byte[] content = request.getContentAsByteArray();
        String contentString = new String(content, StandardCharsets.UTF_8);
        
        if (content.length > 0) {
            log.debug("REQUEST {} {}{}: BODY={}", 
                    method, path, queryString != null ? "?" + queryString : "", contentString);
        } else {
            log.debug("REQUEST {} {}{}: EMPTY BODY", 
                    method, path, queryString != null ? "?" + queryString : "");
        }
    }

    private void logResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        String method = request.getMethod();
        String path = request.getRequestURI();
        int status = response.getStatus();
        
        // Get response content
        byte[] content = response.getContentAsByteArray();
        String contentString = new String(content, StandardCharsets.UTF_8);
        
        if (content.length > 0) {
            log.debug("RESPONSE for {} {}: STATUS={}, BODY={}", 
                    method, path, status, contentString);
        } else {
            log.debug("RESPONSE for {} {}: STATUS={}, EMPTY BODY", 
                    method, path, status);
        }
    }
}