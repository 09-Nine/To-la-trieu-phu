package com.example.tolatrieuphu.view.fragment;


import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.tolatrieuphu.App;
import com.example.tolatrieuphu.Constants;
import com.example.tolatrieuphu.R;
import com.example.tolatrieuphu.databinding.HomeFragmentBinding;
import com.example.tolatrieuphu.dialogs.CustomAlertDialog;
import com.example.tolatrieuphu.dialogs.InfoDialog;
import com.example.tolatrieuphu.manager.MediaManager;
import com.example.tolatrieuphu.view.viewmodel.HomeViewModel;

public class HomeFragment extends BaseFragment<HomeFragmentBinding, HomeViewModel> {
    private MediaManager mediaManager;

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
        mediaManager = App.getInstance().getMediaManager();
        InfoDialog infoDialog = new InfoDialog(requireActivity());
        binding.homeInfo.setOnClickListener(v -> infoDialog.showDialog());

        Animation rotation = AnimationUtils.loadAnimation(requireActivity(), R.anim.spinning_anim);
        binding.spinningCircle.startAnimation(rotation);

        binding.btnPlay.setOnClickListener(v -> gotoGameplay());
        binding.btnHighScore.setOnClickListener(v -> gotoHighScore());

        mediaManager.playBgMusic(MediaManager.HOME_BG_MUSIC);

        binding.soundSetting.setOnClickListener(v -> handleSound());


    }

    public void handleShowExitDialog() {
        new CustomAlertDialog(requireActivity(), "Bạn muốn thoát trò chơi?",
                v -> requireActivity().finishAffinity(), null).showDialog();
    }

    private void handleSound() {
        if (mediaManager.isMute()) {
            mediaManager.setMute(false);
            binding.soundSetting.setImageResource(R.drawable.unmute);
        } else {
           mediaManager.setMute(true);
            binding.soundSetting.setImageResource(R.drawable.mute);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaManager.pauseBg();
    }

    @Override
    public void onResume() {
        super.onResume();
        mediaManager.resumeBg();
    }

    private void gotoGameplay() {
        App.getInstance().getMediaManager().stopBgMusic();
        callBack.callBack(Constants.KEY_SHOW_GAMEPLAY, null);
    }

    private void gotoHighScore() {
        callBack.callBack(Constants.KEY_SHOW_HIGH_SCORE, null);
    }
}
