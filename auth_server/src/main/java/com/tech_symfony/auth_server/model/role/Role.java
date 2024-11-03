package com.tech_symfony.auth_server.model.role;


import com.tech_symfony.auth_server.model.NamedEntity;
import com.tech_symfony.auth_server.model.role.permission.Permission;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "roles")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends NamedEntity {

	@ColumnDefault("false")
	@Column(name = "is_super_admin")
	private Boolean isSuperAdmin;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "role_permission",
		joinColumns = @JoinColumn(name = "role_id"),
		inverseJoinColumns = @JoinColumn(name = "permission_id")
	)
	private Set<Permission> permissions;
	
}
