package com.tech_symfony.resource_server.api.user;

import com.tech_symfony.resource_server.api.role.Role;
import com.tech_symfony.resource_server.api.role.RoleMapper;
import com.tech_symfony.resource_server.api.role.RoleRepository;
import com.tech_symfony.resource_server.api.role.permission.PermissionRepository;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleDetailVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleListVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RolePostVm;
import com.tech_symfony.resource_server.api.user.viewmodel.UserDetailVm;
import com.tech_symfony.resource_server.api.user.viewmodel.UserListVm;
import com.tech_symfony.resource_server.api.user.viewmodel.UserPostVm;
import com.tech_symfony.resource_server.commonlibrary.constants.MessageCode;
import com.tech_symfony.resource_server.commonlibrary.exception.BadRequestException;
import com.tech_symfony.resource_server.commonlibrary.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public interface UserService {
    List<UserListVm> findAll(Integer page, Integer limit, String sortBy);

    UserDetailVm findById(Integer id);

    UserDetailVm save(UserPostVm user);

    UserDetailVm update(Integer id, UserPostVm user);

    Boolean delete(Integer id);
}

@Service
@RequiredArgsConstructor
class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public List<UserListVm> findAll(Integer page, Integer limit, String sortBy) {
        Pageable paging = PageRequest.of(page, limit, Sort.by(sortBy));
        return userRepository.findAll(paging).stream()
                .map(userMapper::enityToUserListVm)
                .collect(Collectors.toList());
    }

    @Override
    public UserDetailVm findById(Integer id) {
        return userRepository.findById(id)
                .map((userMapper::enityToUserDetailVm))
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));
    }



    @Override
    public UserDetailVm save(UserPostVm user) {
        User newUser = userMapper.userPostVmToUser(user);
        newUser.setPassword("123123");
        User savedUser = userRepository.save((newUser));
        return userMapper.enityToUserDetailVm(savedUser);
    }

    @Override
    @Transactional
    public UserDetailVm update(Integer id, UserPostVm user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));

        User user1 = userMapper.updateUserFromDto(user, existingUser);
        User updatedUser = userRepository.save(user1);
        return userMapper.enityToUserDetailVm((updatedUser));
    }


    @Override
    public Boolean delete(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));

        userRepository.delete(user);
        return !userRepository.existsById(id);
    }
}
