package com.github.ilyadreamix.notie.common;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;

import com.github.ilyadreamix.notie.R;

import java.util.List;

@Navigator.Name("fragment")
public class NotieFragmentNavigator extends FragmentNavigator {
    public NotieFragmentNavigator(
        @NonNull Context context,
        @NonNull FragmentManager fragmentManager,
        int containerId
    ) {
        super(context, fragmentManager, containerId);
    }

    @Override
    public void navigate(
        @NonNull List<NavBackStackEntry> entries,
        @Nullable NavOptions navOptions,
        @Nullable Navigator.Extras navigatorExtras
    ) {
        NavOptions finalOptions;
        if (navigatorExtras != null) {
            // Navigator extras are provided
            // Should use transitions instead
            finalOptions = navOptions;
        } else {
            finalOptions = this.replaceNavOptionsAnimations(navOptions);
        }
        super.navigate(entries, finalOptions, navigatorExtras);
    }

    private NavOptions replaceNavOptionsAnimations(@Nullable NavOptions navOptions) {
        if (navOptions == null) {
            return this.getDefaultNavOptions();
        } else {
            return this.copyNavOptionsWithAnimations(navOptions);
        }
    }

    private NavOptions copyNavOptionsWithAnimations(NavOptions navOptions) {
        NavOptions.Builder builder = new NavOptions.Builder()
            .setLaunchSingleTop(navOptions.shouldLaunchSingleTop())
            .setPopUpTo(navOptions.getPopUpToId(), navOptions.isPopUpToInclusive());

        if (navOptions.getEnterAnim() == this.getEmptyNavOptions().getEnterAnim()) {
            builder.setEnterAnim(this.getDefaultNavOptions().getEnterAnim());
        } else {
            builder.setEnterAnim(navOptions.getEnterAnim());
        }

        if (navOptions.getExitAnim() == this.getEmptyNavOptions().getExitAnim()) {
            builder.setExitAnim(this.getDefaultNavOptions().getExitAnim());
        } else {
            builder.setExitAnim(navOptions.getExitAnim());
        }

        if (navOptions.getPopEnterAnim() == this.getEmptyNavOptions().getPopEnterAnim()) {
            builder.setPopEnterAnim(this.getDefaultNavOptions().getPopEnterAnim());
        } else {
            builder.setPopEnterAnim(navOptions.getPopEnterAnim());
        }

        if (navOptions.getPopExitAnim() == this.getEmptyNavOptions().getPopExitAnim()) {
            builder.setPopExitAnim(this.getDefaultNavOptions().getPopExitAnim());
        } else {
            builder.setPopExitAnim(navOptions.getPopExitAnim());
        }

        return builder.build();
    }

    private NavOptions getEmptyNavOptions() {
        return new NavOptions.Builder().build();
    }

    private NavOptions getDefaultNavOptions() {
        return new NavOptions.Builder()
            .setEnterAnim(R.anim.nav_slide_in_right)
            .setExitAnim(R.anim.nav_slide_out_left)
            .setPopEnterAnim(R.anim.nav_slide_in_left)
            .setPopExitAnim(R.anim.nav_slide_out_right)
            .build();
    }
}
