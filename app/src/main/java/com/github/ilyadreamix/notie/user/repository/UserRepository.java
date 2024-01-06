package com.github.ilyadreamix.notie.user.repository;

import com.github.ilyadreamix.notie.common.data.Result;
import com.github.ilyadreamix.notie.user.data.UserEntity;
import com.google.android.gms.tasks.Task;

public interface UserRepository {
    Task<Result<UserEntity>> createUser(UserEntity user);
    Task<Result<String>> deleteUser(String id);
    Task<Result<UserEntity>> editUser(UserEntity user);
    Task<Result<UserEntity>> getUserById(String id);
    Task<Result<UserEntity>> getUserByEmail(String email);
}
