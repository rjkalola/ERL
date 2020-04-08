package com.app.erl.util;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LoginViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private ResourceProvider resourceProvider;


    public LoginViewModelFactory(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
//        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
//            return (T) new LoginViewModel(resourceProvider);
//        }else if (modelClass.isAssignableFrom(SignUpViewModel.class)) {
//            return (T) new SignUpViewModel(resourceProvider);
//        }else if (modelClass.isAssignableFrom(ManageProjectViewModel.class)) {
//            return (T) new ManageProjectViewModel(resourceProvider);
//        }else if (modelClass.isAssignableFrom(ManageProfileViewModel.class)) {
//            return (T) new ManageProfileViewModel(resourceProvider);
//        }else if (modelClass.isAssignableFrom(DashBoardViewModel.class)) {
//            return (T) new DashBoardViewModel(resourceProvider);
//        }else if (modelClass.isAssignableFrom(FilterModuleViewModel.class)) {
//            return (T) new FilterModuleViewModel(resourceProvider);
//        }else if (modelClass.isAssignableFrom(HealthSafetyViewModel.class)) {
//            return (T) new HealthSafetyViewModel(resourceProvider);
//        }else if (modelClass.isAssignableFrom(GoodsRequestViewModel.class)) {
//            return (T) new GoodsRequestViewModel(resourceProvider);
//        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}