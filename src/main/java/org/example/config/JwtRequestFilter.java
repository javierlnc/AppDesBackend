package org.example.config;
import java.io.IOException;
import java.net.http.HttpClient;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.careydevelopment.jwtguide.service.JwtUserDetailsService;
import com.careydevelopment.jwtguide.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;


public class JwtRequestFilter  extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtRequestFilter.class);
    @Autowired
	private JwtUserDetailsService jwtUserDetailsService;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String usernme = null;
        String jwtToken = null;

        if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")){
            jwtToken = requestTokenHeader.substring(7);
            JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(jwtToken);
            try {
                usernme = jwtTokenUtil.getUsernameFromToken();
                validateToken(jwtTokenUtil, username, chain, request, response);
            } catch (IllegalArgumentException e) {
                LOG.error("Unable to get JWT Token", e);
            }catch (ExpiredJwtException e){
                LOG.error("JWT Token has expired", e);
            }
        }else {
            chain.doFilter(request, response);
        }

    }
    private void validateToken(JwtTokenUtil jwtTokenUtil, String username, FilterChain chain, HttpServletRequest request, 
    HttpServletResponse response) throws SerialException, IOException{
        if (username != null  && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
        }
        chain.doFilter(request, response);
    }

    
}