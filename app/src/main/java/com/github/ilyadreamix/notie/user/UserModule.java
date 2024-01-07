package com.github.ilyadreamix.notie.user;

import android.content.Context;

import com.github.ilyadreamix.notie.user.manager.UserManager;
import com.github.ilyadreamix.notie.user.manager.UserManagerImpl;
import com.github.ilyadreamix.notie.user.repository.UserRepository;
import com.github.ilyadreamix.notie.user.repository.UserRepositoryImpl;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class UserModule {

    @Provides
    @Singleton
    public UserRepository provideUserRepository(FirebaseFirestore firestore) {
        return new UserRepositoryImpl(firestore);
    }

    @Provides
    @Singleton
    public UserManager provideUserManager(@ApplicationContext Context context) {
        return new UserManagerImpl(context);
    }
}
