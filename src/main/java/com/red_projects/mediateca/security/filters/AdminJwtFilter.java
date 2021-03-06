package com.red_projects.mediateca.security.filters;

import com.nimbusds.jwt.JWTClaimsSet;
import com.red_projects.mediateca.communication.response.Response;
import com.red_projects.mediateca.security.JwtUtil;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AdminJwtFilter implements Filter {

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
                // Admin Check
                JWTClaimsSet claimsSet = JwtUtil.getJwtClaims(token);
                if (JwtUtil.isAdmin(claimsSet)) {
                    validToken = true;
                    chain.doFilter(request, response);
                }
            }
        }

        if (!validToken) {
            System.out.println("JWT Authentication Failed");
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            Response responseData = new Response("Verify Authorization");
            responseData.actionFailed();
            responseData.setMessage("Invalid Authorization Token");
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(responseData.toJson());
            httpServletResponse.setStatus(401);
            //chain.doFilter(request, response);
        }
    }
}
