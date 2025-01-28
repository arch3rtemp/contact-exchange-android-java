package dev.arch3rtemp.contactexchange.ui.card.result;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dev.arch3rtemp.contactexchange.R;

public class ResultFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deleted, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        checkedSignScaleUp(view);
    }

    private void initUI(View view) {
        LinearLayout llDeleted = view.findViewById(R.id.ll_deleted);
        llDeleted.setClipChildren(false);
        llDeleted.setClipToPadding(false);

    }

    private void checkedSignScaleUp(View view) {
        ImageView ivChecked = view.findViewById(R.id.iv_checked);

        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(ivChecked, View.SCALE_X, 1f, 1.1f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(ivChecked, View.SCALE_Y, 1f, 1.1f);
        scaleUpX.setDuration(300)
                .setStartDelay(800);
        scaleUpY.setDuration(300)
                .setStartDelay(800);


        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(ivChecked, View.SCALE_X, 1.1f, 1.0f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(ivChecked, View.SCALE_Y, 1.1f, 1.0f);
        scaleDownX.setDuration(300)
                .setStartDelay(1700);
        scaleDownY.setDuration(300)
                .setStartDelay(1700);


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleUpX).with(scaleUpY).with(scaleDownX).with(scaleDownY);
        animatorSet.start();
    }
}
