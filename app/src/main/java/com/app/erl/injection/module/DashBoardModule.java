package com.app.erl.injection.module;



import com.app.erl.injection.scope.UserScope;
import com.app.erl.model.state.DashBoardServiceInterface;
import com.app.erl.model.state.UserAuthenticationServiceInterface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class DashBoardModule {
    @UserScope
    @Provides
    public DashBoardServiceInterface provideUserAuthenticationService(Retrofit retrofit){
        return retrofit.create(DashBoardServiceInterface.class);
    }

}
