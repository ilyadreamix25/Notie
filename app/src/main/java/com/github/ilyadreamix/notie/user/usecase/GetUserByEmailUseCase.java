package com.github.ilyadreamix.notie.user.usecase;

import com.github.ilyadreamix.notie.common.data.NotieResult;
import com.github.ilyadreamix.notie.user.data.UserEntity;
import com.github.ilyadreamix.notie.user.repository.UserRepository;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

public class GetUserByEmailUseCase {

    private final UserRepository repository;

    @Inject
    public GetUserByEmailUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public Task<NotieResult<UserEntity>> invoke(String email) {
        return repository.getUserByEmail(email);
    }
}
