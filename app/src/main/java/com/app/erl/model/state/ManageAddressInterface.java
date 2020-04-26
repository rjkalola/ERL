package com.app.erl.model.state;


import com.app.erl.model.entity.response.AddressResourcesResponse;
import com.app.erl.model.entity.response.ClientDashBoardResponse;
import com.app.erl.model.entity.response.ServiceItemsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface ManageAddressInterface {
    @GET("address-resources")
    Observable<AddressResourcesResponse> getAddressResources();

//    @Multipart
//    @POST("get-service-item")
//    Observable<ServiceItemsResponse> getServiceItems();
}
