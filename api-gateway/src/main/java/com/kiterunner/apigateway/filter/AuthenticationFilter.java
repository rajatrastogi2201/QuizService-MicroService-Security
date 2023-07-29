package com.kiterunner.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.net.HttpHeaders;
import com.kiterunner.apigateway.util.JwtUtil;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
	
	@Autowired
	private RestTemplate template;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private RouteValidator validator;
	
	public AuthenticationFilter() {
		super(Config.class);
	}
	

	@Override
	public GatewayFilter apply(Config config) {
		
		return ((exchange,chain) -> {
			if(validator.isSecured.test(exchange.getRequest())) {
				// header contains token or not
				if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
					throw new RuntimeException("missing authorization header");
				
				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				if(authHeader!=null && authHeader.startsWith("Bearer ")) {
					authHeader=authHeader.substring(7);
				}
				try {
					//Rest Call to Auth Service
//					template.getForObject("http://AUTH-SERVICE//validate?token"+authHeader, String.class);
					jwtUtil.validateToken(authHeader);
					
				}catch(Exception e) {
					System.out.println("Token not validated");
					throw new RuntimeException("unauthorized access to application");
				}
				
			}
			return chain.filter(exchange);
		});
	}
	
	public static class Config{
		
	}


}
