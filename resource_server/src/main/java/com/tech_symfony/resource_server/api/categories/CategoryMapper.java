package com.tech_symfony.resource_server.api.categories;

import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryDetailVm;
import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryListVm;
import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryPostVm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

	CategoryDetailVm entityToCategoryDetailVm (Category category);
	CategoryListVm entityToCategoryListVm(Category category);

	Category updateCategoryFromDto(CategoryPostVm categoryPostVm, @MappingTarget Category category);
	Category categoryPostVmToCategory(CategoryPostVm categoryPostVm);

}