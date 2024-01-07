package com.github.ilyadreamix.notie.user.signin;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.ilyadreamix.notie.common.data.NotieState;
import com.github.ilyadreamix.notie.user.data.UserEntity;
import com.github.ilyadreamix.notie.user.manager.UserManager;
import com.github.ilyadreamix.notie.user.usecase.CreateUserUseCase;
import com.github.ilyadreamix.notie.user.usecase.GetUserByEmailUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserSignInViewModel extends ViewModel {

    private static final String TAG = "UserSignInViewModel";

    private final CreateUserUseCase createUserUseCase;
    private final GetUserByEmailUseCase getUserByEmailUseCase;
    private final UserManager userManager;

    private final MutableLiveData<NotieState<UserEntity>> userState = new MutableLiveData<>(new NotieState.Idle<>());

    @Inject
    public UserSignInViewModel(
        CreateUserUseCase createUserUseCase,
        GetUserByEmailUseCase getUserByEmailUseCase,
        UserManager userManager
    ) {
        this.createUserUseCase = createUserUseCase;
        this.getUserByEmailUseCase = getUserByEmailUseCase;
        this.userManager = userManager;
    }

    public LiveData<NotieState<UserEntity>> getUserState() {
        return userState;
    }

    public void getOrCreateUser(UserEntity user) {
        getUserByEmailUseCase.invoke(user.getEmail())
            .addOnSuccessListener((getUserResult) -> {
                if (getUserResult.isSuccess()) {
                    // User exists, return it now
                    UserEntity userFromDb = getUserResult.requireData();
                    userState.postValue(new NotieState.Success<>(userFromDb));
                } else {
                    // User does not exist, create the new one
                    createUserUseCase.invoke(user)
                        .addOnSuccessListener((createUserResult) -> {
                            if (createUserResult.isSuccess()) {
                                UserEntity newUser = createUserResult.requireData();
                                userState.postValue(new NotieState.Success<>(newUser));
                            } else {
                                Log.e(TAG, "getOrCreateUser.create.onFailure: Cannot create user");
                                UserSignInError signInError = new UserSignInError(UserSignInErrorType.CannotCreateUser);
                                userState.postValue(new NotieState.Failure<>(signInError));
                            }
                        })
                        .addOnFailureListener((error) -> {
                            Log.e(TAG, "getOrCreateUser.create.onFailure: " + error.getMessage());
                            UserSignInError signInError = new UserSignInError(UserSignInErrorType.CannotCreateUser);
                            userState.postValue(new NotieState.Failure<>(signInError));
                        });
                }
            })
            .addOnFailureListener((error) -> {
                Log.e(TAG, "getOrCreateUser.get.onFailure: " + error.getMessage());
                UserSignInError signInError = new UserSignInError(UserSignInErrorType.CannotCheckUser);
                userState.postValue(new NotieState.Failure<>(signInError));
            });
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
