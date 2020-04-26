package com.app.erl.injection.component;

import com.app.erl.injection.module.DashBoardModule;
import com.app.erl.injection.module.ManageAddressModule;
import com.app.erl.injection.module.UserAuthenticationModule;
import com.app.erl.injection.scope.UserScope;
import com.app.erl.model.entity.dashboard.DashboardModel;
import com.app.erl.viewModel.DashBoardViewModel;
import com.app.erl.viewModel.ManageAddressViewModel;
import com.app.erl.viewModel.UserAuthenticationViewModel;

import dagger.Component;

@UserScope
@Component(dependencies = NetworkComponent.class
        , modules = {UserAuthenticationModule.class, DashBoardModule.class, ManageAddressModule.class})

public interface ServiceComponent {
    void inject(UserAuthenticationViewModel userAuthenticationViewModel);

    void inject(DashBoardViewModel dashboardModel);

    void inject(ManageAddressViewModel manageAddressViewModel);
}
