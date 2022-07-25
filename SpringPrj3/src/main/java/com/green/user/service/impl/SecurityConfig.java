package com.green.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private CustomOAuth2UserService customOAuth2UserService; //oauth2 UserService 의존성 주입
	@Autowired
	private UserLoginFailHandler userLoginFailHandler; //loginFaileHandler 의존성 주입
	
	@Bean	
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http	//메소드 체이닝 방식으로 config 설정
			.csrf().disable()	//cross site request forgery 방지 미사용
			.headers().frameOptions().disable()
			.and()
				.authorizeRequests()
				.antMatchers("/","/login","/loginProcess","/loginpage","/sumbit",
						"/css/**","/img/**","/js/**").permitAll() 
				  // 위 경로들은 인증이 필요없음
				.antMatchers("/Mypage/Manage").hasRole("ADMIN") // 마이페이지의 회원관리 페이지는 ADMIN 권한 필요
				.anyRequest().authenticated() // 그외 URL은 로그인필요
			.and()
				.formLogin() 
				.loginPage("/loginpage") // 인증이 필요한 페이지 접근시 리다이렉팅 되는 URL
				.usernameParameter("userId")
				.passwordParameter("userPw")
				.loginProcessingUrl("/login") // 인증 과정은 /login에서 처리. 시큐리티의 UserDetailService로 향함
				.failureHandler(userLoginFailHandler)
				.successForwardUrl("/") // 로그인 성공시 home.jsp로 이동     
				.permitAll()
			.and()
				.logout()
				.logoutSuccessUrl("/") // 로그아웃 성공시 home.jsp로 이동
			.and()
				.oauth2Login() // oauth2 로그인 설정
				.loginPage("/loginpage")
				.defaultSuccessUrl("/")
				.userInfoEndpoint()
				.userService(customOAuth2UserService); 
				// oauth2의 UserDetailService는 CustomOAuth2UserService로 구현	
		http.headers().frameOptions().sameOrigin();
		return http.build();
	}
	
	@Bean
    public InMemoryClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }

	private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
            .clientId("433814286993-i53h47j5oosfdveb2i33v6b8tiajkn46.apps.googleusercontent.com")
            .clientSecret("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")
            .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUriTemplate("http://localhost:9090/login/oauth2/code/google")
            .scope("email")
            .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
            .tokenUri("https://www.googleapis.com/oauth2/v4/token")
            .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
            .userNameAttributeName(IdTokenClaimNames.SUB)
            .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
            .clientName("Google")
            .build();
    }
}