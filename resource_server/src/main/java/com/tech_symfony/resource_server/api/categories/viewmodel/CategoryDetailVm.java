package com.tech_symfony.resource_server.api.categories.viewmodel;

import com.tech_symfony.resource_server.api.categories.Category;

public record CategoryDetailVm(int id, String name, Category parent) {
}
