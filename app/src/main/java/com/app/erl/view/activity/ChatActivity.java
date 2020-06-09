package com.app.erl.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erl.R;
import com.app.erl.adapter.ChatAdapter;
import com.app.erl.databinding.ActivityChatBinding;

public class ChatActivity extends BaseActivity implements View.OnClickListener {
    private ActivityChatBinding binding;
    private Context mContext;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        mContext = this;
//        binding.txtHome.setOnClickListener(this);

        setAddressAdapter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.txtHome:
//                moveActivity(mContext, DashBoardActivity.class, true, true, null);
//                break;
        }
    }

    public void setAddressAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewChat.setLayoutManager(linearLayoutManager);
        binding.recyclerViewChat.setHasFixedSize(true);
        adapter = new ChatAdapter(mContext);
        binding.recyclerViewChat.setAdapter(adapter);
    }

}
