package com.phengtola.repositories;

import com.phengtola.entities.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleRepository {

	@Select(" SELECT "
			+ " R.id,"
			+ "	R.name "
		+ " FROM user_roles UR INNER JOIN roles R ON UR.role_id = R.id"
		+ " WHERE UR.user_id=#{user_id}")
	public List<Role> findRolesByUserId(@Param("user_id") int userId);
	
	
}
