package com.github.ilyadreamix.notie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.graphics.Color;
import android.os.Bundle;

import com.github.ilyadreamix.notie.databinding.NotieActivityBinding;
import com.google.android.material.color.MaterialColors;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NotieActivity extends AppCompatActivity {

    private NotieActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = NotieActivityBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        setupEdgeToEdge();
        setupNavigationListener();
    }

    private void setupEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }

    private void setupNavigationListener() {
        getNavController().addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.notie_home) {
                setupHomeStatusBar();
            } else {
                restoreStatusBar();
            }
        });
    }

    private void setupHomeStatusBar() {
        int surfaceColor = MaterialColors.getColor(binding.getRoot(), com.google.android.material.R.attr.colorSurface);
        getWindow().setStatusBarColor(surfaceColor);
    }

    private void restoreStatusBar() {
        if (getWindow().getStatusBarColor() != Color.TRANSPARENT) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private NavController getNavController() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.notie_nav_host_fragment);
        return Objects.requireNonNull(navHostFragment).getNavController();
    }

    @Override
    public void onBackPressed() {
        getNavController().popBackStack();
    }
}
