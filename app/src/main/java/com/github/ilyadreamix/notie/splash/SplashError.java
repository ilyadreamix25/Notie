package com.github.ilyadreamix.notie.splash;

public class SplashError extends Throwable {

    private final SplashErrorType errorType;

    public SplashError(SplashErrorType errorType) {
        this.errorType = errorType;
    }

    public SplashErrorType getErrorType() {
        return errorType;
    }
}
