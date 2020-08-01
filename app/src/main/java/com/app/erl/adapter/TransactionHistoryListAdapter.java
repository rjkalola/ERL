package com.app.erl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erl.R;
import com.app.erl.databinding.RowTransactionHistoryListBinding;
import com.app.erl.model.entity.info.TransactionHistoryInfo;

import java.util.List;

public class TransactionHistoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<TransactionHistoryInfo> list;

    public TransactionHistoryListAdapter(Context context, List<TransactionHistoryInfo> list) {
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction_history_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        TransactionHistoryInfo info = list.get(position);
        if (info.getStatus() == 1) {
            itemViewHolder.binding.imgArrow.setColorFilter(mContext.getResources().getColor(R.color.colorGreen));
            itemViewHolder.binding.imgArrow.setScaleY(-1);
            itemViewHolder.binding.txtAmount.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
            itemViewHolder.binding.txtAmount.setText(String.format(mContext.getResources().getString(R.string.lbl_display_added_price), Integer.toString(info.getAmount())));
        } else {
            itemViewHolder.binding.imgArrow.setColorFilter(mContext.getResources().getColor(R.color.colorRed));
            itemViewHolder.binding.txtAmount.setTextColor(mContext.getResources().getColor(R.color.colorRed));
            itemViewHolder.binding.txtAmount.setText(String.format(mContext.getResources().getString(R.string.lbl_display_deduct_price), Integer.toString(info.getAmount())));
        }
        itemViewHolder.getData(info);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowTransactionHistoryListBinding binding;

        public void getData(TransactionHistoryInfo info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
