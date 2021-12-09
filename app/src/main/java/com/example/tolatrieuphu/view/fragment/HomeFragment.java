package com.example.tolatrieuphu.view.fragment;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.tolatrieuphu.App;
import com.example.tolatrieuphu.Constants;
import com.example.tolatrieuphu.R;
import com.example.tolatrieuphu.databinding.HomeFragmentBinding;
import com.example.tolatrieuphu.dialogs.CustomAlertDialog;
import com.example.tolatrieuphu.manager.MediaManager;
import com.example.tolatrieuphu.utils.CommonFunction;
import com.example.tolatrieuphu.view.viewmodel.HomeViewModel;

public class HomeFragment extends BaseFragment<HomeFragmentBinding, HomeViewModel> {
    @Override
    protected Class<HomeViewModel> getViewModelClass() {
        return HomeViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initViews() {
        binding.homeInfo.setOnClickListener(v -> handleShowInfoDialog());

        Animation rotation = AnimationUtils.loadAnimation(requireActivity(), R.anim.spinning_anim);
        binding.spinningCircle.startAnimation(rotation);

        binding.btnPlay.setOnClickListener(v -> gotoGameplay());

        App.getInstance().getMediaManager().playBgMusic(MediaManager.HOME_BG_MUSIC);

    }

    private void handleShowInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.home_info_dialog, null);
        builder.setView(v);
        AlertDialog dialog = builder.create();
        v.findViewById(R.id.info_dialog_cancel).setOnClickListener(v1 -> dialog.dismiss());
        dialog.show();
    }

    public void handleShowExitDialog() {
        new CustomAlertDialog(requireActivity(), "Bạn muốn thoát trò chơi?",
                v -> requireActivity().finishAffinity(), null).showDialog();
    }


    private void gotoGameplay() {
        App.getInstance().getMediaManager().stopBgMusic();
        callBack.callBack(Constants.KEY_SHOW_GAMEPLAY, null);
    }
}
