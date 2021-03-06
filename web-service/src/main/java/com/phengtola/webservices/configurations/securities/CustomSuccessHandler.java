package com.phengtola.webservices.configurations.securities;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component("customSuccessHandler")
public class CustomSuccessHandler implements AuthenticationSuccessHandler{


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		String targetUrl = determineTargetUrl(authentication);
		System.out.println("User has been logged in successfully and redirect to " + targetUrl);
		if(response.isCommitted()){
			System.out.println("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}
		response.sendRedirect(targetUrl);
	}
	
	
	/**
	 * This method extracts the roles of currently logged-in user and return
	 * appropriate URL according to user's role.
	 * @param authentication
	 * @return
	 */
	public String determineTargetUrl(Authentication authentication){
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		List<String> roles = new ArrayList<String>();
		for(GrantedAuthority authority: authorities){
			System.out.println("ROLE: "+ authority.getAuthority());
			roles.add(authority.getAuthority());
		}
		if(roles.contains("ROLE_API_DEV")) {
			return "/docs/api/v1";
		}else{
			return "/error/403"; // Access Denied
		}
	}
	
	
	
	
}
