package com.green.user.service.impl;

import java.nio.file.attribute.UserPrincipal;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.green.user.dao.LoginDao;
import com.green.user.dao.UserDao;
import com.green.user.vo.OAuthAttributes;
import com.green.user.vo.SessionUser;
import com.green.user.vo.UserDetailsImpl;
import com.green.user.vo.UserVo;
@Service("customOAuth2UserService")
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
	
	@Autowired
    private LoginDao userRepository;
	
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        // 현재 진행중인 서비스. Google이면 google, Naver면 naver,...

        String userNameAttributeName = oAuth2UserRequest.getClientRegistration().
        		getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        // OAuth2 로그인 시 키 값이 된다. 구글은 키 값이 "sub"이고, 네이버는 "response"이고, 카카오는 "id"이다. 
        // 각각 다르므로 이렇게 따로 변수로 받아서 넣어줘야함.

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, 
        		oAuth2User.getAttributes());
        // OAuth2 로그인을 통해 가져온 OAuth2User의 attribute를 담아주는 of 메소드.

        UserVo user = saveOrUpdate(attributes);
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getAuthority()))
                , attributes.getAttributes()
                , attributes.getNameAttributeKey());
    }

    // 혹시 이미 저장된 정보라면, update 처리
    private UserVo saveOrUpdate(OAuthAttributes attributes) {
    	HashMap<String,Object> map=new HashMap<String, Object>();
    	map.put("email", attributes.getEmail());
        UserVo user = userRepository.login_security(map);
        if(user==null) {
        	user=new UserVo();
        	user.setEmail((String)map.get("email"));
        	return userRepository.save_oauth2(user);
        }
        else {
        	user.setNickname(attributes.getName());
        	return user;
        }
    }
}
