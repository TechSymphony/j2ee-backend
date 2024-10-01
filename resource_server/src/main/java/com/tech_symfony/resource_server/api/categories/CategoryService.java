package com.tech_symfony.resource_server.api.categories;

//import com.tech_symfony.resource_server.api.role.permission.PermissionRepository;
import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryDetailVm;
import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryListVm;
import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryPostVm;
import com.tech_symfony.resource_server.commonlibrary.constants.MessageCode;
import com.tech_symfony.resource_server.commonlibrary.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public interface CategoryService {
    List<CategoryListVm> findAll();

    CategoryDetailVm findById(Integer id);

    CategoryDetailVm save(CategoryPostVm campaign);

    CategoryDetailVm update(Integer id, CategoryPostVm campaign);

    Boolean delete(Integer id);
}

@Service
@RequiredArgsConstructor
class DefaultCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryListVm> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::entityToCategoryListVm)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDetailVm findById(Integer id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::entityToCategoryDetailVm)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));
    }

    @Override
    public CategoryDetailVm save(CategoryPostVm categoryPostVm) {
        Category newCampain = categoryMapper.categoryPostVmToCategory(categoryPostVm);
        Category savedCategory = categoryRepository.save(newCampain);
        return categoryMapper.entityToCategoryDetailVm(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDetailVm update(Integer id, CategoryPostVm categoryPostVm) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));

        Category campaign1 = categoryMapper.updateCategoryFromDto(categoryPostVm, existingCategory);
        Category updatedCategory = categoryRepository.save(
                campaign1
        );
        return categoryMapper.entityToCategoryDetailVm(updatedCategory);
    }

    @Override
    public Boolean delete(Integer id) {
        Category campaign = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));

//        if (campaignRepository.existsByUsersRole(id)) {
//            throw new BadRequestException(MessageCode.RESOURCE_NOT_CONTAIN_CHILD_FOR_DELETION);
//        }
        categoryRepository.delete(campaign);
        return !categoryRepository.existsById(id);
    }
}
