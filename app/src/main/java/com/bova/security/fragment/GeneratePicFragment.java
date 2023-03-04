package com.bova.security.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.bova.security.databinding.FragmentPictureExampleBinding;
import com.bova.security.databinding.FragmentPictureGenerateBinding;
import com.bova.security.entity.ImageModel;
import com.bova.security.entity.StyleModel;
import com.bova.security.uic.ClearableEditText;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneratePicFragment extends Fragment {
    private FragmentPictureGenerateBinding binding;

    private StyleChooseAdapter styleChooseAdapter;
    private List<StyleModel> dataList = new ArrayList<>();

    private StyleModel mStyleModel;
    private int ratioPosition = -1;

    private final List<LinearLayout> ratioContainerArray = new ArrayList<>();
    private final List<ImageView> ratioImageViewArray = new ArrayList<>();
    private final List<TextView> ratioTextViewArray = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        binding = FragmentPictureGenerateBinding.inflate(inflater, container, false);

        initData();

        styleChooseAdapter = new StyleChooseAdapter(requireContext(), dataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvStyle.setLayoutManager(layoutManager);
        binding.rvStyle.addItemDecoration(new HorizontalItemDecoration(24));
        binding.rvStyle.setAdapter(styleChooseAdapter);
        styleChooseAdapter.setOnItemClickListener(new StyleChooseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(StyleModel styleModel) {
                mStyleModel = styleModel;
            }
        });

        binding.etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.tvLength.setText(String.valueOf(s.length()));
            }
        });

        binding.llRandomInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etDesc.setText(getRandomString(200));
            }
        });

        binding.llClearContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etDesc.setText("");
            }
        });

        refreshRatioUI(0);

        binding.llSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshRatioUI(0);
            }
        });

        binding.llVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshRatioUI(1);
            }
        });

        binding.llHorizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshRatioUI(2);
            }
        });

        binding.btnGeneratePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 生成画作
            }
        });

        return binding.getRoot();
    }

    private void initData() {
        dataList.add(new StyleModel(R.drawable.bg_anime_style, "动漫风", false));
        dataList.add(new StyleModel(R.drawable.bg_classical_style, "复古风", false));
        dataList.add(new StyleModel(R.drawable.bg_sketch_style, "素描风", false));
        dataList.add(new StyleModel(R.drawable.bg_modern_style, "现代风", false));

        ratioContainerArray.add(binding.llSquare);
        ratioContainerArray.add(binding.llVertical);
        ratioContainerArray.add(binding.llHorizontal);
        ratioImageViewArray.add(binding.ivSquare);
        ratioImageViewArray.add(binding.ivVertical);
        ratioImageViewArray.add(binding.ivHorizontal);
        ratioTextViewArray.add(binding.tvSquare);
        ratioTextViewArray.add(binding.tvVertical);
        ratioTextViewArray.add(binding.tvHorizontal);
    }

    private void refreshRatioUI(int position) {
        if (position == ratioPosition) {
            return;
        }

        for (int i = 0; i < ratioContainerArray.size(); i++) {
            if (i == position) {
                ratioContainerArray.get(i).setSelected(true);
                ratioImageViewArray.get(i).setSelected(true);
                ratioTextViewArray.get(i).setSelected(true);
            } else {
                ratioContainerArray.get(i).setSelected(false);
                ratioImageViewArray.get(i).setSelected(false);
                ratioTextViewArray.get(i).setSelected(false);
            }
        }

        ratioPosition = position;
    }

    private String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
