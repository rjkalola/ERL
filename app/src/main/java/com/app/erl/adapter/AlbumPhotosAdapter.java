package com.app.erl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erl.R;
import com.app.erl.callback.SelectItemListener;
import com.app.erl.databinding.RowAlbumPhotosBinding;
import com.app.erl.model.entity.info.AlbumInfo;
import com.app.erl.util.AppConstant;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;

import java.io.File;
import java.util.List;


public class AlbumPhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<AlbumInfo> albumsList;
    private SelectItemListener listener;

    public AlbumPhotosAdapter(Context mContext, List<AlbumInfo> list) {
        this.mContext = mContext;
        this.albumsList = list;
//        setHasStableIds(true);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_album_photos, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AlbumInfo info = albumsList.get(position);
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        if (!StringHelper.isEmpty(info.getPath())) {
            GlideUtil.loadImageFromFile(itemViewHolder.binding.imgAlbum, new File(info.getPath()), null, null, Constant.ImageScaleType.FIT_CENTER, null);
        }

        itemViewHolder.binding.routMainView.setOnClickListener(v -> {
            if (listener != null)
                listener.onSelectItem(position, AppConstant.Action.SELECT_PHOTO);
        });
    }

    @Override
    public int getItemCount() {
        return albumsList.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowAlbumPhotosBinding binding;

        private ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

//    @Override
//    public void setHasStableIds(boolean hasStableIds) {
//        super.setHasStableIds(hasStableIds);
//    }

    public SelectItemListener getListener() {
        return listener;
    }

    public void setListener(SelectItemListener listener) {
        this.listener = listener;
    }
}
