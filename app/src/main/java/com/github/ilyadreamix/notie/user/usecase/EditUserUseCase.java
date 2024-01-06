package com.github.ilyadreamix.notie.user.usecase;

import com.github.ilyadreamix.notie.common.data.Result;
import com.github.ilyadreamix.notie.user.data.UserEntity;
import com.github.ilyadreamix.notie.user.repository.UserRepository;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

public class EditUserUseCase {

    private final UserRepository repository;

    @Inject
    public EditUserUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public Task<Result<UserEntity>> invoke(UserEntity user) {
        return repository.editUser(user);
    }
}
