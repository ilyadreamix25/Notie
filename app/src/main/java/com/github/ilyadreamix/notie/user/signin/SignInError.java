package com.github.ilyadreamix.notie.user.signin;

public class SignInError extends Throwable {

    private final SignInErrorType errorType;

    public SignInError(SignInErrorType errorType) {
        this.errorType = errorType;
    }

    public SignInErrorType getErrorType() {
        return errorType;
    }
}
