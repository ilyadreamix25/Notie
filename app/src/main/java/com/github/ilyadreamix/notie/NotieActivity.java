package com.github.ilyadreamix.notie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.os.Bundle;

import com.github.ilyadreamix.notie.databinding.NotieActivityBinding;

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
    }

    private void setupEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }
}
