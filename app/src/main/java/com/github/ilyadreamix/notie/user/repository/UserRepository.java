package com.github.ilyadreamix.notie.user.repository;

import com.github.ilyadreamix.notie.common.data.NotieResult;
import com.github.ilyadreamix.notie.user.data.UserEntity;
import com.google.android.gms.tasks.Task;

public interface UserRepository {
    Task<NotieResult<UserEntity>> createUser(UserEntity user);
    Task<NotieResult<String>> deleteUser(String id);
    Task<NotieResult<UserEntity>> editUser(UserEntity user);
    Task<NotieResult<UserEntity>> getUserById(String id);
    Task<NotieResult<UserEntity>> getUserByEmail(String email);
}
