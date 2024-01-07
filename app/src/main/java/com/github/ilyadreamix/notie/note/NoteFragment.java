package com.github.ilyadreamix.notie.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.ilyadreamix.notie.common.NotieFragment;
import com.github.ilyadreamix.notie.databinding.NoteFragmentBinding;
import com.google.android.material.transition.platform.MaterialContainerTransform;

public class NoteFragment extends NotieFragment {

    private NoteFragmentBinding binding;

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
        binding = NoteFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
