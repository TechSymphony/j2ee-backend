package com.tech_symfony.resource_server.api.role;

import com.tech_symfony.resource_server.api.beneficiary.Beneficiary;
import com.tech_symfony.resource_server.api.beneficiary.BeneficiaryPage;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryListVm;
import com.tech_symfony.resource_server.api.donation.Donation;
import com.tech_symfony.resource_server.api.donation.DonationPage;
import com.tech_symfony.resource_server.api.role.permission.PermissionRepository;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleDetailVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleListVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RolePostVm;
import com.tech_symfony.resource_server.commonlibrary.constants.MessageCode;
import com.tech_symfony.resource_server.commonlibrary.exception.BadRequestException;
import com.tech_symfony.resource_server.commonlibrary.exception.NotFoundException;
import com.tech_symfony.resource_server.system.pagination.PaginationCommand;
import com.tech_symfony.resource_server.system.pagination.SpecificationBuilderPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface RoleService {
    RolePage<RoleListVm> findAll(Map<String, String> params);

    Set<RoleListVm> findAll();

    RoleDetailVm findById(Integer id);

    RoleDetailVm save(RolePostVm role);

    RoleDetailVm update(Integer id, RolePostVm role);

    Boolean delete(Integer id);
}

@Service
@RequiredArgsConstructor
class DefaultRoleService implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;
    private final SpecificationBuilderPagination<Role> specificationBuilder;
    private final PaginationCommand<Role, RoleListVm> paginationCommand;

    @Override
    public RolePage<RoleListVm> findAll(Map<String, String> params) {
        return new RolePage<>(paginationCommand.execute(params, roleRepository,  roleMapper, specificationBuilder));
    }

    @Override
    public Set<RoleListVm> findAll() {
        return roleRepository.findAll().stream()
                .map(roleMapper::entityToRoleListVm)
                .collect(Collectors.toSet());
    }

    @Override
    public RoleDetailVm findById(Integer id) {
        return roleRepository.findById(id)
                .map(roleMapper::entityToRoleDetailVm)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));
    }

    @Override
    public RoleDetailVm save(RolePostVm role) {
        Role savedRole = roleRepository.save(roleMapper.rolePostVmToRole(role));
        return roleMapper.entityToRoleDetailVm(savedRole);
    }

    @Override
    @Transactional
    public RoleDetailVm update(Integer id, RolePostVm role) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));
        Role role1= roleMapper.updateRoleFromDto(role, existingRole);
        Role updatedRole = roleRepository.save(
                role1
        );
        return roleMapper.entityToRoleDetailVm(updatedRole);
    }

    @Override
    public Boolean delete(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));

        if (roleRepository.existsByUsersRole(id)) {
            throw new BadRequestException(MessageCode.RESOURCE_NOT_CONTAIN_CHILD_FOR_DELETION);
        }
        roleRepository.delete(role);
        return !roleRepository.existsById(id);
    }
}
