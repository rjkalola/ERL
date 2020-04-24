package com.app.erl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erl.R;
import com.app.erl.callback.SelectItemListener;
import com.app.erl.databinding.RowDashboardItemsListBinding;
import com.app.erl.model.entity.info.ClientDashBoardInfo;
import com.app.erl.view.activity.BaseActivity;
import com.app.erl.view.activity.DashBoardActivity;
import com.app.erl.view.activity.SelectOrderItemsActivity;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;

import java.util.List;

public class ClientDashBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ClientDashBoardInfo> list;
    private SelectItemListener listener;

    public ClientDashBoardAdapter(Context context, List<ClientDashBoardInfo> list, SelectItemListener listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_dashboard_items_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ClientDashBoardInfo info = list.get(position);
        itemViewHolder.getData(info);
        itemViewHolder.binding.txtTitle.setText(info.getName().replace("\\n", "\n"));
        if (!StringHelper.isEmpty(info.getImage()))
            GlideUtil.loadImage(info.getImage(), itemViewHolder.binding.img, null, null, Constant.ImageScaleType.CENTER_CROP, null);
        itemViewHolder.binding.routMainView.setOnClickListener(v -> {
            if (mContext instanceof DashBoardActivity)
                ((DashBoardActivity) mContext).moveOrderItemsList(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowDashboardItemsListBinding binding;

        public void getData(ClientDashBoardInfo info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}
