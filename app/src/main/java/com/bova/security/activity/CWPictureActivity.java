package com.bova.security.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bova.security.R;
import com.bova.security.databinding.ActivityCwPictureBinding;
import com.bova.security.fragment.GeneratePicFragment;
import com.bova.security.fragment.HistoryFragment;
import com.bova.security.fragment.PictureExampleFragment;
import com.bova.security.fragment.PictureHandlerFragment;

import java.util.ArrayList;
import java.util.List;

public class CWPictureActivity extends AppCompatActivity {

    private ActivityCwPictureBinding binding;
    private final List<Fragment> fragmentArray = new ArrayList<>();
    private final List<TextView> tvTabArray = new ArrayList<>();
    private final List<View> indicatorArray = new ArrayList<>();

    private int lastTabPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_cw_picture);

        getSupportActionBar().hide();

        init();

        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refreshCurrentTab(lastTabPosition);

        binding.llExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastTabPosition == 0) {
                    return;
                }
                refreshCurrentTab(0);
                switchFragment(0);
            }
        });

        binding.llGeneratePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastTabPosition == 1) {
                    return;
                }
                refreshCurrentTab(1);
                switchFragment(1);
            }
        });

        binding.llPicProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastTabPosition == 2) {
                    return;
                }
                refreshCurrentTab(2);
                switchFragment(2);
            }
        });

        binding.llHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastTabPosition == 3) {
                    return;
                }
                refreshCurrentTab(3);
                switchFragment(3);
            }
        });

        getSupportFragmentManager().beginTransaction().add(binding.container.getId(), fragmentArray.get(0)).commit();
    }

    private void init() {
        fragmentArray.add(new PictureExampleFragment());
        fragmentArray.add(new GeneratePicFragment());
        fragmentArray.add(new PictureHandlerFragment());
        fragmentArray.add(new HistoryFragment());
        tvTabArray.add(binding.tvExample);
        tvTabArray.add(binding.tvGeneratePic);
        tvTabArray.add(binding.tvPicProcess);
        tvTabArray.add(binding.tvHistory);
        indicatorArray.add(binding.indicator1);
        indicatorArray.add(binding.indicator2);
        indicatorArray.add(binding.indicator3);
        indicatorArray.add(binding.indicator4);
    }

    private void refreshCurrentTab(int position) {
        for (int i = 0; i < tvTabArray.size(); i++) {
            if (i == position) {
                tvTabArray.get(i).setSelected(true);
                indicatorArray.get(i).setVisibility(View.VISIBLE);
                indicatorAdaptive(tvTabArray.get(i), indicatorArray.get(i));
            } else {
                tvTabArray.get(i).setSelected(false);
                indicatorArray.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * indicator自适应
     */
    private void indicatorAdaptive(TextView tvContent, View indicator) {
        tvContent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int textViewWidth = tvContent.getMeasuredWidth();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) indicator.getLayoutParams();
        layoutParams.width = textViewWidth;
        indicator.setLayoutParams(layoutParams);
    }

    private void switchFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (!fragmentArray.get(position).isAdded()) {
            Log.e("switchFragment", "position = " + position);
            transaction.hide(fragmentArray.get(lastTabPosition)).add(binding.container.getId(), fragmentArray.get(position)).commit();
        } else {
            transaction.hide(fragmentArray.get(lastTabPosition)).show(fragmentArray.get(position)).commit();
        }
        lastTabPosition = position;
    }
}
