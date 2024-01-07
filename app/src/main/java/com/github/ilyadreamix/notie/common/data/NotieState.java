package com.github.ilyadreamix.notie.common.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.Objects;

public abstract class NotieState<T> {

    @Nullable protected T data;
    @Nullable protected Throwable error;

    private NotieState(@Nullable T data, @Nullable Throwable error) {
        this.data = data;
        this.error = error;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @NonNull
    public T requireData() { return Objects.requireNonNull(data); }

    @Nullable
    public Throwable getError() {
        return error;
    }

    @NonNull
    public Throwable requireError() { return Objects.requireNonNull(error); }

    public static class Idle<T> extends NotieState<T> {
        public Idle() {
            super(null, null);
        }
    }

    public static class Loading<T> extends NotieState<T> {
        public Loading() {
            super(null, null);
        }
    }

    public static class Success<T> extends NotieState<T> {
        public Success(@NonNull T data) {
            super(data, null);
        }

        @NonNull
        @Override
        public T getData() {
            return Objects.requireNonNull(super.getData());
        }
    }

    public static class Failure<T> extends NotieState<T> {
        public Failure(@NonNull Throwable error) {
            super(null, error);
        }

        @NonNull
        @Override
        public Throwable getError() {
            return Objects.requireNonNull(super.getError());
        }
    }

    public interface Listener<T> {
        default void onIdle() {}
        default void onLoading() {}
        default void onSuccess(T data) {}
        default void onFailure(Throwable error) {}
    }

    public static <T> void listen(
        LiveData<NotieState<T>> liveData,
        LifecycleOwner lifecycleOwner,
        Listener<T> listener
    ) {
        liveData.observe(lifecycleOwner, (state) -> {
            if (state instanceof Idle) {
                listener.onIdle();
            } else if (state instanceof Loading) {
                listener.onLoading();
            } else if (state instanceof Success) {
                T data = state.requireData();
                listener.onSuccess(data);
            } else if (state instanceof Failure) {
                Throwable error = state.requireError();
                listener.onFailure(error);
            }
        });
    }
}
