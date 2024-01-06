package com.github.ilyadreamix.notie.user;

import com.github.ilyadreamix.notie.user.repository.UserRepository;
import com.github.ilyadreamix.notie.user.repository.UserRepositoryImpl;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class UserModule {
    @Provides
    @Singleton
    public UserRepository provideUserRepository(FirebaseFirestore firestore) {
        return new UserRepositoryImpl(firestore);
    }
}
