package com.github.ilyadreamix.notie.splash;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.ilyadreamix.notie.common.data.NotieState;
import com.github.ilyadreamix.notie.user.data.UserEntity;
import com.github.ilyadreamix.notie.user.manager.UserManager;
import com.github.ilyadreamix.notie.user.usecase.GetUserByEmailUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SplashViewModel extends ViewModel {

    private static final String TAG = "UserSignInViewModel";

    private final GetUserByEmailUseCase getUserByEmailUseCase;

    private final UserManager userManager;

    private final MutableLiveData<NotieState<UserEntity>> userState = new MutableLiveData<>(new NotieState.Idle<>());

    @Inject
    public SplashViewModel(
        GetUserByEmailUseCase getUserByEmailUseCase,
        UserManager userManager
    ) {
        this.getUserByEmailUseCase = getUserByEmailUseCase;
        this.userManager = userManager;
    }

    public void getUser(String email) {
        getUserByEmailUseCase.invoke(email)
            .addOnSuccessListener((result) -> {
                if (result.isSuccess()) {
                    UserEntity user = result.requireData();
                    userState.postValue(new NotieState.Success<>(user));
                } else {
                    Log.e(TAG, "getUser.onFailure: Cannot find user");
                    SplashError error = new SplashError(SplashErrorType.CannotFindUser);
                    userState.postValue(new NotieState.Failure<>(error));
                }
            })
            .addOnFailureListener((exception) -> {
                Log.e(TAG, "getUser.onFailure: " + exception.getMessage(), exception);
                SplashError error = new SplashError(SplashErrorType.CannotConnectToServer);
                userState.postValue(new NotieState.Failure<>(error));
            });
    }

    public LiveData<NotieState<UserEntity>> getUserState() {
        return userState;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
