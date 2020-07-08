package com.app.erl.model.state;


import com.app.erl.model.entity.request.LoginRequest;
import com.app.erl.model.entity.request.SignUpRequest;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.ForgotPasswordResponse;
import com.app.erl.model.entity.response.GetMessagesResponse;
import com.app.erl.model.entity.response.ProfileResponse;
import com.app.erl.model.entity.response.SendMessageResponse;
import com.app.erl.model.entity.response.UserResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserAuthenticationServiceInterface {
    @POST("login")
    Observable<UserResponse> login(@Body LoginRequest loginRequest);

    @POST("wn-kkm")
    Observable<UserResponse> signUp(@Body SignUpRequest signUpRequest);

    @Multipart
    @POST("forgot-password")
    Observable<ForgotPasswordResponse> forgotPassword(@Part("email") RequestBody email);

    @Multipart
    @POST("resend-code")
    Observable<BaseResponse> resendCode(@Part("user_id") int user_id, @Part("type") int type);

    @Multipart
    @POST("verify-code")
    Observable<UserResponse> verifyCode(@Part("user_id") RequestBody user_id, @Part("code") RequestBody code, @Part("type") RequestBody type);

    @Multipart
    @POST("verify-mobile")
    Observable<UserResponse> verifyMobile(@Part("user_id") RequestBody user_id, @Part("code") RequestBody code);

    @Multipart
    @POST("reset-password")
    Observable<BaseResponse> resetPassword(@Part("user_id") RequestBody user_id, @Part("password") RequestBody password);

    @Multipart
    @POST("change-password")
    Observable<BaseResponse> changePassword(@Part("cur_password") RequestBody cur_password,@Part("password") RequestBody password);

    @GET("get-profile")
    Observable<ProfileResponse> getProfile();

    @Multipart
    @POST("save-profile")
    Observable<ProfileResponse> saveProfile(@Part("name") RequestBody name, @Part("email") RequestBody email, @Part("phone") RequestBody phone, @Part MultipartBody.Part image);

    @Multipart
    @POST("send-message")
    Observable<SendMessageResponse> sendMessage(@Part("message") RequestBody message, @Part MultipartBody.Part image);

    @Multipart
    @POST("messages")
    Observable<GetMessagesResponse> getMessages(@Part("last_message_id") int last_message_id);

    @Multipart
    @POST("kkm-token")
    Observable<BaseResponse> registerToken(@Part("token") RequestBody token, @Part("device_type") RequestBody device_type);


}
