package com.tech_symfony.resource_server.api.categories.viewmodel;

import java.util.Set;

public record CategoryMenuVm(int id, String name, Set<CategoryMenuVm> children) {
}
