package com.app.erl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erl.R;
import com.app.erl.callback.SelectItemListener;
import com.app.erl.databinding.RowAlbumsBinding;
import com.app.erl.model.entity.info.AlbumInfo;
import com.app.erl.util.AppConstant;
import com.app.utilities.GlideApp;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;

import java.io.File;
import java.util.List;


public class AlbumsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<AlbumInfo> albumsList;
    private SelectItemListener listener;

    public AlbumsAdapter(Context mContext, List<AlbumInfo> list) {
        this.mContext = mContext;
        this.albumsList = list;
//        setHasStableIds(true);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_albums, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AlbumInfo info = albumsList.get(position);

        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        itemViewHolder.binding.txtAlbumTitle.setText(info.getAlbum());
        itemViewHolder.binding.txtPhotosCount.setText(info.getCountPhoto() + " Photos");

        Log.e("test", "Album Title:" + info.getAlbum());

        if (!StringHelper.isEmpty(info.getPath())) {
            GlideUtil.loadImageFromFile(itemViewHolder.binding.imgAlbum, new File(info.getPath()), null, null, Constant.ImageScaleType.FIT_CENTER, null);
        }

        itemViewHolder.binding.routMainView.setOnClickListener(v -> {
            if (listener != null)
                listener.onSelectItem(position, AppConstant.Action.SELECT_ALBUM);
        });
    }

    @Override
    public int getItemCount() {
        return albumsList.size();
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private RowAlbumsBinding binding;

        private ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public SelectItemListener getListener() {
        return listener;
    }

    public void setListener(SelectItemListener listener) {
        this.listener = listener;
    }
}
