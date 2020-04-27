package com.app.erl.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.app.erl.ERLApp;
import com.app.erl.model.entity.request.SaveAddressRequest;
import com.app.erl.model.entity.request.SaveOrderRequest;
import com.app.erl.model.entity.response.AddressResourcesResponse;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.OrderResourcesResponse;
import com.app.erl.model.state.ManageAddressInterface;
import com.app.erl.model.state.ManageOrderInterface;
import com.app.erl.network.RXRetroManager;
import com.app.erl.network.RetrofitException;
import com.app.erl.util.ResourceProvider;

import javax.inject.Inject;

public class ManageOrderViewModel extends BaseViewModel {
    @Inject
    ManageOrderInterface manageOrderInterface;

    private MutableLiveData<OrderResourcesResponse> orderResourcesResponse;
    private MutableLiveData<BaseResponse> mBaseResponse;
    private SaveOrderRequest saveOrderRequest;

    public ManageOrderViewModel(ResourceProvider resourceProvider) {
        ERLApp.getServiceComponent().inject(this);
        saveOrderRequest = new SaveOrderRequest();
    }

    public void getOrderResourcesRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<OrderResourcesResponse>() {
            @Override
            protected void onSuccess(OrderResourcesResponse response) {
                if (view != null) {
                    orderResourcesResponse.postValue(response);
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
        }.rxSingleCall(manageOrderInterface.getOrderResources());
    }

    public void saveAddressRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<BaseResponse>() {
            @Override
            protected void onSuccess(BaseResponse response) {
                if (view != null) {
                    mBaseResponse.postValue(response);
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
        }.rxSingleCall(manageOrderInterface.placeOrder(saveOrderRequest));
    }

    public MutableLiveData<BaseResponse> mBaseResponse() {
        if (mBaseResponse == null) {
            mBaseResponse = new MutableLiveData<>();
        }
        return mBaseResponse;
    }

    public MutableLiveData<OrderResourcesResponse> orderResourcesResponse() {
        if (orderResourcesResponse == null) {
            orderResourcesResponse = new MutableLiveData<>();
        }
        return orderResourcesResponse;
    }

    public SaveOrderRequest getSaveOrderRequest() {
        return saveOrderRequest;
    }

    public void setSaveOrderRequest(SaveOrderRequest saveOrderRequest) {
        this.saveOrderRequest = saveOrderRequest;
    }
}
