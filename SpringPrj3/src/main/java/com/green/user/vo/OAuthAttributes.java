package com.green.user.vo;

import java.util.Map;

import org.springframework.security.core.userdetails.User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthAttributes {
	private Map<String, Object> attributes;
	   private String nameAttributeKey;
	   private String name;
	   private String email;
	   private String picture;
	   
	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email,
			String picture) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
		this.picture = picture;
	}

	@Builder
	public static OAuthAttributes of(String registrationId, String userNameAttributeName,
			Map<String, Object> attributes) {
		// TODO Auto-generated method stub
		 return ofGoogle(userNameAttributeName, attributes);
	}
	
	private static OAuthAttributes ofGoogle(String userNameAttributeName,
            Map<String, Object> attributes) {
		return OAuthAttributes.builder()
		.name((String) attributes.get("name"))
		.email((String) attributes.get("email"))
		.picture((String) attributes.get("picture"))
		.attributes(attributes)
		.nameAttributeKey(userNameAttributeName)
		.build();
		}
	
	public UserVo toEntity() {
		return new UserVo(name,email);
	}

}
