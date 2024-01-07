package com.github.ilyadreamix.notie.user.signin;

import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.github.ilyadreamix.notie.R;
import com.github.ilyadreamix.notie.common.NotieFragment;
import com.github.ilyadreamix.notie.common.data.NotieState;
import com.github.ilyadreamix.notie.databinding.UserSignInFragmentBinding;
import com.github.ilyadreamix.notie.user.data.UserEntity;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserSignInFragment extends NotieFragment {

    private static final String TAG = "UserSignInFragment";

    private UserSignInFragmentBinding binding;
    private UserSignInViewModel viewModel;

    private SignInClient signInClient;

    private final ActivityResultLauncher<IntentSenderRequest> signInIntentLauncher = registerForActivityResult(
        new ActivityResultContracts.StartIntentSenderForResult(),
        this::onGoogleSignInIntentResult
    );

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = new ViewModelProvider(this).get(UserSignInViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = UserSignInFragmentBinding.inflate(inflater, container, false);
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
        setupFooter();
        setupSignInButton();
        setupSignInStateListener();
    }

    private void setupFooter() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.footer, (view, insets) -> {
            int screenPadding = getResources().getDimensionPixelSize(R.dimen.screen_padding);
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.footer.setPadding(screenPadding, 0, screenPadding, screenPadding + systemBarsInsets.bottom);
            return insets;
        });
    }

    private void setupSignInButton() {
        binding.signInButton.setOnClickListener((view) -> {
            startLoading();
            startGoogleSignIn();
        });
    }

    private void startGoogleSignIn() {

        signInClient = Identity.getSignInClient(requireContext());

        BeginSignInRequest.GoogleIdTokenRequestOptions googleIdTokenRequestOptions =
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(getString(R.string.google_sign_in_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build();

        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(googleIdTokenRequestOptions)
            .build();

        signInClient.beginSignIn(signInRequest)
            .addOnSuccessListener((task) -> {
                try {
                    IntentSender intentSender = task.getPendingIntent().getIntentSender();
                    IntentSenderRequest intentSenderRequest = new IntentSenderRequest(intentSender, null, 0, 0);
                    signInIntentLauncher.launch(intentSenderRequest);
                } catch (Throwable exception) {
                    Log.e(TAG, "startGoogleSignIn.onSuccessListener: " + exception.getMessage());
                    onGoogleSignInFailure();
                }
            })
            .addOnFailureListener((exception) -> {
                Log.e(TAG, "startGoogleSignIn.onFailureListener: " + exception.getMessage());
                onGoogleSignInFailure();
            });
    }

    private void onGoogleSignInIntentResult(ActivityResult result) {
        try {
            SignInCredential credential = signInClient.getSignInCredentialFromIntent(result.getData());
            onGoogleSignInSuccess(credential);
        } catch (ApiException exception) {
            Log.e(TAG, "onGoogleSignInIntentResult: " + exception.getMessage());
            onGoogleSignInFailure();
        }
    }

    private void onGoogleSignInSuccess(SignInCredential credential) {

        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(credential.getGoogleIdToken(), null);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithCredential(firebaseCredential)
            .addOnSuccessListener((task) -> onFirebaseSignInSuccess(credential))
            .addOnFailureListener((exception) -> {
                Log.e(TAG, "onGoogleSignInSuccess.signInWithCredential.onFailureListener: " + exception.getMessage());
                onGoogleSignInFailure();
            });
    }

    private void onFirebaseSignInSuccess(SignInCredential credential) {
        UserEntity user = UserEntity.buildFromCredential(credential);
        viewModel.getOrCreateUser(user);
    }

    private void onGoogleSignInFailure() {
        stopLoading();
        showToast(R.string.sign_in_google_error);
    }

    private void setupSignInStateListener() {
        NotieState.Listener<UserEntity> listener = new NotieState.Listener<UserEntity>() {
            @Override
            public void onSuccess(UserEntity data) {
                onSignInSuccess(data);
            }

            @Override
            public void onFailure(Throwable error) {
                onSignInFailure(error);
            }
        };
        NotieState.listen(viewModel.getUserState(), getViewLifecycleOwner(), listener);
    }

    private void onSignInSuccess(UserEntity user) {

        stopLoading();
        viewModel.getUserManager().setAuthenticatedUser(user);

        // Start shared element transition
        HashMap<View, String> extrasMap = new HashMap<>();
        extrasMap.put(binding.signInButton, binding.signInButton.getTransitionName());
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras(extrasMap);
        findNavController().navigate(R.id.user_sign_in_to_home, null, null, extras);
    }

    private void onSignInFailure(Throwable error) {

        stopLoading();

        if (error instanceof UserSignInError) {

            UserSignInError signInError = ((UserSignInError) error);
            UserSignInErrorType signInErrorType = signInError.getErrorType();

            if (signInErrorType == UserSignInErrorType.CannotCreateUser) {
                showToast(R.string.sign_in_cannot_create_user);
            } else if (signInErrorType == UserSignInErrorType.CannotCheckUser) {
                showToast(R.string.sign_in_check_user_error);
            }
        } else {
            showToast(R.string.unknown_error);
        }
    }

    private void startLoading() {
        showLoadingDialog();
        binding.signInButton.setEnabled(false);
    }

    private void stopLoading() {
        hideLoadingDialog();
        binding.signInButton.setEnabled(true);
    }
}
