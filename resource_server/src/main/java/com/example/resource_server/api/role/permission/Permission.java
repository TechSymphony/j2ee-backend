package com.example.resource_server.api.role.permission;

import com.example.resource_server.api.role.Role;
import com.example.resource_server.system.model.NamedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Table(name = "permissions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permission extends NamedEntity {


	private String decription;

	@Column(name = "description", length = Integer.MAX_VALUE)
	private String description;



}
