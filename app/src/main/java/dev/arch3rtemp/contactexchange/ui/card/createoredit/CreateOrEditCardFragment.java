package dev.arch3rtemp.contactexchange.ui.card.createoredit;

import static dev.arch3rtemp.contactexchange.ui.card.CardActivity.ID;
import static dev.arch3rtemp.contactexchange.ui.card.CardActivity.IS_CREATE;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
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

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.App;
import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.contactexchange.ui.MainActivity;
import dev.arch3rtemp.ui.util.ColorUtils;

public class CreateOrEditCardFragment extends Fragment implements View.OnClickListener, CreateOrEditCardContract.View {

    private ConstraintLayout clCreateOrEdit;
    private Button btnCreateOrSave;
    private TextView tvNavy;
    private TextView tvAqua;
    private TextView tvYellow;
    private TextView tvGreen;
    private TextView tvBlack;
    private TextView tvPumpkin;
    private TextView tvPurple;
    private Drawable cardBackground;
    private EditText etFullName;
    private EditText etCompany;
    private EditText etPosition;
    private EditText etEmail;
    private EditText etPhoneMobile;
    private EditText etPhoneOffice;
    private boolean isCreate;
    private int color;
    private int currentColor;
    @Inject
    CreateOrEditCardContract.Presenter presenter;
    private Contact card;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        ((App) requireActivity()
                .getApplication())
                .getAppComponent()
                .activityComponent()
                .create(requireActivity())
                .fragmentComponent()
                .create()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_create_or_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
    }

    public void initUI(View view) {
        presenter.onCreate(this);
        clCreateOrEdit = view.findViewById(R.id.cl_create_or_edit);
        btnCreateOrSave = view.findViewById(R.id.btn_create_or_save);
        isCreate = requireArguments().getBoolean(IS_CREATE, false);
        initEditTextFields(view);

        if (!isCreate) {
            var contactId = requireArguments().getInt(ID, -1);
            presenter.getContactById(contactId);
            ConstraintLayout clColorPalette = view.findViewById(R.id.cl_color_palette);
            TextView tvCardHeader = view.findViewById(R.id.tv_card_header);
            clColorPalette.setVisibility(View.GONE);
            tvCardHeader.setText(getResources().getText(R.string.edit_your_card));
            btnCreateOrSave.setText(getResources().getString(R.string.update));
            btnCreateOrSave.setOnClickListener(this);
        } else {
            initColorsView(view);
            currentColor = getResources().getColor(R.color.light_navy);
            color = currentColor;
            cardBackground = clCreateOrEdit.getBackground();
            tvNavy.setBackgroundResource(R.drawable.shape_selected_card_color_light_navy_bg);

            setBackgroundColorWithAnimationAndRetainShape(getResources().getColor(R.color.light_navy), getResources().getColor(R.color.light_navy), cardBackground);
            setListeners();
        }
    }

    private void initEditTextFields(View view) {
        etFullName = view.findViewById(R.id.et_full_name);
        etCompany = view.findViewById(R.id.et_company);
        etPosition = view.findViewById(R.id.et_position);
        etEmail = view.findViewById(R.id.et_email);
        etPhoneMobile = view.findViewById(R.id.et_tel);
        etPhoneOffice = view.findViewById(R.id.et_tel_office);
    }

    private void setDataEditTextFields() {
        etFullName.setText(card.getName());
        etCompany.setText(card.getJob());
        etPosition.setText(card.getPosition());
        etEmail.setText(card.getEmail());
        etPhoneMobile.setText(card.getPhoneMobile());
        etPhoneOffice.setText(card.getPhoneOffice());
    }

    @Override
    public void onGetContactById(Contact card) {
        this.card = card;
        Drawable cardBackground = clCreateOrEdit.getBackground();
        setBackgroundColorWithAnimationAndRetainShape(card.getColor(), card.getColor(), cardBackground);
        setDataEditTextFields();
    }

    private void initColorsView(View view) {

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
        if (isCreate) {
            defaultColorsView();
        }

        int clickedId = v.getId();

        if (clickedId == R.id.tv_color_light_navy) {
            color = getResources().getColor(R.color.light_navy);
            tvNavy.setBackgroundResource(R.drawable.shape_selected_card_color_light_navy_bg);
            setBackgroundColorWithAnimationAndRetainShape(currentColor, color, cardBackground);
            currentColor = color;
        } else if (clickedId == R.id.tv_color_aqua_marine) {
            color = getResources().getColor(R.color.aqua_marine);
            tvAqua.setBackgroundResource(R.drawable.shape_selected_card_color_aqua_marine_bg);
            setBackgroundColorWithAnimationAndRetainShape(currentColor, color, cardBackground);
            currentColor = color;
        } else if (clickedId == R.id.tv_color_ugly_yellow) {
            color = getResources().getColor(R.color.ugly_yellow);
            tvYellow.setBackgroundResource(R.drawable.shape_selected_card_color_ugly_yellow_bg);
            setBackgroundColorWithAnimationAndRetainShape(currentColor, color, cardBackground);
            currentColor = color;
        } else if (clickedId == R.id.tv_color_shamrock_green) {
            color = getResources().getColor(R.color.shamrock_green);
            tvGreen.setBackgroundResource(R.drawable.shape_selected_card_color_shamrock_green_bg);
            setBackgroundColorWithAnimationAndRetainShape(currentColor, color, cardBackground);
            currentColor = color;
        } else if (clickedId == R.id.tv_color_black_three) {
            color = getResources().getColor(R.color.black_three);
            tvBlack.setBackgroundResource(R.drawable.shape_selected_card_color_black_bg);
            setBackgroundColorWithAnimationAndRetainShape(currentColor, color, cardBackground);
            currentColor = color;
        } else if (clickedId == R.id.tv_color_pumpkin) {
            color = getResources().getColor(R.color.pumpkin);
            tvPumpkin.setBackgroundResource(R.drawable.shape_selected_card_color_pumpkin_bg);
            setBackgroundColorWithAnimationAndRetainShape(currentColor, color, cardBackground);
            currentColor = color;
        } else if (clickedId == R.id.tv_color_darkish_purple) {
            color = getResources().getColor(R.color.darkish_purple);
            tvPurple.setBackgroundResource(R.drawable.shape_selected_card_color_darkish_purple_bg);
            setBackgroundColorWithAnimationAndRetainShape(currentColor, color, cardBackground);
            currentColor = color;
        } else if (clickedId == R.id.btn_create_or_save) {

            if (isEmptyField(etFullName))
                return;
            if (isEmptyField(etCompany))
                return;
            if (isEmptyField(etPosition))
                return;
            if (isEmptyField(etEmail))
                return;
            if (isEmptyField(etPhoneMobile))
                return;
            if (isEmptyField(etPhoneOffice))
                return;

            var name = etFullName.getText().toString();

            var company = etCompany.getText().toString();
            var position = etPosition.getText().toString();
            var email = etEmail.getText().toString();
            var phoneMobile = etPhoneMobile.getText().toString();
            var phoneOffice = etPhoneOffice.getText().toString();

            if (isCreate) {
                Contact contact = new Contact(0, name, company, position, email, phoneMobile, phoneOffice, System.currentTimeMillis(), color, true);
                presenter.createContact(contact);
            } else {
                card.setName(name);
                card.setJob(company);
                card.setPosition(position);
                card.setEmail(email);
                card.setPhoneMobile(phoneMobile);
                card.setPhoneOffice(phoneOffice);
                presenter.editContact(card);
            }

            startHomeActivity();
        }
    }

    private void defaultColorsView() {

        tvNavy.setBackgroundResource(R.drawable.shape_default_card_color_light_navy_bg);

        tvAqua.setBackgroundResource(R.drawable.shape_default_card_color_aqua_marine_bg);

        tvYellow.setBackgroundResource(R.drawable.shape_default_card_color_ugly_yellow_bg);

        tvGreen.setBackgroundResource(R.drawable.shape_default_card_color_shamrock_green_bg);

        tvBlack.setBackgroundResource(R.drawable.shape_default_card_color_black_bg);

        tvPumpkin.setBackgroundResource(R.drawable.shape_default_card_color_pumpkin_bg);

        tvPurple.setBackgroundResource(R.drawable.shape_default_card_color_darkish_purple_bg);

    }

    private boolean isEmptyField(EditText editText) {
        boolean result = editText.getText().toString().isEmpty();
        if (result)
            showToastMessage(getString(R.string.msg_all_fields_required));
        return result;
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void startHomeActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        requireActivity().startActivity(intent);
    }

    private void setBackgroundColorWithAnimationAndRetainShape(final int currentColor, final int finalColor, final Drawable background) {

        ValueAnimator valueAnimator = ValueAnimator.ofArgb(currentColor, finalColor);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                background.setColorFilter(ColorUtils.createSrcInColorFilter((Integer) valueAnimator.getAnimatedValue()));
            }
        });
        valueAnimator.start();
    }
}
