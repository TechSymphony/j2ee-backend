package com.tech_symfony.resource_server.commonlibrary.constants;

public final class MessageCode {
    // General CRUD messages
    public static final String RESOURCE_CREATED = "RESOURCE_CREATED";
    public static final String RESOURCE_ALREADY_EXISTED_MESSAGE = "RESOURCE_ALREADY_EXISTED";
    public static final String RESOURCE_CREATION_FAILED = "RESOURCE_CREATION_FAILED";
    public static final String RESOURCE_FOUND = "RESOURCE_FOUND";
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String RESOURCE_UPDATED = "RESOURCE_UPDATED";
    public static final String RESOURCE_NOT_FOUND_FOR_UPDATE = "RESOURCE_NOT_FOUND_FOR_UPDATE";
    public static final String RESOURCE_UPDATE_FAILED = "RESOURCE_UPDATE_FAILED";
    public static final String RESOURCE_DELETED = "RESOURCE_DELETED";
    public static final String RESOURCE_NOT_FOUND_FOR_DELETION = "RESOURCE_NOT_FOUND_FOR_DELETION";
    public static final String RESOURCE_NOT_CONTAIN_CHILD_FOR_DELETION = "RESOURCE_NOT_CONTAIN_CHILD_FOR_DELETION";

    public static final String RESOURCE_DELETION_FAILED = "RESOURCE_DELETION_FAILED";

    // Specific messages ( for your reference )
    public static final String NAME_ALREADY_EXITED = "NAME_ALREADY_EXITED";
    public static final String NOT_FOUND_ITEM = "NOT_FOUND_PRODUCT";
    public static final String NON_EXISTING_CART_ITEM = "NON_EXISTING_CART_ITEM";

    public static final String NOT_EXISTING_ITEM_IN_CART = "NOT_EXISTING_ITEM_IN_CART";
    public static final String NOT_EXISTING_PRODUCT_IN_CART = "NOT_EXISTING_PRODUCT_IN_CART";
    public static final String USER_WITH_EMAIL_NOT_FOUND = "USER_WITH_EMAIL_NOT_FOUND";
    public static final String WRONG_EMAIL_FORMAT = "WRONG_EMAIL_FORMAT";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String USER_ADDRESS_NOT_FOUND = "USER_ADDRESS_NOT_FOUND";
    public static final String PRODUCT_NOT_FOUND = "PRODUCT_NOT_FOUND";
    public static final String WAREHOUSE_NOT_FOUND = "WAREHOUSE_NOT_FOUND";
    public static final String INVALID_ADJUSTED_QUANTITY = "INVALID_ADJUSTED_QUANTITY";
    public static final String STATE_OR_PROVINCE_NOT_FOUND = "STATE_OR_PROVINCE_NOT_FOUND";
    public static final String ADDRESS_NOT_FOUND = "ADDRESS_NOT_FOUND";
    public static final String COUNTRY_NOT_FOUND = "COUNTRY_NOT_FOUND";
    public static final String CODE_ALREADY_EXISTED = "CODE_ALREADY_EXISTED";
    public static final String ORDER_NOT_FOUND = "ORDER_NOT_FOUND";
    public static final String CHECKOUT_NOT_FOUND = "CHECKOUT_NOT_FOUND";
    public static final String SIGN_IN_REQUIRED = "SIGN_IN_REQUIRED";
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String PAYMENT_PROVIDER_NOT_FOUND = "PAYMENT_PROVIDER_NOT_FOUND";
    public static final String PAYMENT_FAIL_MESSAGE = "PAYMENT_FAIL_MESSAGE";
    public static final String PAYMENT_SUCCESS_MESSAGE = "PAYMENT_SUCCESS_MESSAGE";
    public static final String CATEGORY_NOT_FOUND = "CATEGORY_NOT_FOUND";
    public static final String BRAND_NOT_FOUND = "BRAND_NOT_FOUND";
    public static final String PARENT_CATEGORY_NOT_FOUND = "PARENT_CATEGORY_NOT_FOUND";
    public static final String MAKE_SURE_CATEGORY_DO_NOT_CONTAIN_CHILDREN = "MAKE_SURE_CATEGORY_DO_NOT_CONTAIN_CHILDREN";
    public static final String MAKE_SURE_CATEGORY_DO_NOT_CONTAIN_PRODUCT = "MAKE_SURE_CATEGORY_DO_NOT_CONTAIN_PRODUCT";
    public static final String PARENT_CATEGORY_CANNOT_BE_ITSELF = "PARENT_CATEGORY_CANNOT_BE_ITSELF";
    public static final String THIS_PROD_ATTRI_NOT_EXIST_IN_ANY_PROD_ATTRI = "THIS_PROD_ATTRI_NOT_EXIST_IN_ANY_PROD_ATTRI";
    public static final String PRODUCT_ATTRIBUTE_VALUE_IS_NOT_FOUND = "PRODUCT_ATTRIBUTE_VALUE_IS_NOT_FOUND";
    public static final String PRODUCT_ATTRIBUTE_IS_NOT_FOUND = "PRODUCT_ATTRIBUTE_IS_NOT_FOUND";
    public static final String PRODUCT_OPTION_IS_NOT_FOUND = "PRODUCT_OPTION_IS_NOT_FOUND";
    public static final String SLUG_IS_DUPLICATED = "SLUG_IS_DUPLICATED";
    public static final String MAKE_SURE_BRAND_DONT_CONTAINS_ANY_PRODUCT = "MAKE_SURE_BRAND_DONT_CONTAINS_ANY_PRODUCT";
    public static final String SLUG_ALREADY_EXISTED_OR_DUPLICATED = "SLUG_ALREADY_EXISTED_OR_DUPLICATED";
    public static final String SKU_ALREADY_EXISTED_OR_DUPLICATED = "SKU_ALREADY_EXISTED_OR_DUPLICATED";
    public static final String GTIN_ALREADY_EXISTED_OR_DUPLICATED = "GTIN_ALREADY_EXISTED_OR_DUPLICATED";
    public static final String RATING_NOT_FOUND = "RATING_NOT_FOUND";
    public static final String CUSTOMER_NOT_FOUND = "CUSTOMER_NOT_FOUND";
    public static final String RESOURCE_ALREADY_EXISTED = "RESOURCE_ALREADY_EXISTED";
    public static final String ACCESS_DENIED = "ACCESS_DENIED";
    public static final String CURRENT_PASSWORD_NOT_CORRECT = "CURRENT_PASSWORD_NOT_CORRECT";

   private MessageCode() {
        //Add constructor
    }
}
