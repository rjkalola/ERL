package com.app.erl.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.app.erl.ERLApp;
import com.app.erl.model.entity.request.SaveAddressRequest;
import com.app.erl.model.entity.response.AddressResourcesResponse;
import com.app.erl.model.state.ManageAddressInterface;
import com.app.erl.network.RXRetroManager;
import com.app.erl.network.RetrofitException;
import com.app.erl.util.ResourceProvider;

import javax.inject.Inject;

public class ManageAddressViewModel extends BaseViewModel {
    @Inject
    ManageAddressInterface manageAddressInterface;

    private MutableLiveData<AddressResourcesResponse> addressResourcesResponse;

    private SaveAddressRequest saveAddressRequest;

    public ManageAddressViewModel(ResourceProvider resourceProvider) {
        ERLApp.getServiceComponent().inject(this);
        saveAddressRequest = new SaveAddressRequest();
    }

    public void getAddressResourcesRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<AddressResourcesResponse>() {
            @Override
            protected void onSuccess(AddressResourcesResponse response) {
                if (view != null) {
                    addressResourcesResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(manageAddressInterface.getAddressResources());
    }

    public MutableLiveData<AddressResourcesResponse> addressResourcesResponse() {
        if (addressResourcesResponse == null) {
            addressResourcesResponse = new MutableLiveData<>();
        }
        return addressResourcesResponse;
    }

    public SaveAddressRequest getSaveAddressRequest() {
        return saveAddressRequest;
    }

    public void setSaveAddressRequest(SaveAddressRequest saveAddressRequest) {
        this.saveAddressRequest = saveAddressRequest;
    }
}
