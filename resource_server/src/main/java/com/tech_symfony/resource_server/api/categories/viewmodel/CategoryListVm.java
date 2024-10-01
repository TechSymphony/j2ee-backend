package com.tech_symfony.resource_server.api.categories.viewmodel;

import com.tech_symfony.resource_server.api.categories.Category;

public record CategoryListVm(int id, String name, Category parent) {
}
