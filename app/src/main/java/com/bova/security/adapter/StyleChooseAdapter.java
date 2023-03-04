package com.bova.security.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bova.security.R;
import com.bova.security.entity.StyleModel;

import java.util.List;

public class StyleChooseAdapter extends RecyclerView.Adapter<StyleChooseAdapter.RecyclerHolder> {
    private Context context;
    private List<StyleModel> list;
    private OnItemClickListener listener;

    public StyleChooseAdapter(Context context, List<StyleModel> list) {
        this.context = context;
        this.list = list;
    }

    @SuppressLint("InflateParams")
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerHolder(LayoutInflater.from(context).inflate(R.layout.item_style, null));
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, @SuppressLint("RecyclerView") final int position) {
        final StyleModel bean = list.get(position);
        int imgResId = bean.getImageResId();
        holder.ivStyle.setImageResource(imgResId);
        String styleName = bean.getStyleName();
        holder.tvStyleName.setText(styleName);
        boolean isSelected = bean.getIsSelected();

        if (isSelected) {
            holder.ivStyle.setSelected(true);
            holder.tvStyleName.setSelected(true);
        } else {
            holder.ivStyle.setSelected(false);
            holder.tvStyleName.setSelected(false);
        }

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < getItemCount(); i++) {
                    list.get(i).setIsSelected(i == position);
                }
                notifyDataSetChanged();

                if (null != listener) {
                    listener.onItemClick(bean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class RecyclerHolder extends RecyclerView.ViewHolder {
        LinearLayout llContainer;
        ImageView ivStyle;
        TextView tvStyleName;

        private RecyclerHolder(View itemView) {
            super(itemView);

            llContainer = itemView.findViewById(R.id.llContainer);
            ivStyle = itemView.findViewById(R.id.ivStyle);
            tvStyleName = itemView.findViewById(R.id.tvStyleName);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(StyleModel styleModel);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
