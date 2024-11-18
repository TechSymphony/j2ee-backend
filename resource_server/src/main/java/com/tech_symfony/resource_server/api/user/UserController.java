package com.tech_symfony.resource_server.api.user;

import com.tech_symfony.resource_server.api.beneficiary.BeneficiaryService;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryListVm;
import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignDetailVm;
import com.tech_symfony.resource_server.api.role.RoleService;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleDetailVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleListVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RolePostVm;
import com.tech_symfony.resource_server.api.user.viewmodel.ChangePasswordPostVm;
import com.tech_symfony.resource_server.api.user.viewmodel.UserDetailVm;
import com.tech_symfony.resource_server.api.user.viewmodel.UserListVm;
import com.tech_symfony.resource_server.api.user.viewmodel.UserPostVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final BeneficiaryService beneficiaryService;


    @GetMapping
    public Page<UserListVm> getAllUsers(@RequestParam Map<String, String> allParams) { return userService.findAll(allParams);}

    @GetMapping("/{id}")
    public UserDetailVm getUserById(@PathVariable Integer id) { return userService.findById(id);}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDetailVm createUser(@Valid @RequestBody UserPostVm user) {return userService.save(user);}

    @PutMapping("/{id}")
    public UserDetailVm updateUser(@PathVariable Integer id, @Valid @RequestBody UserPostVm user) {
        return userService.update(id,user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer id) {userService.delete(id);}

    @PutMapping("/{id}/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPasswordAdmin(@PathVariable Integer id) {userService.resetPassword(id);}

    @PutMapping("/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangePasswordPostVm changePasswordPostVm) {
        Integer currentId = authService.getCurrentUserAuthenticated().getId();
        userService.changePassword(currentId, changePasswordPostVm);
    }

    @PostMapping("/import/student")
    @ResponseStatus(HttpStatus.CREATED)
    public List<UserListVm> importStudents(@RequestParam("file") MultipartFile file) throws IOException {
        return userService.importStudents(file);
    }

    @GetMapping("/{id}/beneficiaries")
    public Set<BeneficiaryListVm> getBeneficiariesById(@PathVariable Integer id) {
        return beneficiaryService.findAllByUser(id);
    }

}
