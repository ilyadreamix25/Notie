package com.github.ilyadreamix.notie.common.ui;

import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public class NotieStickyHeaderItemDecoration<VB extends ViewBinding> extends RecyclerView.ItemDecoration {

    private final NotieStickyHeaderManager<VB> manager;
    private int headerHeight;

    public NotieStickyHeaderItemDecoration(NotieStickyHeaderManager<VB> manager) {
        this.manager = manager;
    }

    @Override
    public void onDrawOver(
        @NonNull Canvas canvas,
        @NonNull RecyclerView parent,
        @NonNull RecyclerView.State state
    ) {
        super.onDrawOver(canvas, parent, state);
        View topChild = parent.getChildAt(0);
        if (topChild == null) {
            return;
        }

        int topChildPosition = parent.getChildAdapterPosition(topChild);
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return;
        }

        int headerPos = manager.getHeaderPositionForItem(topChildPosition);
        View currentHeader = getHeaderViewForItem(headerPos, parent);
        fixLayoutSize(parent, currentHeader);
        int contactPoint = currentHeader.getBottom();
        View childInContact = getChildInContact(parent, contactPoint, headerPos);

        if (childInContact != null && manager.isHeader(parent.getChildAdapterPosition(childInContact))) {
            moveHeader(canvas, currentHeader, childInContact);
            return;
        }

        drawHeader(canvas, currentHeader);
    }

    private View getHeaderViewForItem(int headerPosition, RecyclerView parent) {
        VB headerBinding = manager.getHeaderLayout(headerPosition, parent);
        return headerBinding.getRoot();
    }

    private void drawHeader(Canvas canvas, View header) {
        canvas.save();
        canvas.translate(0, 0);
        header.draw(canvas);
        canvas.restore();
    }

    private void moveHeader(Canvas canvas, View currentHeader, View nextHeader) {
        canvas.save();
        canvas.translate(0, nextHeader.getTop() - currentHeader.getHeight());
        currentHeader.draw(canvas);
        canvas.restore();
    }

    private View getChildInContact(RecyclerView parent, int contactPoint, int currentHeaderPos) {

        View childInContact = null;

        for (int i = 0; i < parent.getChildCount(); i++) {

            int heightTolerance = 0;
            View child = parent.getChildAt(i);

            if (currentHeaderPos != i) {
                boolean isChildHeader = manager.isHeader(parent.getChildAdapterPosition(child));
                if (isChildHeader) {
                    heightTolerance = headerHeight - child.getHeight();
                }
            }

            int childBottomPosition;
            if (child.getTop() > 0) {
                childBottomPosition = child.getBottom() + heightTolerance;
            } else {
                childBottomPosition = child.getBottom();
            }

            if (childBottomPosition > contactPoint) {
                if (child.getTop() <= contactPoint) {
                    childInContact = child;
                    break;
                }
            }
        }

        return childInContact;
    }

    private void fixLayoutSize(ViewGroup parent, View view) {

        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), view.getLayoutParams().height);

        view.measure(childWidthSpec, childHeightSpec);
        view.layout(0, 0, view.getMeasuredWidth(), headerHeight = view.getMeasuredHeight());
    }
}
