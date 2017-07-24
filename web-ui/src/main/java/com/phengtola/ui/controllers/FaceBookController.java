package com.phengtola.ui.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phengtola.entities.Role;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.phengtola.entities.User;
import com.phengtola.forms.FrmSocialLogin;
import com.phengtola.forms.FrmWebLogin;


@Controller
@RequestMapping(value = "/facebook")
@PropertySource(value = { "classpath:social.properties" })
public class FaceBookController {

	@Autowired
	private Environment environment;

	private static Logger logger = LoggerFactory.getLogger(FaceBookController.class);

	@Value("${FACEBOOK_APP_API_KEY}")
	private String YOUR_API_KEY;
	@Value("${FACEBOOK_API_SECRET}")
	private String YOUR_API_SECRET;
	@Value("${DOMAIN}")
	private String DOMAIN;

	@Value("${WEB_SERVICE_URL}")
	private String WEB_SERVICE_URL;



	private static String CALLBACK_URL = "/facebook/callback";

	private static final Token EMPTY_TOKEN = null;



	// permission scope
	private static final List<String> SCOPES = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("public_profile");
			add("email");
		}
	};

	// API End point
	private static final String USER_PROFILE_API = "https://graph.facebook.com/v2.8/me";
	private static final String QUERY = "?fields=id,name,first_name,last_name,gender,email";

	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public void signin(HttpServletRequest request, HttpServletResponse response) throws IOException {

		logger.debug("signin");


		System.out.println("signin    ====>>>> domain =====>>>>> " + DOMAIN);

		System.out.println(YOUR_API_KEY);
		System.out.println(YOUR_API_SECRET);

		String secretState = "secret" + new Random().nextInt(999_999);
		request.getSession().setAttribute("SECRET_STATE", secretState);

		System.out.println("CALLBACK_URL =====>>>>> " + DOMAIN + CALLBACK_URL);

		/*OAuthService service = new ServiceBuilder().provider(FacebookApi.class).apiKey(YOUR_API_KEY)
				.apiSecret(YOUR_API_SECRET).callback(HOST + CALLBACK_URL).scope(String.join(",", SCOPES))
				.state(secretState).grantType("code").connectTimeout(10).build();*/

		
		OAuth20Service service = new ServiceBuilder().apiKey(YOUR_API_KEY)
				.apiSecret(YOUR_API_SECRET).callback(DOMAIN + CALLBACK_URL).scope(String.join(",", SCOPES))
				.state(secretState).build(FacebookApi.instance());
		
		String redirectURL = service.getAuthorizationUrl();

		System.out.println("redirectURL " + redirectURL);
		
		logger.info("redirectURL:{}", redirectURL);

		response.sendRedirect(redirectURL);
	}

	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public String callback(@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "state", required = false) String state, HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("callback");
		logger.info("code:{}", code);
		logger.info("state:{}", state);

		try{
			
			System.out.println("code " + code);
		
		String secretState = (String) request.getSession().getAttribute("SECRET_STATE");
		String userHash = "";

//		System.out.println("callback : " + domain);

		if (secretState.equals(state)) {
			logger.info("State value does match!");
		} else {
			logger.error("Ooops, state value does not match!");
		}

		//		OAuthService service = new ServiceBuilder().provider(FacebookApi.class).apiKey(YOUR_API_KEY)
		//		.apiSecret(YOUR_API_SECRET).callback(HOST + CALLBACK_URL).build();
		
		OAuth20Service service = new ServiceBuilder().apiKey(YOUR_API_KEY)
				.apiSecret(YOUR_API_SECRET).callback(DOMAIN + CALLBACK_URL).build(FacebookApi.instance());
		
		final String requestUrl = USER_PROFILE_API + QUERY;
		
		//final Verifier verifier = new Verifier(code);
		final OAuth2AccessToken accessToken = service.getAccessToken(code);
		
		final OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, requestUrl);
		service.signRequest(accessToken, oauthRequest);
		
		//final Response resourceResponse = oauthRequest.send();
		final Response resourceResponse = service.execute(oauthRequest);
		
		logger.info("code:{}", resourceResponse.getCode());
		logger.info("body:{}", resourceResponse.getBody());
		logger.info("message:{}", resourceResponse.getMessage());
		
		final JSONObject obj = new JSONObject(resourceResponse.getBody());
		logger.info("json:{}", obj.toString());
		
		System.out.println( obj.toString());

		

		FrmSocialLogin frmSocialLogin = new FrmSocialLogin();
		try{
			frmSocialLogin.setEmail(obj.getString("email"));
		}catch(Exception e){
			frmSocialLogin.setEmail(obj.getString("id"));
			e.printStackTrace();
		}

			frmSocialLogin.setUsername(obj.getString("name"));
			frmSocialLogin.setImageUrl("http://graph.facebook.com/" + obj.getString("id") + "/picture?type=large");
			frmSocialLogin.setSocialId(obj.getString("id"));
			frmSocialLogin.setSocialType("F"); // SOCIAL_TYPE must be equal with F, G, T (G = GOOGLE ; F=FACEBOOK  ; T=TWITTER)

			// Header - use to put API Baisc Authentication
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Basic UkVTVGZ1bFVzZXI6UkVTVGZ1bFBhc3N3b3Jk");

			// RestTemplate - use to request data from web service
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

			HttpEntity<Object> requestAPI = new HttpEntity<Object>(frmSocialLogin, headers);
			ResponseEntity<Map> responsAPI = restTemplate.exchange("http://localhost:9090/api/v1/authentication/user/insert-by-social-account", HttpMethod.POST,
				requestAPI, Map.class);

			Map<String, Object> map = (HashMap<String, Object>) responsAPI.getBody();


			if(map.get("data") != null){

				Map<String, Object> userMap = (HashMap<String, Object>)map.get("data");

				System.out.print(userMap);

				User user = new User();
				user.setEmail((String) userMap.get("email"));
				user.setPassword((String) userMap.get("password"));
				user.setId((Integer) userMap.get("id"));
				user.setUsername((String) userMap.get("username"));
				user.setImageUrl((String) userMap.get("imageUrl"));
				user.setSocialId((String) userMap.get("socialId"));
				user.setSocialType((String) userMap.get("socialType"));

				List<Role> roles = new ArrayList<Role>();
				List<HashMap<String, Object>> dataRoles = (List<HashMap<String, Object>>) userMap.get("roles");
				for (Map<String , Object> dataRole  : dataRoles) {
					Role role = new Role();
					role.setId((Integer)dataRole.get("id"));
					role.setName((String) dataRole.get("name"));
					roles.add(role);
				}
				System.out.println(dataRoles);
				user.setRoles(roles);

				// TODO: Login

				Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
						user.getAuthorities());

				SecurityContext context = new SecurityContextImpl();
				context.setAuthentication(authentication);

				SecurityContextHolder.setContext(context);

			}else{
				System.out.println("User not found!");
			}


			/////////




		request.getSession().setAttribute("FACEBOOK_ACCESS_TOKEN", accessToken);


		return "redirect:/profile";
		
		}catch(Exception e){
			e.printStackTrace();
			return "/login";
		}
	}

}
