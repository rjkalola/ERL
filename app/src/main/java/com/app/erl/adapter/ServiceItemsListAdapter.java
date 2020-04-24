package com.app.erl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erl.R;
import com.app.erl.callback.SelectItemListener;
import com.app.erl.databinding.RowServiceItemsListBinding;
import com.app.erl.model.entity.info.ServiceItemInfo;

import java.util.List;

public class ServiceItemsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ServiceItemInfo> list;
    private SelectItemListener listener;
    private int position;

    public ServiceItemsListAdapter(Context context, List<ServiceItemInfo> list, SelectItemListener listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_service_items_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ServiceItemInfo info = list.get(position);
        itemViewHolder.getData(info);

        itemViewHolder.binding.txtPrice.setText(String.format(mContext.getString(R.string.lbl_display_price), info.getPrice()));
        setSelectedItemCont(info, itemViewHolder.binding);

        itemViewHolder.binding.txtAdd.setOnClickListener(v -> {
            if (listener != null) {
                info.setQuantity(info.getQuantity() + 1);
                setSelectedItemCont(info, itemViewHolder.binding);
                listener.onSelectItem(position, info.getQuantity());
//                notifyItemChanged(position);
            }
        });

        itemViewHolder.binding.imgAdd.setOnClickListener(v -> {
            if (listener != null) {
                info.setQuantity(info.getQuantity() + 1);
                setSelectedItemCont(info, itemViewHolder.binding);
                listener.onSelectItem(position, info.getQuantity());
//                notifyItemChanged(position);
            }
        });

        itemViewHolder.binding.imgRemove.setOnClickListener(v -> {
            if (listener != null && info.getQuantity() > 0) {
                info.setQuantity(info.getQuantity() - 1);
                setSelectedItemCont(info, itemViewHolder.binding);
                listener.onSelectItem(position, info.getQuantity());
//                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowServiceItemsListBinding binding;

        public void getData(ServiceItemInfo info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void setSelectedItemCont(ServiceItemInfo info, RowServiceItemsListBinding binding) {
        if (info.getQuantity() > 0) {
            binding.txtAdd.setVisibility(View.INVISIBLE);
            binding.routAddRemoveView.setVisibility(View.VISIBLE);
        } else {
            binding.txtAdd.setVisibility(View.VISIBLE);
            binding.routAddRemoveView.setVisibility(View.INVISIBLE);
        }
        binding.txtQuantity.setText(String.valueOf(info.getQuantity()));
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
