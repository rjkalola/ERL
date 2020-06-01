package com.app.erl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erl.R;
import com.app.erl.callback.SelectItemListener;
import com.app.erl.callback.SelectedServiceItemListener;
import com.app.erl.databinding.RowServiceItemsListBinding;
import com.app.erl.databinding.RowServiceSelectedItemsTitleListBinding;
import com.app.erl.model.entity.info.ClientDashBoardInfo;
import com.app.erl.model.entity.info.ItemInfo;
import com.app.erl.model.entity.info.ServiceInfo;
import com.app.erl.model.entity.info.ServiceItemInfo;

import java.util.List;

public class ServiceSelectedItemsTitleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ItemInfo> list;
    private SelectedServiceItemListener listener;
    private int position;

    public ServiceSelectedItemsTitleListAdapter(Context context, List<ItemInfo> list, SelectedServiceItemListener listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_service_selected_items_title_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ItemInfo info = list.get(position);
        itemViewHolder.getData(info);
        itemViewHolder.binding.txtTitle.setText(info.getName());
        setSelectedItemsAdapter(itemViewHolder.binding.rvSelectedItems, info.getServiceList(), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowServiceSelectedItemsTitleListBinding binding;

        public void getData(ItemInfo info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    private void setSelectedItemsAdapter(RecyclerView recyclerView, List<ServiceItemInfo> service_item, int position) {
        if (service_item != null && service_item.size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            recyclerView.setHasFixedSize(true);
            ServiceSelectedItemsListAdapter adapter = new ServiceSelectedItemsListAdapter(mContext, service_item, position, listener);
            recyclerView.setAdapter(adapter);
        }
    }

//    @Override
//    public void onSelectItem(int position, int quantity) {
//        Log.e("test", "position:" + position + " Quantity:" + position);
//    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
