package com.phengtola.repositories;

import com.phengtola.entities.User;
import com.phengtola.forms.FrmSocialLogin;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository {


	// C = CREATE
	// R = READ
	// U = UPDATE
	// D = DELETE

	String C_USER_WITH_SOCIAL_ACCOUNT = "INSERT INTO users (" +
			"username," +
			"email," +
			"password," +
			"social_id," +
			"social_type," +
			"image_url," +
			"uuid) VALUES (" +
			"#{user.username}," +
			"#{user.email}," +
			"(SELECT md5(random()::text || clock_timestamp()::text)::uuid)," +
			"#{user.socialId}," +
			"UPPER(#{user.socialType})," +
			"#{user.imageUrl}," +
			"(SELECT md5(random()::text || clock_timestamp()::text)::uuid)" +
			")";

	String R_USER_BY_EMAIL = "SELECT COUNT(email) FROM users WHERE email=#{email}";



	@Select(" SELECT "
			+ " id,"
			+ "	username, "
			+ "	email, "
			+ "	password, "
			+ "	dob, "
			+ "	gender, "
			+ "	social_id, "
			+ "	social_type, "
			+ " image_url"
		+ " FROM users WHERE email=#{email}")
	@Results(value={
			@Result(property = "socialId" , column = "social_id"),
			@Result(property = "socialType" , column = "social_type"),
			@Result(property = "imageUrl" , column = "image_url"),
			@Result(property="roles", column="id",
					many = @Many(select  = "com.phengtola.repositories.RoleRepository.findRolesByUserId")
			)
	})
	public User findUserByEmail(@Param("email") String email);


	// Insert user with social account
	@Insert(C_USER_WITH_SOCIAL_ACCOUNT)
	public boolean insertUserWithSocialAccount(@Param("user") FrmSocialLogin frmSocialLogin);

	@Insert(R_USER_BY_EMAIL)
	public int isEmailExist(@Param("email") String email);
	
}
