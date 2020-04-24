package com.app.erl.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.app.erl.ERLApp;
import com.app.erl.model.entity.response.ClientDashBoardResponse;
import com.app.erl.model.entity.response.ServiceItemsResponse;
import com.app.erl.model.state.DashBoardServiceInterface;
import com.app.erl.network.RXRetroManager;
import com.app.erl.network.RetrofitException;
import com.app.erl.util.ResourceProvider;

import javax.inject.Inject;

public class DashBoardViewModel extends BaseViewModel {
    @Inject
    DashBoardServiceInterface dashBoardServiceInterface;

    private MutableLiveData<ClientDashBoardResponse> clientDashBoardResponse;
    private MutableLiveData<ServiceItemsResponse> serviceItemsResponse;

    public DashBoardViewModel(ResourceProvider resourceProvider) {
        ERLApp.getServiceComponent().inject(this);
    }

    public void getClientDashboardRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<ClientDashBoardResponse>() {
            @Override
            protected void onSuccess(ClientDashBoardResponse response) {
                if (view != null) {
                    clientDashBoardResponse.postValue(response);
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
        }.rxSingleCall(dashBoardServiceInterface.getClientDashboard());
    }

    public void getServiceItemsRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<ServiceItemsResponse>() {
            @Override
            protected void onSuccess(ServiceItemsResponse response) {
                if (view != null) {
                    serviceItemsResponse.postValue(response);
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
        }.rxSingleCall(dashBoardServiceInterface.getServiceItems());
    }

    public MutableLiveData<ClientDashBoardResponse> clientDashBoardResponse() {
        if (clientDashBoardResponse == null) {
            clientDashBoardResponse = new MutableLiveData<>();
        }
        return clientDashBoardResponse;
    }

    public MutableLiveData<ServiceItemsResponse> serviceItemsResponse() {
        if (serviceItemsResponse == null) {
            serviceItemsResponse = new MutableLiveData<>();
        }
        return serviceItemsResponse;
    }
}
