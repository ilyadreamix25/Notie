package com.github.ilyadreamix.notie.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.github.ilyadreamix.notie.R;
import com.github.ilyadreamix.notie.common.NotieFragment;
import com.github.ilyadreamix.notie.common.data.NotieState;
import com.github.ilyadreamix.notie.databinding.SplashFragmentBinding;
import com.github.ilyadreamix.notie.user.data.UserEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashFragment extends NotieFragment {

    private SplashFragmentBinding binding;
    private SplashViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = SplashFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkUserAuthentication();
        setupUserStateListener();
    }

    private void checkUserAuthentication() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser == null) {
            findNavController().navigate(SplashFragmentDirections.splashToSignIn());
        } else {
            String email = firebaseUser.getEmail();
            viewModel.getUser(email);
        }
    }

    private void setupUserStateListener() {
        NotieState.Listener<UserEntity> listener = new NotieState.Listener<UserEntity>() {
            @Override
            public void onSuccess(UserEntity data) {
                onUserStateSuccess();
            }

            @Override
            public void onFailure(Throwable error) {
                onUserStateFailure(error);
            }
        };
        NotieState.listen(viewModel.getUserState(), getViewLifecycleOwner(), listener);
    }

    private void onUserStateSuccess() {
        findNavController().navigate(SplashFragmentDirections.splashToHome());
    }

    private void onUserStateFailure(Throwable error) {
        if (error instanceof SplashError) {

            SplashError splashError = (SplashError) error;
            SplashErrorType errorType = splashError.getErrorType();

            if (errorType == SplashErrorType.CannotFindUser) {
                showToast(R.string.splash_cannot_find_user);
            } else {
                showToast(R.string.splash_cannot_connects_server);
            }
        } else {
            showToast(R.string.unknown_error);
        }

        viewModel.getUserManager().clearAuthenticatedUser();
        findNavController().navigate(SplashFragmentDirections.splashToSignIn());
    }
}
