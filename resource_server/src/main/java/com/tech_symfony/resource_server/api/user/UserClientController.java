package com.tech_symfony.resource_server.api.user;


import com.tech_symfony.resource_server.api.user.viewmodel.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
public class UserClientController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping()
    public BasicUserDetailVm getProfileById() {return userService.getProfileById(authService.getCurrentUserAuthenticated().getId());}

@PutMapping()
public BasicUserDetailVm updateProfile(@Valid @RequestBody BasicUserPostVm user) {
        Integer currentId = authService.getCurrentUserAuthenticated().getId();
    return userService.updateProfile(currentId,user);
}

}



