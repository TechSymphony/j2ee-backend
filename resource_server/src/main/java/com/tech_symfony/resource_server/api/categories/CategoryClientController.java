package com.tech_symfony.resource_server.api.categories;

import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryMenuVm;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories/menus")
public class CategoryClientController {

    private final CategoryService categoryService;

    @GetMapping
    @Cacheable("menus")
    public List<CategoryMenuVm> getAllCategories() {
        return categoryService.getMenus();
    }
}
