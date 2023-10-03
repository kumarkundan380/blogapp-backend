package com.blogapp.security;
import com.blogapp.exception.BlogAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.blogapp.constant.BlogAppConstant.AUTHORIZATION_HEADER;
import static com.blogapp.constant.BlogAppConstant.AUTHORIZATION_PREFIX;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	

	private final UserDetailsService userDetailsService;

	@Autowired
	public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwtToken = parseJwtToken(request);
		if(StringUtils.hasText(jwtToken)) {
			String userName = jwtUtil.getUserNameFromJwtToken(jwtToken);
			
			// Username should not be empty, context-auth must be empty
			if(StringUtils.hasText(userName) && SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails user = userDetailsService.loadUserByUsername(userName);
				// Validate token
				boolean isValid = jwtUtil.validateToken(jwtToken, user.getUsername());
				if(isValid) {
					UsernamePasswordAuthenticationToken authToken = 
							new UsernamePasswordAuthenticationToken(userName, user.getPassword(),user.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				} else {
					throw new BlogAppException("Token is Not Valid");
				}
			}
		}
		filterChain.doFilter(request, response);
		
	}

	private String parseJwtToken(HttpServletRequest request) {
		String headerAuth = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(AUTHORIZATION_PREFIX)) {
			return headerAuth.substring(7);
		}
		return null;
	}

}
