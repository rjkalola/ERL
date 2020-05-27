package com.app.erl.view.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erl.R;
import com.app.erl.databinding.FragmentHomeBinding;
import com.app.erl.databinding.FragmentPriceListBinding;
import com.app.erl.viewModel.DashBoardViewModel;

public class PriceListFragment extends BaseFragment implements View.OnClickListener {
    private final int LAYOUT_ACTIVITY = R.layout.fragment_price_list;
    private FragmentPriceListBinding binding;
    private Context mContext;
    private DashBoardViewModel dashBoardViewModel;
    private Menu mMenu;

    public static final PriceListFragment newInstance() {
        return new PriceListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, LAYOUT_ACTIVITY, container, false);
        mContext = getActivity();

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
//            case R.id.txtStartWork:
//                startStopWork();
//                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.dashboard_menu, menu);
        mMenu = menu;
        menu.findItem(R.id.actionArchived).setVisible(false);
        menu.findItem(R.id.actionNotification).setVisible(false);
        menu.findItem(R.id.actionSelectedItemCount).setVisible(true);
        menu.findItem(R.id.actionSearch).setVisible(true);

        setCartCountMenu(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void setCartCountMenu(int cartCount) {
        if (mMenu == null) return;
        MenuItem menuItem = mMenu.findItem(R.id.actionSelectedItemCount);
        RelativeLayout mContainer = (RelativeLayout) menuItem.setActionView(R.layout.layout_toolbar_item_count).getActionView();
        TextView mBadgeCount = mContainer.findViewById(R.id.txtItemCount);
        ImageView mIconBadge = mContainer.findViewById(R.id.icon_badge);
        mIconBadge.setImageResource(R.drawable.ic_selected_item_count);
        Drawable countIcon = getResources().getDrawable(R.drawable.img_white_circle);
        mBadgeCount.setBackground(countIcon);

        mContainer.setOnClickListener(v -> onOptionsItemSelected(menuItem));

        if (cartCount > 0) {
            mBadgeCount.setVisibility(View.VISIBLE);
            mBadgeCount.setText(String.valueOf(cartCount));
        } else {
            mBadgeCount.setVisibility(View.INVISIBLE);
        }
    }

}
