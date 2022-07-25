package com.green.user.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.green.user.dao.LoginDao;
import com.green.user.service.LoginService;
import com.green.user.vo.UserDetailsImpl;
import com.green.user.vo.UserVo;

@Service("userDetailsService")
public class UserDetailServiceCustom implements UserDetailsService {
	//spring-servlet에서 설정 등록후 UserDetailService를 implements하면 
	//로그인 form post가 실행될때 스프링 시큐리티가 이 클래스를 타고 들어옴
	@Autowired
	private  LoginDao  loginDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetailsImpl userDetail = new UserDetailsImpl();
		HashMap<String,Object> map=new HashMap<String, Object>();

		map.put("email", username);
		
		UserVo userVo=loginDao.login_security(map); //DB 연동부분. 존재하는 사용자면 UserVo 정보들 반환하는 함수
		
		if(userVo==null) { // 테이블에 없으면 userVo는 null
			return null;
		}
		else {
			userDetail.setUsername(userVo.getEmail());
			userDetail.setPassword("{noop}"+userVo.getPasswd()); // {noop} : PasswordEncoder 미사용
			List<String> authList=new ArrayList<String>();
			authList.add(userVo.getAuthority());
			userDetail.setAuthorities(authList);
		}
		return userDetail; // 시큐리티에 UserVo를 UserDeatils 형태로 반환해야함
	}
}
