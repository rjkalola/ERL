package com.app.erl.model.state;


import com.app.erl.model.entity.request.SaveOrderRequest;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.OrderDetailsResponse;
import com.app.erl.model.entity.response.OrderListResponse;
import com.app.erl.model.entity.response.OrderResourcesResponse;
import com.app.erl.model.entity.response.PromoCodeResponse;
import com.app.erl.model.entity.response.TransactionHistoryResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ManageOrderInterface {
    @GET("order-resources")
    Observable<OrderResourcesResponse> getOrderResources();

    @POST("place-order")
    Observable<BaseResponse> placeOrder(@Body SaveOrderRequest saveOrderRequest);

    @Multipart
    @POST("client-orders")
    Observable<OrderListResponse> clientOrders(@Part("limit") int limit, @Part("offset") int offset);

    @Multipart
    @POST("client-cancel-order")
    Observable<BaseResponse> clientCancelOrders(@Part("id") int id);

    @Multipart
    @POST("client-order-detail")
    Observable<OrderDetailsResponse> clientOrderDetails(@Part("order_id") int order_id);

    @Multipart
    @POST("check-promocode")
    Observable<PromoCodeResponse> checkPromoCode(@Part("code") RequestBody code);

    @Multipart
    @POST("add-coupon")
    Observable<PromoCodeResponse> addCouponCode(@Part("code") RequestBody code);

    @GET("transaction-history")
    Observable<TransactionHistoryResponse> getTransactionHistory();

    @Multipart
    @POST("store-payment-info")
    Observable<BaseResponse> storePaymentInfo(@Part("order_id") RequestBody order_id
            , @Part("status") RequestBody status
            , @Part("code") RequestBody code);
}
