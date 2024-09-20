package com.example.resource_server.api.role;

import com.example.resource_server.api.role.permission.Permission;
import com.example.resource_server.system.model.NamedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Table(name = "roles")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends NamedEntity {


	@Column(name = "description", length = Integer.MAX_VALUE)
	private String description;

	@ManyToMany
	@JoinTable(name = "role_permissions",
			joinColumns = @JoinColumn(name = "role_id"),
			inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Set<Permission> permissions = new LinkedHashSet<>();

}
