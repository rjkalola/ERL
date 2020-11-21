package com.app.erl.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.app.erl.ERLApp;
import com.app.erl.model.entity.request.SaveOrderRequest;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.OrderDetailsResponse;
import com.app.erl.model.entity.response.OrderListResponse;
import com.app.erl.model.entity.response.OrderResourcesResponse;
import com.app.erl.model.entity.response.PromoCodeResponse;
import com.app.erl.model.entity.response.TransactionHistoryResponse;
import com.app.erl.model.state.ManageOrderInterface;
import com.app.erl.network.RXRetroManager;
import com.app.erl.network.RetrofitException;
import com.app.erl.util.ResourceProvider;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ManageOrderViewModel extends BaseViewModel {
    @Inject
    ManageOrderInterface manageOrderInterface;

    private MutableLiveData<OrderResourcesResponse> orderResourcesResponse;
    private MutableLiveData<BaseResponse> mBaseResponse;
    private MutableLiveData<OrderListResponse> mOrderListResponse;
    private MutableLiveData<OrderDetailsResponse> mOrderDetailsResponse;
    private MutableLiveData<PromoCodeResponse> mPromoCodeResponse;
    private MutableLiveData<TransactionHistoryResponse> mTransactionHistoryResponse;

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

    public void clientOrderDetailsRequest(int orderId) {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<OrderDetailsResponse>() {
            @Override
            protected void onSuccess(OrderDetailsResponse response) {
                if (view != null) {
                    mOrderDetailsResponse.postValue(response);
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
        }.rxSingleCall(manageOrderInterface.clientOrderDetails(orderId));
    }

    public void checkPromoCodeRequest(String promoCode) {
        RequestBody promoCodeBody = RequestBody.create(MediaType.parse("text/plain"), promoCode);

        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<PromoCodeResponse>() {
            @Override
            protected void onSuccess(PromoCodeResponse response) {
                if (view != null) {
                    mPromoCodeResponse.postValue(response);
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
        }.rxSingleCall(manageOrderInterface.checkPromoCode(promoCodeBody));
    }

    public void addCouponCodeRequest(String code) {
        RequestBody couponCodeBody = RequestBody.create(MediaType.parse("text/plain"), code);

        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<PromoCodeResponse>() {
            @Override
            protected void onSuccess(PromoCodeResponse response) {
                if (view != null) {
                    mPromoCodeResponse.postValue(response);
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
        }.rxSingleCall(manageOrderInterface.addCouponCode(couponCodeBody));
    }

    public void transactionHistoryRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<TransactionHistoryResponse>() {
            @Override
            protected void onSuccess(TransactionHistoryResponse response) {
                if (view != null) {
                    mTransactionHistoryResponse.postValue(response);
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
        }.rxSingleCall(manageOrderInterface.getTransactionHistory());
    }

    public void storePaymentInfoRequest(int orderId, int status, String code) {
        RequestBody orderIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(orderId));
        RequestBody statusBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(status));
        RequestBody codeBody = RequestBody.create(MediaType.parse("text/plain"), code);

//        if (view != null) {
//            view.showProgress();
//        }
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
        }.rxSingleCall(manageOrderInterface.storePaymentInfo(orderIdBody, statusBody, codeBody));
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

    public MutableLiveData<OrderDetailsResponse> mOrderDetailsResponse() {
        if (mOrderDetailsResponse == null) {
            mOrderDetailsResponse = new MutableLiveData<>();
        }
        return mOrderDetailsResponse;
    }

    public MutableLiveData<PromoCodeResponse> mPromoCodeResponse() {
        if (mPromoCodeResponse == null) {
            mPromoCodeResponse = new MutableLiveData<>();
        }
        return mPromoCodeResponse;
    }

    public MutableLiveData<TransactionHistoryResponse> mmTransactionHistoryResponse() {
        if (mTransactionHistoryResponse == null) {
            mTransactionHistoryResponse = new MutableLiveData<>();
        }
        return mTransactionHistoryResponse;
    }

    public SaveOrderRequest getSaveOrderRequest() {
        return saveOrderRequest;
    }

    public void setSaveOrderRequest(SaveOrderRequest saveOrderRequest) {
        this.saveOrderRequest = saveOrderRequest;
    }
}
