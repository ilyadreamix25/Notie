package com.github.ilyadreamix.notie.common;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.github.ilyadreamix.notie.R;

public abstract class NotieFragment extends Fragment {

    @Nullable
    private Dialog loadingDialog = null;

    protected void showLoadingDialog() {
        if (loadingDialog != null) {
            return;
        }

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        loadingDialog = dialog;
        dialog.create();
        dialog.show();
    }

    protected void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    protected void showToast(@StringRes int stringRes) {
        Toast.makeText(requireContext(), stringRes, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String text) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected NavController findNavController() {
        return Navigation.findNavController(requireActivity(), R.id.notie_nav_host_fragment);
    }
}
