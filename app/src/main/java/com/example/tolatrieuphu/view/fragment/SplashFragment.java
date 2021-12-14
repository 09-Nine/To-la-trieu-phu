package com.example.tolatrieuphu.view.fragment;

import android.os.Handler;

import com.example.tolatrieuphu.Constants;
import com.example.tolatrieuphu.R;
import com.example.tolatrieuphu.databinding.SplashFragmentBinding;
import com.example.tolatrieuphu.view.viewmodel.SplashViewModel;

public class SplashFragment extends BaseFragment<SplashFragmentBinding, SplashViewModel> {
    @Override
    protected Class<SplashViewModel> getViewModelClass() {
        return SplashViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.splash_fragment;
    }

    @Override
    protected void initViews() {
        new Handler().postDelayed(this::gotoHome, 2000);
    }

    private void gotoHome() {
        callBack.callBack(Constants.KEY_SHOW_HOME, null);
    }
}
