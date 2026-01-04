package com.meteorfish.whosnext.application.member;

import java.util.Map;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getNickname();
    Map<String, Object> getAttributes();
}
