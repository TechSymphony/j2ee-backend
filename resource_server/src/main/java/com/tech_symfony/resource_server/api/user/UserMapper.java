package com.tech_symfony.resource_server.api.user;

import com.tech_symfony.resource_server.api.role.Role;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleDetailVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleListVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RolePostVm;
import com.tech_symfony.resource_server.api.user.viewmodel.UserDetailVm;
import com.tech_symfony.resource_server.api.user.viewmodel.UserListVm;
import com.tech_symfony.resource_server.api.user.viewmodel.UserPostVm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserListVm enityToUserListVm(User user);
	UserDetailVm enityToUserDetailVm(User user);

	User updateUserFromDto(UserPostVm userPostVm, @MappingTarget User user);
	User userPostVmToUser(UserPostVm userPostVm);

}
