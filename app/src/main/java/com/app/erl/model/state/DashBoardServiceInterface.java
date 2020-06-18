package com.app.erl.model.state;


import com.app.erl.model.entity.response.ClientDashBoardResponse;
import com.app.erl.model.entity.response.OurServicesResponse;
import com.app.erl.model.entity.response.PrivacyPolicyResponse;
import com.app.erl.model.entity.response.ServiceItemsResponse;
import com.app.erl.model.entity.response.StoreLocatorResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface DashBoardServiceInterface {
    @GET("client-dashboard")
    Observable<ClientDashBoardResponse> getClientDashboard();

    @GET("get-service-items")
    Observable<ServiceItemsResponse> getServiceItems();

    @GET("terms-condition")
    Observable<PrivacyPolicyResponse> getTermsConditions();

    @GET("privacy-policy")
    Observable<PrivacyPolicyResponse> getPrivacyPolicy();

    @GET("our-services")
    Observable<OurServicesResponse> getOurServices();

    @GET("store-locators")
    Observable<StoreLocatorResponse> getStoreLocator();
}
