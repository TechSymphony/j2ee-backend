package com.tech_symfony.resource_server.api.categories;

import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryMenuVm;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryClientController {

    private final CategoryService categoryService;

    @GetMapping("/public/menus")
    @Cacheable("/menus")
    public List<CategoryMenuVm> getAllCategories() {
        return categoryService.getMenus();
    }
}
