package com.github.ilyadreamix.notie.common.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class NotieResult<T> {

    @Nullable private final T data;
    private final boolean isSuccess;

    private NotieResult(@Nullable T data, boolean isSuccess) {
        this.data = data;
        this.isSuccess = isSuccess;
    }

    public static <T> NotieResult<T> success(@NonNull T result) {
        return new NotieResult<>(result, true);
    }

    public static <T> NotieResult<T> failure() {
        return new NotieResult<>(null, false);
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
