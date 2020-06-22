package com.app.erl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erl.R;
import com.app.erl.model.entity.info.MessageInfo;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<MessageInfo> messageList;
    private int currentUserId = 0;

    public ChatAdapter(Context context, List<MessageInfo> list) {
        this.mContext = context;
        currentUserId = AppUtils.getUserPrefrence(mContext).getId();
        this.messageList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == AppConstant.Type.ME) {
            itemView = inflater.inflate(R.layout.row_chat_right_item, parent, false);
        } else if (viewType == AppConstant.Type.FRIEND) {
            itemView = inflater.inflate(R.layout.row_chat_left_item, parent, false);
        }
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        MessageInfo info = messageList.get(position);
        itemViewHolder.txtMsg.setText(info.getMessage());
        itemViewHolder.txtTime.setText(info.getCreated_date());
        setImage(itemViewHolder.imgUserPic,info.getUser_image());

        itemViewHolder.parentView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getFrom_user_id() == currentUserId) {
            return AppConstant.Type.ME;
        } else {
            return AppConstant.Type.FRIEND;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtMsg, txtTime, txtUserName;
        private RelativeLayout parentView, routOverLayer;
        private LinearLayout routUserDetails, rlMessageView;
        private ImageView imgUserPic;

        public ItemViewHolder(View itemView) {
            super(itemView);
            txtMsg = itemView.findViewById(R.id.txtMsg);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            imgUserPic = itemView.findViewById(R.id.imgUserImage);
            parentView = itemView.findViewById(R.id.parentView);
        }
    }

    private void setImage(ImageView imageView, String url) {
        if (!StringHelper.isEmpty(url)) {
            GlideUtil.loadImageUsingGlideTransformation(url, imageView, Constant.TransformationType.CIRCLECROP_TRANSFORM, null, null, Constant.ImageScaleType.CENTER_CROP, 0, 0, "", 0, null);
        }
    }

    public void addMessage(MessageInfo info) {
        messageList.add(info);
        notifyDataSetChanged();
    }

    public void addMessages(List<MessageInfo> list) {
        messageList.addAll(list);
        notifyDataSetChanged();
    }

    public List<MessageInfo> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageInfo> messageList) {
        this.messageList = messageList;
    }
}