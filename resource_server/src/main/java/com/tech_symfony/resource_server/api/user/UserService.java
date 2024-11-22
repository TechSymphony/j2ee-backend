package com.tech_symfony.resource_server.api.user;

import com.tech_symfony.resource_server.api.role.Role;
import com.tech_symfony.resource_server.api.role.RoleMapper;
import com.tech_symfony.resource_server.api.role.RoleRepository;
import com.tech_symfony.resource_server.api.role.permission.PermissionRepository;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleDetailVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleListVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RolePostVm;
import com.tech_symfony.resource_server.api.user.viewmodel.*;
import com.tech_symfony.resource_server.commonlibrary.constants.MessageCode;
import com.tech_symfony.resource_server.commonlibrary.exception.BadRequestException;
import com.tech_symfony.resource_server.commonlibrary.exception.NotFoundException;
import com.tech_symfony.resource_server.system.importing.ImportExcelService;
import com.tech_symfony.resource_server.system.mail.EmailService;
import com.tech_symfony.resource_server.system.pagination.PaginationCommand;
import com.tech_symfony.resource_server.system.pagination.SpecificationBuilderPagination;
import com.tech_symfony.resource_server.system.password.RandomPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface UserService {
    Page<UserListVm> findAll(Map<String, String> params);

    UserDetailVm findById(Integer id);

    UserDetailVm save(UserPostVm user);

    UserDetailVm update(Integer id, UserPostVm user);

    Boolean delete(Integer id);

    Boolean resetPassword(Integer id);

    Boolean changePassword(Integer id, ChangePasswordPostVm changePasswordPostVm);

    BasicUserDetailVm getProfileById(Integer id);
    
    BasicUserDetailVm updateProfile(Integer id, BasicUserPostVm user);

    List<UserListVm> importFrom(MultipartFile file, boolean isStudent) throws IOException;
}

@Service
@RequiredArgsConstructor
class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ImportExcelService importExcelService;
    private final RandomPasswordService randomPasswordService;

    private final PaginationCommand<User, UserListVm> paginationCommand;
    private final SpecificationBuilderPagination<User> specificationBuilder;

    @Override
    public Page<UserListVm> findAll(Map<String, String> params) {
        return paginationCommand.execute(params, userRepository, userMapper, specificationBuilder);
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
        newUser.setPassword(passwordEncoder.encode("password"));
        User savedUser = userRepository.save((newUser));

        // TODO: send mail

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

    @Override
    public Boolean resetPassword(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));
        String password = randomPasswordService.generatePassword();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        emailService.sendEmail(user.getEmail(), "Reset Password", "Your password has been reset to '"+password+"'");
        return true;
    }

    @Override
    public Boolean changePassword(Integer id, ChangePasswordPostVm changePasswordPostVm) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));

        if(!passwordEncoder.matches(changePasswordPostVm.currentPassword(),user.getPassword())){
            throw new BadRequestException(MessageCode.CURRENT_PASSWORD_NOT_CORRECT);
//            throw new EntityNotFoundException(MessageCode.CURRENT_PASSWORD_NOT_CORRECT);
        }
        user.setPassword(passwordEncoder.encode(changePasswordPostVm.newPassword()));
        userRepository.save(user);
        emailService.sendEmail(user.getEmail(), "Thay đổi mật khẩu", "Thay đổi mật khẩu thành công, vui lòng sử dụng mật khẩu mới để đăng nhập: " + changePasswordPostVm.newPassword());

        return true;
    }

    @Override
    public BasicUserDetailVm getProfileById(Integer id) {
        return userRepository.findById(id)
                .map((userMapper::enityToBasicUserDetailVm))
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));
    }

    @Override
    public BasicUserDetailVm updateProfile(Integer id, BasicUserPostVm user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));

        User user1 = userMapper.updateProfileFromDto(user, existingUser);
        User updatedUser = userRepository.save(user1);
        return userMapper.enityToBasicUserDetailVm((updatedUser));
    }


    public List<UserListVm> importFrom(MultipartFile file, boolean isStudent) throws IOException {
        return importExcelService.importFrom(file, isStudent)
                .stream()
                .map(userMapper::enityToUserListVm)
                .collect(Collectors.toList());
    }
}

