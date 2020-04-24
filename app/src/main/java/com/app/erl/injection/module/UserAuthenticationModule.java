package com.app.erl.injection.module;



import com.app.erl.injection.scope.UserScope;
import com.app.erl.model.state.UserAuthenticationServiceInterface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 */
@Module
public class UserAuthenticationModule {
    @UserScope
    @Provides
    public UserAuthenticationServiceInterface provideUserAuthenticationService(Retrofit retrofit){
        return retrofit.create(UserAuthenticationServiceInterface.class);
    }

}
