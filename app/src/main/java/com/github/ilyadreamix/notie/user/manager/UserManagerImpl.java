package com.github.ilyadreamix.notie.user.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.github.ilyadreamix.notie.user.data.UserEntity;
import com.google.gson.Gson;

public class UserManagerImpl implements UserManager {

    private static final String SHARED_PREFERENCES_NAME = "UserManager";
    private static final String SHARED_PREFERENCES_KEY = "authenticatedUser";
    private static final String TAG = "UserManagerImpl";

    private final Context context;

    public UserManagerImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean isAuthenticated() {
        try {
            getSharedPreferences().getAll();
            Log.d(TAG, "isAuthenticated: true");
            return true;
        } catch (NullPointerException exception) {
            Log.d(TAG, "isAuthenticated: false", exception);
            return false;
        }
    }

    @Override
    public void setAuthenticatedUser(UserEntity user) {

        Gson gson = new Gson();
        String userJson = gson.toJson(user);

        getSharedPreferences().edit()
            .putString(SHARED_PREFERENCES_KEY, userJson)
            .apply();

        Log.d(TAG, "setAuthenticatedUser: " + user.getId());
    }

    @Override
    public UserEntity getAuthenticatedUser() {
        Gson gson = new Gson();
        String userJson = getSharedPreferences().getString(SHARED_PREFERENCES_KEY, null);
        UserEntity user = gson.fromJson(userJson, UserEntity.class);
        Log.d(TAG, "getAuthenticatedUser: " + user.getId());
        return user;
    }

    @Override
    public void clearAuthenticatedUser() {
        getSharedPreferences().edit()
            .clear()
            .apply();
        Log.d(TAG, "clearAuthenticatedUser: Success");
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
}
