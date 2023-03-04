package com.bova.security.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bova.security.databinding.FragmentHistoryBinding;
import com.bova.security.databinding.FragmentPictureHandlerBinding;

public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }
}
