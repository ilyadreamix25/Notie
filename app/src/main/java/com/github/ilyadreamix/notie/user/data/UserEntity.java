package com.github.ilyadreamix.notie.user.data;

import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class UserEntity {

    @SerializedName("id")
    private String id;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("createdAt")
    private long createdAt;

    public UserEntity() {
        this.id = "";
        this.email = "";
        this.name = "";
        this.createdAt = 0L;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public static UserEntity buildFromCredential(SignInCredential credential) {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(credential.getId());
        user.setName(credential.getDisplayName());
        user.setCreatedAt(System.currentTimeMillis());
        return user;
    }
}
