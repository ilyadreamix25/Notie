package com.github.ilyadreamix.notie.user.repository;

import com.github.ilyadreamix.notie.common.data.NotieResult;
import com.github.ilyadreamix.notie.user.data.UserEntity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class UserRepositoryImpl implements UserRepository {

    private static final String COLLECTION = "users";

    private final FirebaseFirestore firestore;

    public UserRepositoryImpl(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Task<NotieResult<UserEntity>> createUser(UserEntity user) {
        return firestore.collection(COLLECTION)
            .document(user.getId())
            .set(user)
            .continueWith((continuation) -> {
                if (continuation.isSuccessful()) {
                    return NotieResult.success(user);
                } else {
                    return NotieResult.failure();
                }
            });
    }

    @Override
    public Task<NotieResult<String>> deleteUser(String id) {
        return firestore.collection(COLLECTION)
            .document(id)
            .delete()
            .continueWith((continuation) -> {
                if (continuation.isSuccessful()) {
                    return NotieResult.success(id);
                } else {
                    return NotieResult.failure();
                }
            });
    }

    @Override
    public Task<NotieResult<UserEntity>> editUser(UserEntity user) {
        return firestore.collection(COLLECTION)
            .document(user.getId())
            .set(user)
            .continueWith((continuation) -> {
                if (continuation.isSuccessful()) {
                    return NotieResult.success(user);
                } else {
                    return NotieResult.failure();
                }
            });
    }

    @Override
    public Task<NotieResult<UserEntity>> getUserById(String id) {
        return firestore.collection(COLLECTION)
            .document(id)
            .get()
            .continueWith((continuation) -> {
                if (!continuation.isSuccessful() || !continuation.getResult().exists()) {
                    return NotieResult.failure();
                }

                UserEntity user = continuation.getResult().toObject(UserEntity.class);
                return NotieResult.success(Objects.requireNonNull(user));
            });
    }

    @Override
    public Task<NotieResult<UserEntity>> getUserByEmail(String email) {
        return firestore.collection(COLLECTION)
            .whereEqualTo("email", email)
            .get()
            .continueWith((continuation) -> {
                if (!continuation.isSuccessful() || continuation.getResult().getDocuments().isEmpty()) {
                    return NotieResult.failure();
                }

                UserEntity user = continuation.getResult()
                    .getDocuments().get(0)
                    .toObject(UserEntity.class);
                return NotieResult.success(Objects.requireNonNull(user));
            });
    }
}
