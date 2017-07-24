package com.phengtola.services.impl;


import com.phengtola.entities.User;
import com.phengtola.forms.FrmSocialLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.phengtola.repositories.UserRepository;
import com.phengtola.services.UserService;

@Service("apiUserServiceImpl")
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public User findUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findUserByEmail(email);
	}

	@Override
	public boolean insertUserWithSocialAccount(FrmSocialLogin frmSocialLogin) {
		return userRepository.insertUserWithSocialAccount(frmSocialLogin);
	}

	@Override
	public int isEmailExist(String email) {
		return userRepository.isEmailExist(email);
	}


}
