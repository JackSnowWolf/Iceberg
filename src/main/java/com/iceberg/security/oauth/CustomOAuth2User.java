//package com.iceberg.security.oauth;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.Collection;
//import java.util.Map;
//
//public class CustomOAuth2User implements OAuth2User {
//
//    public CustomOAuth2User(OAuth2User oAuth2User) {
//        this.oAuth2User = oAuth2User;
//    }
//
//    private OAuth2User oAuth2User;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return oAuth2User.getAuthorities();
//    }
//
//    @Override
//    public Map<String, Object> getAttributes() {
//        return oAuth2User.getAttributes();
//    }
//
//    @Override
//    public String getName() {
//        return oAuth2User.getName();
//    }
//
//    public String getFullName() {
//        return oAuth2User.getName();
//    }
//}
