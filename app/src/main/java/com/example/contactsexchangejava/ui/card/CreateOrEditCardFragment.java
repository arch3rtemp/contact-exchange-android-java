package com.example.contactsexchangejava.ui.card;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.contactsexchangejava.R;
import com.example.contactsexchangejava.db.AppDatabase;
import com.example.contactsexchangejava.db.DataManager;
import com.example.contactsexchangejava.db.models.Contact;

import java.util.Objects;

public class CreateOrEditCardFragment extends Fragment implements View.OnClickListener, ICreateOrEditCardContract.View {


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
    Drawable cardBackground;

    private DataManager dataManager;
    private EditText etFullName;
    private EditText etCompany;
    private EditText etPosition;
    private EditText etEmail;
    private EditText etPhoneMobile;
    private EditText etPhoneOffice;
    private int color;
    private boolean isMe;
    String firstName;
    String lastName;
    String company;
    String position;
    String email;
    String phoneMobile;
    String phoneOffice;
    ICreateOrEditCardContract.Presenter presenter;




    public CreateOrEditCardFragment(boolean isCreate) {
        this.isCreate = isCreate;
    }

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
        setPresenter(new CreateOrEditCardPresenter(this));
        presenter.onViewCreated(Objects.requireNonNull(getActivity()));
        clColorPalette = view.findViewById(R.id.cl_color_palette);
        clCreateOrEdit = view.findViewById(R.id.cl_create_or_edit);
        tvCardHeader = view.findViewById(R.id.tv_card_header);
        btnCreateOrSave = view.findViewById(R.id.btn_create_or_save);
        initEditTextFields();

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

    private void initEditTextFields() {
        etFullName = view.findViewById(R.id.et_full_name);
        etCompany = view.findViewById(R.id.et_company);
        etPosition = view.findViewById(R.id.et_position);
        etEmail = view.findViewById(R.id.et_email);
        etPhoneMobile = view.findViewById(R.id.tv_phone_mobile);
        etPhoneOffice = view.findViewById(R.id.tv_phone_office);
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
        btnCreateOrSave.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        defaultColorsView();

        cardBackground = clCreateOrEdit.getBackground();

        int id = v.getId();

        if (id == R.id.tv_color_light_navy) {
            tvNavy.setBackgroundResource(R.drawable.selected_card_color_light_navy_shape_bg);
            presenter.setBackgroundColorAndRetainShape(getResources().getColor(R.color.light_navy), cardBackground);
        } else if (id == R.id.tv_color_aqua_marine) {
            tvAqua.setBackgroundResource(R.drawable.selected_card_color_aqua_marine_shape_bg);
            presenter.setBackgroundColorAndRetainShape(getResources().getColor(R.color.aqua_marine), cardBackground);
        } else if (id == R.id.tv_color_ugly_yellow) {
            tvYellow.setBackgroundResource(R.drawable.selected_card_color_ugly_yellow_shape_bg);
            presenter.setBackgroundColorAndRetainShape(getResources().getColor(R.color.ugly_yellow), cardBackground);
        } else if (id == R.id.tv_color_shamrock_green) {
            tvGreen.setBackgroundResource(R.drawable.selected_card_color_shamrock_green_shape_bg);
            presenter.setBackgroundColorAndRetainShape(getResources().getColor(R.color.shamrock_green), cardBackground);
        } else if (id == R.id.tv_color_black_three) {
            tvBlack.setBackgroundResource(R.drawable.selected_card_color_black_shape_bg);
            presenter.setBackgroundColorAndRetainShape(getResources().getColor(R.color.black_three), cardBackground);
        } else if (id == R.id.tv_color_pumpkin) {
            tvPumpkin.setBackgroundResource(R.drawable.selected_card_color_pumpkin_shape_bg);
            presenter.setBackgroundColorAndRetainShape(getResources().getColor(R.color.pumpkin), cardBackground);
        } else if (id == R.id.tv_color_darkish_purple) {
            tvPurple.setBackgroundResource(R.drawable.selected_card_color_darkish_purple_shape_bg);
            presenter.setBackgroundColorAndRetainShape(getResources().getColor(R.color.darkish_purple), cardBackground);
        } else if (id == R.id.btn_create_or_save) {

            color = Color.TRANSPARENT;
            if (cardBackground instanceof ColorDrawable)
                color = ((ColorDrawable) cardBackground).getColor();

            firstName = etFullName.toString();
            lastName = "";
            if (firstName.contains(" ")) {
                lastName = firstName.substring(firstName.indexOf(" "), firstName.length());
                firstName = firstName.substring(0, firstName.indexOf(" "));
            }

            company = etCompany.toString();
            position = etPosition.toString();
            email = etEmail.toString();
            phoneMobile = etPhoneMobile.toString();
            phoneOffice = etPhoneOffice.toString();

            if (isCreate) {
                dataManager.createContact(new Contact(
                        firstName, lastName, company, position, email, phoneMobile, phoneOffice, color, isMe
                ));
                showToastMessage("Contact created");
            }

            else
                dataManager.editContact(new Contact());


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


    public void editContact() {
        showToastMessage("Contact edited");
    }



    private void showToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(ICreateOrEditCardContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
