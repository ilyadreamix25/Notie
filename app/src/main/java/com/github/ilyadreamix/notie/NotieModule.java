package com.github.ilyadreamix.notie;

import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class NotieModule {
    @Provides
    @Singleton
    public static FirebaseFirestore provideFirestore() {
        return FirebaseFirestore.getInstance();
    }
}
