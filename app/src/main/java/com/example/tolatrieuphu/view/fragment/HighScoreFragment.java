package com.example.tolatrieuphu.view.fragment;

import com.example.tolatrieuphu.R;
import com.example.tolatrieuphu.databinding.HighScoreFragmentBinding;
import com.example.tolatrieuphu.view.viewmodel.HighScoreViewModel;

public class HighScoreFragment extends BaseFragment<HighScoreFragmentBinding, HighScoreViewModel> {
    @Override
    protected Class<HighScoreViewModel> getViewModelClass() {
        return HighScoreViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.high_score_fragment;
    }

    @Override
    protected void initViews() {

    }
}
