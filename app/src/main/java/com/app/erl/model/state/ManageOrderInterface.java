package com.app.erl.model.state;


import com.app.erl.model.entity.request.SaveAddressRequest;
import com.app.erl.model.entity.request.SaveOrderRequest;
import com.app.erl.model.entity.response.AddressResourcesResponse;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.OrderDetailsResponse;
import com.app.erl.model.entity.response.OrderListResponse;
import com.app.erl.model.entity.response.OrderResourcesResponse;

import io.reactivex.Observable;
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


}
