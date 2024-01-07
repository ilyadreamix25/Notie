package com.github.ilyadreamix.notie.common.ui;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public interface NotieStickyHeaderManager<VB extends ViewBinding> {
    int getHeaderPositionForItem(int itemPosition);
    VB getHeaderLayout(int headerPosition, RecyclerView parent);
    void bindHeaderLayout(VB header, int headerPosition);
    boolean isHeader(int itemPosition);
}
