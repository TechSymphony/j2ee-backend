package com.tech_symfony.resource_server.api.categories;

import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryDetailVm;
import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryListVm;
import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryMenuVm;
import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryPostVm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

	CategoryDetailVm entityToCategoryDetailVm (Category category);
	CategoryListVm entityToCategoryListVm(Category category);
	CategoryMenuVm entityToCategoryMenuVm(Category category);


	@Mapping(source = "parent", target = "parent")
	Category categoryPostVmToCategory(CategoryPostVm categoryPostVm);

	@Mapping(source = "parent", target = "parent")
	Category updateCategoryFromDto(CategoryPostVm categoryPostVm, @MappingTarget Category category);

}
