package com.phengtola.webservices.configurations.securities.api;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("customRESTfulAuthenticationEntryPoint")
public class CustomRESTfulAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException auth)
			throws IOException, ServletException {




		System.out.println(auth.getMessage());
		request.setAttribute("msg", auth.getMessage());
		request.getRequestDispatcher("/error/401").forward(request, response);
	}

}
