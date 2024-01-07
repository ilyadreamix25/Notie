package com.github.ilyadreamix.notie.user.signin;

public class UserSignInError extends Throwable {

    private final UserSignInErrorType errorType;

    public UserSignInError(UserSignInErrorType errorType) {
        this.errorType = errorType;
    }

    public UserSignInErrorType getErrorType() {
        return errorType;
    }
}
