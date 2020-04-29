package com.app.erl.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.app.erl.ERLApp;
import com.app.erl.model.entity.request.SaveOrderRequest;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.OrderListResponse;
import com.app.erl.model.entity.response.OrderResourcesResponse;
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
    private MutableLiveData<OrderListResponse> mOrderListResponse;

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

    public void getClientOrders(int limit, int offset, boolean isProgress) {
        if (view != null && isProgress) {
            view.showProgress();
        }
        new RXRetroManager<OrderListResponse>() {
            @Override
            protected void onSuccess(OrderListResponse response) {
                if (view != null) {
                    mOrderListResponse.postValue(response);
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
        }.rxSingleCall(manageOrderInterface.clientOrders(limit, offset));
    }

    public void clientCancelOrders(int id) {
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
        }.rxSingleCall(manageOrderInterface.clientCancelOrders(id));
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

    public MutableLiveData<OrderListResponse> orderListResponse() {
        if (mOrderListResponse == null) {
            mOrderListResponse = new MutableLiveData<>();
        }
        return mOrderListResponse;
    }

    public SaveOrderRequest getSaveOrderRequest() {
        return saveOrderRequest;
    }

    public void setSaveOrderRequest(SaveOrderRequest saveOrderRequest) {
        this.saveOrderRequest = saveOrderRequest;
    }
}
