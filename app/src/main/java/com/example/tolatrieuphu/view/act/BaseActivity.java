package com.example.tolatrieuphu.view.act;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tolatrieuphu.interfaces.OnActionCallBack;
import com.example.tolatrieuphu.R;

public abstract class BaseActivity<BD extends ViewDataBinding,VM extends ViewModel> extends AppCompatActivity
        implements OnActionCallBack {

    protected BD binding;
    protected VM viewModel;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(getViewModelClass());
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        initViews();
    }

    protected abstract Class<VM> getViewModelClass();

    protected abstract void initViews();

    protected abstract int getLayoutId();

    protected void showFragment(int layoutID, Fragment fragment, boolean addToBackStack, int anim_start, int anim_end) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (anim_end != 0 && anim_start != 0) {
            transaction.setCustomAnimations(anim_start, anim_end);
        }
        transaction.replace(R.id.container_view, fragment);
        if (addToBackStack) {
            transaction.addToBackStack("add");
        }

        transaction.commit();
    }
}
