package com.github.ilyadreamix.notie.common.utility;

import androidx.core.widget.NestedScrollView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class ExtendedFloatingActionButtonUtility {
    public static void enableShrinkOnScroll(
        ExtendedFloatingActionButton floatingActionButton,
        NestedScrollView scrollView
    ) {
        NestedScrollView.OnScrollChangeListener listener = (view, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY && floatingActionButton.isExtended()) {
                floatingActionButton.shrink();
            } else if (scrollY < oldScrollY && !floatingActionButton.isExtended()) {
                floatingActionButton.extend();
            }
        };
        scrollView.setOnScrollChangeListener(listener);
    }
}
