package com.tech_symfony.resource_server.api.role;

import com.tech_symfony.resource_server.api.role.viewmodel.RoleDetailVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleListVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RolePostVm;
import com.tech_symfony.resource_server.system.pagination.GenericMapperPagination;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper extends GenericMapperPagination<Role, RoleListVm> {

	RoleListVm entityToRoleListVm(Role role);
	RoleDetailVm entityToRoleDetailVm(Role role);

	Role updateRoleFromDto(RolePostVm rolePostVm, @MappingTarget Role role);
	Role rolePostVmToRole(RolePostVm rolePostVm);

}
