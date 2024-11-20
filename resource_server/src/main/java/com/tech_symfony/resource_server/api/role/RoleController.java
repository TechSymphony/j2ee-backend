package com.tech_symfony.resource_server.api.role;

import com.tech_symfony.resource_server.api.donation.DonationPage;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleDetailVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleListVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RolePostVm;
import com.tech_symfony.resource_server.api.user.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@PreAuthorize("hasPermission('Role', 'MANGE_ROLES')")
public class RoleController {

    private final RoleService roleService;
    private final AuthService authService;

    @GetMapping
    public RolePage<RoleListVm> getAllRoles(@RequestParam Map<String, String> allParams) {
        return roleService.findAll(allParams);
    }

    @GetMapping("/options")
    public Set<RoleListVm> getRoleOptions() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public RoleDetailVm getRoleById(@PathVariable Integer id) {
        return roleService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDetailVm createRole(@Valid @RequestBody RolePostVm role) {
        return roleService.save(role);
    }

    @PutMapping("/{id}")
    public RoleDetailVm updateRole(@PathVariable Integer id, @Valid @RequestBody RolePostVm role) {
        return roleService.update(id, role);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable Integer id) {
        roleService.delete(id);
    }
}
