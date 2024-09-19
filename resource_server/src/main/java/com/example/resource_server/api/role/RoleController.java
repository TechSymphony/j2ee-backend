package com.example.resource_server.api.role;

import com.example.resource_server.api.role.viewmodel.RoleDetailVm;
import com.example.resource_server.api.role.viewmodel.RoleListVm;
import com.example.resource_server.api.role.viewmodel.RolePostVm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@ResponseBody
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
    public RoleDetailVm createRole(@RequestBody RolePostVm role) {
        return roleService.save(role);
    }

    @PutMapping("/{id}")
    public RoleDetailVm updateRole(@PathVariable Integer id, @RequestBody RolePostVm role) {
        return roleService.update(id, role);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable Integer id) {
        roleService.delete(id);
    }
}
