package com.example.tolatrieuphu.view.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tolatrieuphu.Constants;
import com.example.tolatrieuphu.R;
import com.example.tolatrieuphu.adapters.HighScoreAdapter;
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
        mViewModel.getData();
        binding.highScoreRecycleView.setHasFixedSize(true);
        binding.highScoreRecycleView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        HighScoreAdapter highScoreAdapter = new HighScoreAdapter(mViewModel.getHighScorePlayer());
        binding.highScoreRecycleView.setAdapter(highScoreAdapter);
        binding.returnHome.setOnClickListener(v -> gotoHome());
    }

    private void gotoHome() {
        callBack.callBack(Constants.KEY_SHOW_HOME, null);
    }
}
