package com.app.erl.model.state;


import com.app.erl.model.entity.response.ClientDashBoardResponse;
import com.app.erl.model.entity.response.ServiceItemsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface DashBoardServiceInterface {
    @GET("client-dashboard")
    Observable<ClientDashBoardResponse> getClientDashboard();

    @Multipart
    @POST("get-service-item")
    Observable<ServiceItemsResponse> getServiceItems();
}
