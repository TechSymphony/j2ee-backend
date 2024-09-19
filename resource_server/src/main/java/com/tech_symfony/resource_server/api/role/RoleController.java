package com.tech_symfony.resource_server.api.role;

import com.tech_symfony.resource_server.api.role.viewmodel.RoleDetailVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleListVm;
import com.tech_symfony.resource_server.api.role.viewmodel.RolePostVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleListVm> getAllRoles() {
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
