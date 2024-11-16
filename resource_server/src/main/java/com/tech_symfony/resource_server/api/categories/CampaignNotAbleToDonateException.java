package com.tech_symfony.resource_server.api.categories;

import com.tech_symfony.resource_server.commonlibrary.exception.AccessDeniedException;


public class CampaignNotAbleToDonateException extends AccessDeniedException {

    public CampaignNotAbleToDonateException(String message) {
        super(message);
    }

    public CampaignNotAbleToDonateException() {
        super("Chiến dịch không còn tiếp nhận quyên góp");
    }
}
