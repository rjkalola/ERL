package com.app.erl.view.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erl.R;
import com.app.erl.callback.LocationUpdateCallBack;
import com.app.erl.databinding.ActivityAddAddressBinding;
import com.app.erl.model.entity.info.ModuleInfo;
import com.app.erl.model.entity.info.ModuleSelection;
import com.app.erl.model.entity.response.AddressResourcesResponse;
import com.app.erl.model.entity.response.User;
import com.app.erl.util.AppConstant;
import com.app.erl.util.AppUtils;
import com.app.erl.util.LocationHelper;
import com.app.erl.util.LoginViewModelFactory;
import com.app.erl.util.ResourceProvider;
import com.app.erl.view.dialog.DropdownDialog;
import com.app.erl.viewModel.ManageAddressViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.StringHelper;
import com.app.utilities.utils.ToastHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddAddressActivity extends BaseActivity implements OnMapReadyCallback
        , LocationUpdateCallBack, View.OnClickListener {
    private GoogleMap mMap;
    private ActivityAddAddressBinding binding;
    private Context mContext;
    private static final int DEFAULT_ZOOM = 15;
    private Location mLocation;
    private LocationHelper mLocationHelper;
    private ManageAddressViewModel manageAddressViewModel;
    private AddressResourcesResponse addressData;
    private List<ModuleInfo> listArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = AddAddressActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_address);
        mLocationHelper = new LocationHelper(this);
        mLocationHelper.setLocationUpdateListener(this);
        mLocationHelper.isGPSEnabled();
        listArea = new ArrayList<>();
        startLocationUpdate();
