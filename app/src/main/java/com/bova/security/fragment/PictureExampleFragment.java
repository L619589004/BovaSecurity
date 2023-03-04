package com.bova.security.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.atech.staggedrv.GridItemDecoration;
import com.atech.staggedrv.StaggedAdapter;
import com.atech.staggedrv.callbacks.LoadMoreAndRefresh;
import com.atech.staggedrv.model.StaggedModel;
import com.bova.security.R;
import com.bova.security.databinding.FragmentPictureExampleBinding;
import com.bova.security.entity.ImageModel;

import java.util.ArrayList;
import java.util.List;

public class PictureExampleFragment extends Fragment {
    PictureAdapter<ImageModel> staggedAdapter;
    private FragmentPictureExampleBinding binding;

    private List<ImageModel> dataList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("PictureExampleFragment", "onCreateView");

        binding = FragmentPictureExampleBinding.inflate(inflater, container, false);

        staggedAdapter = new PictureAdapter<>(requireContext());
        binding.recycleView.link(staggedAdapter, 2);

        //动画效果
        binding.recycleView.addAnimation(R.anim.right_to_left);
        //间距
        binding.recycleView.addDecoration(new GridItemDecoration(requireContext(), 10));

        binding.recycleView.addCallbackListener(new LoadMoreAndRefresh() {
            @Override
            public void onLoadMore() {
                getData(false);
            }

            @Override
            public void onRefresh() {
                getData(true);
            }
        });

        getData(true);

        return binding.getRoot();
    }


    /**
     * 自定义adapter继承StaggedAdapter
     */

    class PictureAdapter<T extends StaggedModel> extends StaggedAdapter<T> {

        PictureAdapter(Context c) {
            super(c);
        }


        @Override
        public RecyclerView.ViewHolder addViewHolder(ViewGroup viewGroup, int i) {
            //绑定自定义的viewholder
            View v = LayoutInflater.from(requireContext()).inflate(R.layout.item_picture_example, viewGroup, false);
            return new MyHolder(v);
        }

        @Override
        public void bindView(RecyclerView.ViewHolder viewHolder, int i) {

            MyHolder myHolder = (MyHolder) viewHolder;

            ImageModel data = dataList.get(i);

            // 在加载图片之前设定好图片的宽高，防止出现item错乱及闪烁
            ViewGroup.LayoutParams layoutParams = myHolder.img.getLayoutParams();
            layoutParams.height = data.getHeight();
            myHolder.img.setLayoutParams(layoutParams);
            myHolder.img.setImageResource(data.localResorce());
            myHolder.tvTitle.setText(data.getTitle());
            myHolder.tvAuthor.setText(data.getAuthor());
        }


    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView tvTitle;
        TextView tvAuthor;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
        }
    }


    /**
     * 模拟网络请求
     *
     * @param refresh
     */

    private void getData(final boolean refresh) {

        //模拟刷新，只插入一遍数据
        if (refresh) {
            if (dataList.size() == 0) {
                dataList.add(new ImageModel(500, 500, R.mipmap.squre_image, "美丽的女孩，二次元画风", "@李雷雷"));
                dataList.add(new ImageModel(500, 800, R.mipmap.vertical_image, "帅气的的男孩，三次元画风", "@陈冠希"));
                dataList.add(new ImageModel(500, 300, R.mipmap.horizontal_image, "美丽的女孩，二次元画风", "@李雷雷"));
                dataList.add(new ImageModel(500, 800, R.mipmap.vertical_image2, "帅气的的男孩，三次元画风", "@陈冠希"));
                dataList.add(new ImageModel(500, 800, R.mipmap.vertical_image3, "美丽的女孩，二次元画风", "@李雷雷"));
            }
            staggedAdapter.refresh(dataList);

        } else {
            //模拟加载更多
            dataList.add(new ImageModel(500, 500, R.mipmap.squre_image, "美丽的女孩，二次元画风", "@李雷雷"));
            dataList.add(new ImageModel(500, 800, R.mipmap.vertical_image, "帅气的的男孩，三次元画风", "@陈冠希"));
            dataList.add(new ImageModel(500, 300, R.mipmap.horizontal_image, "美丽的女孩，二次元画风", "@李雷雷"));
            dataList.add(new ImageModel(500, 800, R.mipmap.vertical_image2, "帅气的的男孩，三次元画风", "@陈冠希"));
            dataList.add(new ImageModel(500, 800, R.mipmap.vertical_image3, "美丽的女孩，二次元画风", "@李雷雷"));

            staggedAdapter.loadMore(dataList);
        }


    }
}
