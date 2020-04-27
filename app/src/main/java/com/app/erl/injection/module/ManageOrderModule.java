package com.app.erl.injection.module;


import com.app.erl.injection.scope.UserScope;
import com.app.erl.model.state.ManageAddressInterface;
import com.app.erl.model.state.ManageOrderInterface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ManageOrderModule {
    @UserScope
    @Provides
    public ManageOrderInterface provideUserAuthenticationService(Retrofit retrofit) {
        return retrofit.create(ManageOrderInterface.class);
    }

}
