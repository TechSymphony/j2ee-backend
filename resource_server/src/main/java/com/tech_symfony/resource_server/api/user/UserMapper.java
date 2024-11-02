package com.tech_symfony.resource_server.api.user;

import com.tech_symfony.resource_server.api.user.viewmodel.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserListVm enityToUserListVm(User user);
	UserDetailVm enityToUserDetailVm(User user);

	User updateUserFromDto(UserPostVm userPostVm, @MappingTarget User user);
	User userPostVmToUser(UserPostVm userPostVm);

	BasicUserDetailVm enityToBasicUserDetailVm(User user);
	User updateProfileFromDto(BasicUserPostVm basicUserPostVm, @MappingTarget User user);
}
