package com.tech_symfony.auth_server.service;

import com.tech_symfony.auth_server.model.role.Role;
import com.tech_symfony.auth_server.model.role.permission.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface RoleService {
    Collection<? extends GrantedAuthority> extractAuthoritiesFromRole(Role role);
}

@Service
class DefaultRoleService implements RoleService {

    public Collection<? extends GrantedAuthority> extractAuthoritiesFromRole(Role role) {

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if(role == null) {
            return authorities;
        }

        Set<Permission> permissions = role.getPermissions();

        for (Permission permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        }

        if (role.getIsSuperAdmin())
            authorities.add(new SimpleGrantedAuthority("SUPER_ADMIN"));

        return authorities;
    }
}
