package com.tech_symfony.resource_server.api.user;

import com.tech_symfony.resource_server.api.role.RoleService;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleDetailVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleListVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RolePostVm;
import com.tech_symfony.resource_server.api.user.viewmodel.UserDetailVm;
import com.tech_symfony.resource_server.api.user.viewmodel.UserListVm;
import com.tech_symfony.resource_server.api.user.viewmodel.UserPostVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserListVm> getAllUsers() { return userService.findAll();}

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

}
