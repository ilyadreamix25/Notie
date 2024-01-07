package com.github.ilyadreamix.notie.common;

import androidx.annotation.NonNull;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

public class NotieNavHostFragment extends NavHostFragment {
    @Override
    protected void onCreateNavHostController(@NonNull NavHostController navHostController) {
        super.onCreateNavHostController(navHostController);
        NotieFragmentNavigator navigator = new NotieFragmentNavigator(requireContext(), getChildFragmentManager(), getId());
        navHostController.getNavigatorProvider().addNavigator(navigator);
    }
}
