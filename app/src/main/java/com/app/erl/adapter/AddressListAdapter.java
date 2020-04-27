package com.app.erl.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erl.R;
import com.app.erl.callback.SelectItemListener;
import com.app.erl.databinding.RowAddressListBinding;
import com.app.erl.model.entity.request.SaveAddressRequest;
import com.app.erl.util.AppConstant;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<SaveAddressRequest> list;
    private SelectItemListener listener;
    private int position;

    public AddressListAdapter(Context context, List<SaveAddressRequest> list, SelectItemListener listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_address_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        SaveAddressRequest info = list.get(position);
        itemViewHolder.getData(info);

        itemViewHolder.binding.routMainView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSelectItem(position, AppConstant.Action.SELECT_ADDRESS);
            }
        });

        itemViewHolder.binding.routMainView.setOnLongClickListener(v -> {
            this.position = position;
            showManageAddressMenu();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowAddressListBinding binding;

        public void getData(SaveAddressRequest info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void showManageAddressMenu() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_selectable_list_item);
        arrayAdapter.add(mContext.getString(R.string.edit));
        arrayAdapter.add(mContext.getString(R.string.delete));

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            if (listener != null) {
                if (which == 0) {
                    listener.onSelectItem(position, AppConstant.Action.EDIT_ADDRESS);
                } else if (which == 1) {
                    listener.onSelectItem(position, AppConstant.Action.DELETE_ADDRESS);
                }
            }
        });
        builderSingle.show();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
