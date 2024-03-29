package com.github.ilyadreamix.notie.user.usecase;

import com.github.ilyadreamix.notie.common.data.NotieResult;
import com.github.ilyadreamix.notie.user.repository.UserRepository;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

public class DeleteUserUseCase {

    private final UserRepository repository;

    @Inject
    public DeleteUserUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public Task<NotieResult<String>> invoke(String id) {
        return repository.deleteUser(id);
    }
}
