package com.github.ilyadreamix.notie.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.fragment.FragmentNavigator;

import com.github.ilyadreamix.notie.R;
import com.github.ilyadreamix.notie.common.NotieFragment;
import com.github.ilyadreamix.notie.databinding.HomeFragmentBinding;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.elevation.ElevationOverlayProvider;
import com.google.android.material.transition.platform.MaterialContainerTransform;

import java.util.HashMap;

public class HomeFragment extends NotieFragment {

    private HomeFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(new MaterialContainerTransform());
        setSharedElementReturnTransition(new MaterialContainerTransform());
    }

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
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
        setupCreateNoteButton();
        setupSearchBar();
    }

    private void setupCreateNoteButton() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.createNoteButton, (view, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            int screenPadding = getResources().getDimensionPixelSize(R.dimen.screen_padding);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) binding.createNoteButton.getLayoutParams();
            layoutParams.setMargins(0, screenPadding, screenPadding, screenPadding + systemBarsInsets.bottom);
            return insets;
        });

        binding.createNoteButton.setOnClickListener((view) -> {
            // Start shared element transition
            HashMap<View, String> extrasMap = new HashMap<>();
            extrasMap.put(binding.createNoteButton, binding.createNoteButton.getTransitionName());
            FragmentNavigator.Extras extras = new FragmentNavigator.Extras(extrasMap);
            findNavController().navigate(R.id.home_to_note, null, null, extras);
        });

        // TODO: Notes list
        // ExtendedFloatingActionButtonUtility.enableShrinkOnScroll(binding.createNoteButton, binding.scrollView);
    }

    private void setupSearchBar() {
        int surfaceColor = MaterialColors.getColor(binding.searchBar.getRoot(), com.google.android.material.R.attr.colorSurface);
        ElevationOverlayProvider provider = new ElevationOverlayProvider(requireContext());
        int searchBarColor = provider.compositeOverlay(surfaceColor, 16f);
        binding.searchBar.placeholder.setCardBackgroundColor(searchBarColor);
    }
}
