package com.app.erl.injection.component;

import com.app.erl.injection.module.UserAuthenticationModule;
import com.app.erl.injection.scope.UserScope;

import dagger.Component;

@UserScope
@Component(dependencies = NetworkComponent.class
        , modules = {UserAuthenticationModule.class})

public interface ServiceComponent {
//    void inject(LoginViewModel loginViewModel);


}
