package com.redprojects.mediateca.security.filters;

import com.redprojects.mediateca.communication.response.Response;
import com.redprojects.mediateca.security.JwtUtil;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserJwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // get token from request header
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String headerString = httpServletRequest.getHeader("Authorization");

        boolean validToken = false;
        if (!headerString.isEmpty()) {
            String token = headerString.substring(7);
            // JWT Check
            if (JwtUtil.isJWtValid(token)) {
                validToken = true;
                Response responseData = new Response("Verify Authorization");
                responseData.actionSucceeded();
                responseData.setMessage("Valid Authorization Token");
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setContentType("application/json");
                httpServletResponse.getWriter().write(responseData.toJson());
                httpServletResponse.setStatus(200);
                //chain.doFilter(request, response);
            }
        }

        if (!validToken) {
            System.out.println("Failed");
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            Response responseData = new Response("Verify Authorization");
            responseData.actionFailed();
            responseData.setMessage("Invalid Authorization Token");
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(responseData.toJson());
            httpServletResponse.setStatus(401);
        }
    }
}
