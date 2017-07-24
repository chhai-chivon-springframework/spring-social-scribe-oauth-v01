package com.phengtola.services;


import com.phengtola.entities.User;
import com.phengtola.forms.FrmSocialLogin;
import org.apache.ibatis.annotations.Insert;

public interface UserService {

	User findUserByEmail(String email);

	public boolean insertUserWithSocialAccount(FrmSocialLogin frmSocialLogin);

	public int isEmailExist(String email);
	
}
