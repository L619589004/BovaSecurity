package com.bova.security.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bova.security.R;
import com.bova.security.adapter.HorizontalItemDecoration;
import com.bova.security.adapter.StyleChooseAdapter;
import com.bova.security.databinding.FragmentPictureGenerateBinding;
import com.bova.security.databinding.FragmentPictureHandlerBinding;
import com.bova.security.entity.StyleModel;
import com.bova.security.uic.ClearableEditText;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PictureHandlerFragment extends Fragment {
    private FragmentPictureHandlerBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentPictureHandlerBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }
}
