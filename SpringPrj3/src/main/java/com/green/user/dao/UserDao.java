package com.green.user.dao;

import java.util.HashMap;
import java.util.List;

import com.green.user.vo.AccomInformVo;
import com.green.user.vo.TrainInformVo;
import com.green.user.vo.UserVo;

public interface UserDao {

	List<UserVo> getUserList();

	UserVo getView(String id);

	List<UserVo> getViewName(String name);

	UserVo login(HashMap<String, Object> map);

	void insertReserv(HashMap<String, Object> map);

	List<TrainInformVo> getTrainInform(HashMap<String, Object> map);

	void insertAccomReserv(HashMap<String, Object> map);

	List<AccomInformVo> getAccomInfrom(HashMap<String, Object> map);

}
