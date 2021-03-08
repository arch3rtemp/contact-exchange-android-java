package com.example.contactsexchangejava.ui.fragments;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.contactsexchangejava.R;

public class CreateOrEditCardFragment extends Fragment implements View.OnClickListener {


    private boolean isCreate;
    private View view;
    private ConstraintLayout clColorPalette;
    private ConstraintLayout clCreateOrEdit;
    private TextView tvCardHeader;
    private Button btnCreateOrSave;
    private TextView tvNavy;
    private TextView tvAqua;
    private TextView tvYellow;
    private TextView tvGreen;
    private TextView tvBlack;
    private TextView tvPumpkin;
    private TextView tvPurple;


    public CreateOrEditCardFragment(boolean isCreate) {
        this.isCreate = isCreate;
    }

//    public boolean isCreate() {
//        return isCreate;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_card_create_or_edit, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    public void initUI() {
        clColorPalette = view.findViewById(R.id.cl_color_palette);
        clCreateOrEdit = view.findViewById(R.id.cl_create_or_edit);
        tvCardHeader = view.findViewById(R.id.tv_card_header);
        btnCreateOrSave = view.findViewById(R.id.btn_create_or_save);

        if (!isCreate) {

            clColorPalette.setVisibility(View.GONE);
            tvCardHeader.setText(getResources().getText(R.string.edit_your_card));
            btnCreateOrSave.setText(getResources().getString(R.string.save));

        } else {

            initColorsView();
            tvNavy.setBackgroundResource(R.drawable.selected_card_color_light_navy_shape_bg);
            setListeners();
        }
    }

    private void initColorsView() {

        tvNavy = view.findViewById(R.id.tv_color_light_navy);

        tvAqua = view.findViewById(R.id.tv_color_aqua_marine);

        tvYellow = view.findViewById(R.id.tv_color_ugly_yellow);

        tvGreen = view.findViewById(R.id.tv_color_shamrock_green);

        tvBlack = view.findViewById(R.id.tv_color_black_three);

        tvPumpkin = view.findViewById(R.id.tv_color_pumpkin);

        tvPurple = view.findViewById(R.id.tv_color_darkish_purple);
    }

    private void setListeners() {
        tvNavy.setOnClickListener(this);
        tvAqua.setOnClickListener(this);
        tvYellow.setOnClickListener(this);
        tvGreen.setOnClickListener(this);
        tvBlack.setOnClickListener(this);
        tvPumpkin.setOnClickListener(this);
        tvPurple.setOnClickListener(this);
    }

    private void setBackgroundColorAndRetainShape(final int color, final Drawable background) {
        if (background instanceof ShapeDrawable)
            ((ShapeDrawable) background.mutate()).getPaint().setColor(color);
        else if (background instanceof GradientDrawable)
            ((GradientDrawable) background.mutate()).setColor(color);
        else if (background instanceof ColorDrawable)
            ((ColorDrawable) background).setColor(color);
        else
            Log.w("TAG", "Not a valid background type");
    }

    @Override
    public void onClick(View v) {
        defaultColorsView();

        Drawable cardBackground = clCreateOrEdit.getBackground();

        int id = v.getId();

        if (id == R.id.tv_color_light_navy) {
            tvNavy.setBackgroundResource(R.drawable.selected_card_color_light_navy_shape_bg);
            setBackgroundColorAndRetainShape(getResources().getColor(R.color.light_navy), cardBackground);
        } else if (id == R.id.tv_color_aqua_marine) {
            tvAqua.setBackgroundResource(R.drawable.selected_card_color_aqua_marine_shape_bg);
            setBackgroundColorAndRetainShape(getResources().getColor(R.color.aqua_marine), cardBackground);
        } else if (id == R.id.tv_color_ugly_yellow) {
            tvYellow.setBackgroundResource(R.drawable.selected_card_color_ugly_yellow_shape_bg);
            setBackgroundColorAndRetainShape(getResources().getColor(R.color.ugly_yellow), cardBackground);
        } else if (id == R.id.tv_color_shamrock_green) {
            tvGreen.setBackgroundResource(R.drawable.selected_card_color_shamrock_green_shape_bg);
            setBackgroundColorAndRetainShape(getResources().getColor(R.color.shamrock_green), cardBackground);
        } else if (id == R.id.tv_color_black_three) {
            tvBlack.setBackgroundResource(R.drawable.selected_card_color_black_shape_bg);
            setBackgroundColorAndRetainShape(getResources().getColor(R.color.black_three), cardBackground);
        } else if (id == R.id.tv_color_pumpkin) {
            tvPumpkin.setBackgroundResource(R.drawable.selected_card_color_pumpkin_shape_bg);
            setBackgroundColorAndRetainShape(getResources().getColor(R.color.pumpkin), cardBackground);
        } else if (id == R.id.tv_color_darkish_purple) {
            tvPurple.setBackgroundResource(R.drawable.selected_card_color_darkish_purple_shape_bg);
            setBackgroundColorAndRetainShape(getResources().getColor(R.color.darkish_purple), cardBackground);
        }
    }

    private void defaultColorsView() {

        tvNavy.setBackgroundResource(R.drawable.default_card_color_light_navy_shape_bg);


        tvAqua.setBackgroundResource(R.drawable.default_card_color_aqua_marine_shape_bg);


        tvYellow.setBackgroundResource(R.drawable.default_card_color_ugly_yellow_shape_bg);


        tvGreen.setBackgroundResource(R.drawable.default_card_color_shamrock_green_shape_bg);


        tvBlack.setBackgroundResource(R.drawable.default_card_color_black_shape_bg);


        tvPumpkin.setBackgroundResource(R.drawable.default_card_color_pumpkin_shape_bg);


        tvPurple.setBackgroundResource(R.drawable.default_card_color_darkish_purple_shape_bg);

    }
}
