package com.github.ilyadreamix.notie.common.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public abstract class State<T> {

    @Nullable protected T data;
    @Nullable protected Throwable error;

    private State(@Nullable T data, @Nullable Throwable error) {
        this.data = data;
        this.error = error;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    public static class Idle<T> extends State<T> {
        public Idle() {
            super(null, null);
        }
    }

    public static class Loading<T> extends State<T> {
        public Loading() {
            super(null, null);
        }
    }

    public static class Success<T> extends State<T> {
        public Success(@NonNull T data) {
            super(data, null);
        }

        @NonNull
        @Override
        public T getData() {
            return Objects.requireNonNull(super.getData());
        }
    }

    public static class Failure<T> extends State<T> {
        public Failure(@NonNull Throwable error) {
            super(null, error);
        }

        @NonNull
        @Override
        public Throwable getError() {
            return Objects.requireNonNull(super.getError());
        }
    }
}
