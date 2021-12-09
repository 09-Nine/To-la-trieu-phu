package com.example.tolatrieuphu.view.act;

import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.example.tolatrieuphu.Constants;
import com.example.tolatrieuphu.R;
import com.example.tolatrieuphu.databinding.ActivityMainBinding;
import com.example.tolatrieuphu.view.fragment.GameplayFragment;
import com.example.tolatrieuphu.view.fragment.HomeFragment;
import com.example.tolatrieuphu.view.fragment.SplashFragment;
import com.example.tolatrieuphu.view.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {


    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected void initViews() {
        SplashFragment splashFragment = new SplashFragment();
        splashFragment.setCallBack(this);
        showFragment(R.id.container_view, splashFragment, false, 0, 0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void callBack(String key, Object data) {
        switch (key) {
            case Constants.KEY_SHOW_HOME:
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setCallBack(this);
                showFragment(R.id.container_view, homeFragment, false, 0, 0);
                break;
            case Constants.KEY_SHOW_GAMEPLAY:
                GameplayFragment gameplayFragment = new GameplayFragment();
                gameplayFragment.setCallBack(this);
                showFragment(R.id.container_view, gameplayFragment, false, 0, 0);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.container_view);
        if (currentFrag instanceof HomeFragment) {
            ((HomeFragment) currentFrag).handleShowExitDialog();
        }
    }
}