//        binding.slidingLayout.setAnchorPoint(0.5f);
//        binding.slidingLayout.setClipPanel(true);

        manageAddressViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(ManageAddressViewModel.class);
        manageAddressViewModel.createView(this);
        manageAddressViewModel.addressResourcesResponse()
                .observe(this, addressResourcesResponse());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        binding.slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.e("test", "newState:" + newState.toString());
            }
        });

        binding.imgBack.setOnClickListener(this);
        binding.imgMoveToCurrentLocation.setOnClickListener(this);
        binding.routAddressView.edtCity.setOnClickListener(this);
        binding.routAddressView.edtArea.setOnClickListener(this);

        initData();
    }

    public void initData() {
        User user = AppUtils.getUserPrefrence(mContext);
        if (user != null) {
            binding.routAddressView.edtName.setText(!StringHelper.isEmpty(user.getName()) ? user.getName() : "");
            binding.routAddressView.edtMobileNumber.setText(!StringHelper.isEmpty(user.getPhone()) ? user.getPhone() : "");
            manageAddressViewModel.getAddressResourcesRequest();
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgMoveToCurrentLocation:
                setCurrentLocationPin();
                break;
            case R.id.edtCity:
                showModuleDialog(AppConstant.DialogIdentifier.SELECT_CITY, "", getAddressData().getCities(), true);
                break;
            case R.id.edtArea:
                if (listArea.size() > 0) {
                    showModuleDialog(AppConstant.DialogIdentifier.SELECT_AREA, "", listArea, true);
                } else {
                    ToastHelper.error(mContext, getString(R.string.msg_select_city_first), Toast.LENGTH_SHORT, false);
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(() -> {
            LatLng midLatLng = mMap.getCameraPosition().target;
            String address = getAddress(mContext, midLatLng.latitude, midLatLng.longitude);
            if (!StringHelper.isEmpty(address)) {
                binding.routAddressView.txtFullAddress.setText(address);
                binding.routAddressView.edtHouseNumber.setText(address);
                manageAddressViewModel.getSaveAddressRequest().setAddress(address);
            }
        });
    }

    public String getAddress(Context context, double latitude, double longitude) {
        String address = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                address = addresses.get(0).getAddressLine(0);

                if (!StringHelper.isEmpty(addresses.get(0).getSubLocality())) {
                    binding.routAddressView.txtShortAddress.setText(addresses.get(0).getSubLocality());
                } else if (!StringHelper.isEmpty(addresses.get(0).getLocality())) {
                    binding.routAddressView.txtShortAddress.setText(addresses.get(0).getLocality());
                } else if (!StringHelper.isEmpty(addresses.get(0).getSubAdminArea())) {
                    binding.routAddressView.txtShortAddress.setText(addresses.get(0).getSubAdminArea());
                } else if (!StringHelper.isEmpty(addresses.get(0).getAdminArea())) {
                    binding.routAddressView.txtShortAddress.setText(addresses.get(0).getAdminArea());
                } else {
                    binding.routAddressView.txtShortAddress.setText("");
                }

                Log.e("test", "address:" + address);
                Log.e("test", "getAdminArea:" + addresses.get(0).getAdminArea());
                Log.e("test", "getSubAdminArea:" + addresses.get(0).getSubAdminArea());
                Log.e("test", "getLocality:" + addresses.get(0).getLocality());
                Log.e("test", "getSubLocality:" + addresses.get(0).getSubLocality());
            }
        } catch (IOException ignored) {
        }
        return address;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdate();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConstant.IntentKey.LOCATION_SETTING_STATUS:
                Log.e("test", "LOCATION_SETTING_STATUS");
                if (resultCode == RESULT_CANCELED) {
                    Log.e("test", "RESULT_CANCELED");
                    mLocationHelper.isGPSEnabled();
                } else if (resultCode == RESULT_OK) {
                    Log.e("test", "RESULT_OK");
                    startLocationUpdate();
                }
                break;
            default:
                break;
        }
    }

    public void startLocationUpdate() {
        if (mLocationHelper != null)
            mLocationHelper.startLocationUpdates();
    }

    public void stopLocationUpdate() {
        if (mLocationHelper != null)
            mLocationHelper.stopLocationUpdates();
    }

    @Override
    public void locationUpdate(Location location) {
        if (location != null) {
            mLocation = location;
            setCurrentLocationPin();
            stopLocationUpdate();
        }
    }

    public void setCurrentLocationPin() throws NumberFormatException {
        if (mLocation != null) {
            CameraPosition cPosition = new CameraPosition.Builder()
                    .target(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                    .zoom(DEFAULT_ZOOM)
                    .build();
            if (mMap != null)
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cPosition));
        }
    }

    public Observer addressResourcesResponse() {
        return (Observer<AddressResourcesResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setAddressData(response);
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public void showModuleDialog(int type, String title, List<ModuleInfo> list, boolean singleChoice) {
        FragmentManager fm = getSupportFragmentManager();
        DropdownDialog dropdownDialogFragment = DropdownDialog.newInstance(mContext, title, list, type, singleChoice);
        dropdownDialogFragment.show(fm, "dropdownFragment");
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ModuleSelection moduleInfo) {
        if (moduleInfo != null) {
            if (moduleInfo.getType() == AppConstant.DialogIdentifier.SELECT_CITY) {
                binding.routAddressView.edtCity.setText(moduleInfo.getInfo().getName());
                manageAddressViewModel.getSaveAddressRequest().setArea_id(0);
                binding.routAddressView.edtArea.setText("");
                listArea = new ArrayList<>();
                for (int i = 0; i < getAddressData().getArea().size(); i++) {
                    if (getAddressData().getArea().get(i).getCity_id() == moduleInfo.getInfo().getId()) {
                        listArea.add(getAddressData().getArea().get(i));
                    }
                }
            } else if (moduleInfo.getType() == AppConstant.DialogIdentifier.SELECT_AREA) {
                binding.routAddressView.edtArea.setText(moduleInfo.getInfo().getName());
                manageAddressViewModel.getSaveAddressRequest().setArea_id(moduleInfo.getInfo().getId());
            }
        }
    }

    public AddressResourcesResponse getAddressData() {
        return addressData;
    }

    public void setAddressData(AddressResourcesResponse addressData) {
        this.addressData = addressData;
    }
}
