package com.app.erl.model.state;


import com.app.erl.model.entity.request.LoginRequest;
import com.app.erl.model.entity.request.SignUpRequest;
import com.app.erl.model.entity.response.BaseResponse;
import com.app.erl.model.entity.response.ForgotPasswordResponse;
import com.app.erl.model.entity.response.UserResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
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
    Observable<BaseResponse> resendCode(@Part("user_id") int user_id);

    @Multipart
    @POST("verify-code")
    Observable<BaseResponse> verifyCode(@Part("user_id") RequestBody user_id, @Part("code") RequestBody code);

    @Multipart
    @POST("reset-password")
    Observable<BaseResponse> resetPassword(@Part("user_id") RequestBody user_id, @Part("password") RequestBody code);
}
