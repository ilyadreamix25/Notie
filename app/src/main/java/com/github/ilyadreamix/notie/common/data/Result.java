package com.github.ilyadreamix.notie.common.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Result<T> {

    @Nullable private final T data;
    private final boolean isSuccess;

    private Result(@Nullable T data, boolean isSuccess) {
        this.data = data;
        this.isSuccess = isSuccess;
    }

    public static <T> Result<T> success(@NonNull T result) {
        return new Result<>(result, true);
    }

    public static <T> Result<T> failure() {
        return new Result<>(null, false);
    }

    @NonNull
    public T requireData() {
        if (!isSuccess || data == null) {
            throw new NullPointerException("result is null");
        }

        return data;
    }

    @NonNull
    public T getData() {
        return Objects.requireNonNull(data);
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
