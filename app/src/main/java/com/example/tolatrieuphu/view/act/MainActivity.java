package com.example.tolatrieuphu.view.act;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tolatrieuphu.R;
import com.example.tolatrieuphu.databinding.ActivityMainBinding;
import com.example.tolatrieuphu.interfaces.OnActionCallBack;
import com.example.tolatrieuphu.view.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {


    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void callBack(String key, Object data) {

    }
}