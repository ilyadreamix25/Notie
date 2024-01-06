package com.github.ilyadreamix.notie.user.signin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.ilyadreamix.notie.common.data.Result;
import com.github.ilyadreamix.notie.common.data.State;
import com.github.ilyadreamix.notie.user.data.UserEntity;
import com.github.ilyadreamix.notie.user.usecase.CreateUserUseCase;
import com.github.ilyadreamix.notie.user.usecase.GetUserByEmailUseCase;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignInViewModel extends ViewModel {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserByEmailUseCase getUserByEmailUseCase;

    private final MutableLiveData<State<UserEntity>> _userState = new MutableLiveData<>(new State.Idle<>());
    public final LiveData<State<UserEntity>> userState = _userState;

    @Inject
    public SignInViewModel(
        CreateUserUseCase createUserUseCase,
        GetUserByEmailUseCase getUserByEmailUseCase
    ) {
        this.createUserUseCase = createUserUseCase;
        this.getUserByEmailUseCase = getUserByEmailUseCase;
    }

    public void createUser(UserEntity user) {
        getUserByEmailUseCase.invoke(user.getEmail())
            .addOnCompleteListener((getUserTask) -> {
                if (getUserTask.getResult().isSuccess()) {
                    SignInError error = new SignInError(SignInErrorType.UserAlreadyExists);
                    _userState.postValue(new State.Failure<>(error));
                    return;
                }

                createUserUseCase.invoke(user)
                    .addOnCompleteListener((createUserTask) -> {
                        if (!createUserTask.getResult().isSuccess()) {
                            SignInError error = new SignInError(SignInErrorType.CannotCreateUser);
                            _userState.postValue(new State.Failure<>(error));
                            return;
                        }

                        _userState.postValue(new State.Success<>(createUserTask.getResult().requireData()));
                    });
            });
    }
}
