package com.tech_symfony.resource_server.api.categories;

import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryDetailVm;
import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryListVm;
import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryPostVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryListVm> getAllCampaigns() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public CategoryDetailVm getCampaignById(@PathVariable Integer id) {
        return categoryService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDetailVm createCampaign(@Valid @RequestBody CategoryPostVm campaign) {
        return categoryService.save(campaign);
    }

    @PutMapping("/{id}")
    public CategoryDetailVm updateCampaign(@PathVariable Integer id, @Valid @RequestBody CategoryPostVm campaign) {
        return categoryService.update(id, campaign);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCampaign(@PathVariable Integer id) {
        categoryService.delete(id);
    }
}
