package com.tech_symfony.resource_server.api.categories;

import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryDetailVm;
import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryListVm;
import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryPostVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@PreAuthorize("hasPermission('Category', 'MANAGE_CATEGORIES')")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Page<CategoryListVm> getAllCategories(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "id") String sortBy) {
        return categoryService.findAll(page, limit, sortBy);
    }

    @GetMapping("/{id}")
    public CategoryDetailVm getCategoryById(@PathVariable Integer id) {
        return categoryService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "menus")
    public CategoryDetailVm createCategory(@Valid @RequestBody CategoryPostVm campaign) {
        return categoryService.save(campaign);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "menus")
    public CategoryDetailVm updateCategory(@PathVariable Integer id, @Valid @RequestBody CategoryPostVm campaign) {
        return categoryService.update(id, campaign);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "menus")
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.delete(id);
    }
}
