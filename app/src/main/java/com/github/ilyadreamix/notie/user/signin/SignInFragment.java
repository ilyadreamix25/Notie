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

import com.github.ilyadreamix.notie.R;
import com.github.ilyadreamix.notie.common.NotieFragment;
import com.github.ilyadreamix.notie.databinding.SignInFragmentBinding;
import com.github.ilyadreamix.notie.user.data.UserEntity;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignInFragment extends NotieFragment {

    private static final String TAG = "SignInFragment";

    private SignInFragmentBinding binding;
    private SignInViewModel viewModel;

    private SignInClient signInClient;

    private final ActivityResultLauncher<IntentSenderRequest> signInIntentLauncher = registerForActivityResult(
        new ActivityResultContracts.StartIntentSenderForResult(),
        this::onGoogleSignInIntentResult
    );

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = new ViewModelProvider(this).get(SignInViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = SignInFragmentBinding.inflate(inflater, container, false);
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
        setupSignInButton();
    }

    private void setupSignInButton() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.footer, (view, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) binding.footer.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, systemBarsInsets.bottom);
            return insets;
        });
        binding.signInButton.setOnClickListener((view) -> onSignInButtonClicked());
    }

    private void onSignInButtonClicked() {
        showLoadingDialog();
        startGoogleSignIn();
    }

    private void startGoogleSignIn() {
        signInClient = Identity.getSignInClient(requireContext());
        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(getString(R.string.google_sign_in_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build())
            .build();

        signInClient.beginSignIn(signInRequest)
            .addOnSuccessListener((task) -> {
                try {
                    IntentSender intentSender = task.getPendingIntent().getIntentSender();
                    IntentSenderRequest intentSenderRequest = new IntentSenderRequest(intentSender, null, 0, 0);
                    signInIntentLauncher.launch(intentSenderRequest);
                } catch (Throwable exception) {
                    Log.e(TAG, "startGoogleSignIn.onSuccessListener: " + exception.getMessage());
                    onGoogleSignInError();
                }
            })
            .addOnFailureListener((exception) -> {
                Log.e(TAG, "startGoogleSignIn.onFailureListener: " + exception.getMessage());
                onGoogleSignInError();
            });
    }

    private void onGoogleSignInIntentResult(ActivityResult result) {
        try {
            SignInCredential credential = signInClient.getSignInCredentialFromIntent(result.getData());
            onGoogleSignInSuccess(credential);
        } catch (ApiException exception) {
            Log.e(TAG, "onGoogleSignInIntentResult: " + exception.getMessage());
            onGoogleSignInError();
        }
    }

    private void onGoogleSignInSuccess(SignInCredential credential) {

        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(credential.getGoogleIdToken(), null);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        
        // TODO: Move this logic to ViewModel, finish authentication flow

        auth.signInWithCredential(firebaseCredential)
            .addOnSuccessListener((task) -> {
                hideLoadingDialog();
                showToast(R.string.ok);
            })
            .addOnFailureListener((exception) -> {
                Log.e(TAG, "onGoogleSignInSuccess.signInWithCredential.onFailureListener: " + exception.getMessage());
                onGoogleSignInError();
            });
    }

    private void onGoogleSignInError() {
        hideLoadingDialog();
        showToast(R.string.sign_in_google_error);
    }

    private void onSignInError(Throwable error) {
        hideLoadingDialog();
    }
}
