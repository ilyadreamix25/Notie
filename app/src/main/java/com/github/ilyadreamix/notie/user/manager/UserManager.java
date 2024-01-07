package com.github.ilyadreamix.notie.user.manager;

import com.github.ilyadreamix.notie.user.data.UserEntity;

public interface UserManager {
    boolean isAuthenticated();
    void setAuthenticatedUser(UserEntity user);
    UserEntity getAuthenticatedUser();
    void clearAuthenticatedUser();
}
