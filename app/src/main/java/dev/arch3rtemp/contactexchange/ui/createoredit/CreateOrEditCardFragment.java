package dev.arch3rtemp.contactexchange.ui.createoredit;

import android.animation.ValueAnimator;
import android.content.Context;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.App;
import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.db.model.Contact;
import dev.arch3rtemp.contactexchange.ui.model.ContactUi;
import dev.arch3rtemp.ui.util.ColorUtils;

public class CreateOrEditCardFragment extends Fragment implements View.OnClickListener, CreateOrEditCardContract.View {

    @Inject
    CreateOrEditCardContract.Presenter presenter;
    private boolean isCreate;
    private int currentColor;

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
        isCreate = requireArguments().getBoolean(ARG_IS_CREATE, false);
        initEditTextFields(view);

        if (!isCreate) {
            var contactId = requireArguments().getInt(ARG_ID, -1);
            presenter.getContactById(contactId);
            ConstraintLayout clColorPalette = view.findViewById(R.id.cl_color_palette);
            TextView tvCardHeader = view.findViewById(R.id.tv_card_header);
            clColorPalette.setVisibility(View.GONE);
            tvCardHeader.setText(getResources().getText(R.string.edit_your_card));
            btnCreateOrSave.setText(getResources().getString(R.string.update));
            btnCreateOrSave.setOnClickListener(this);
        } else {
            initColorsView(view);
            currentColor = ContextCompat.getColor(requireContext(), R.color.light_navy);
            cardBackground = clCreateOrEdit.getBackground();
            tvNavy.setBackgroundResource(R.drawable.shape_selected_card_color_light_navy_bg);

            setBackgroundColorWithAnimationAndRetainShape(ContextCompat.getColor(requireContext(), R.color.light_navy), ContextCompat.getColor(requireContext(), R.color.light_navy), cardBackground);
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

    private void setDataEditTextFields(ContactUi card) {
        etFullName.setText(card.name());
        etCompany.setText(card.job());
        etPosition.setText(card.position());
        etEmail.setText(card.email());
        etPhoneMobile.setText(card.phoneMobile());
        etPhoneOffice.setText(card.phoneOffice());
    }

    @Override
    public void onGetContactById(ContactUi card) {
        Drawable cardBackground = clCreateOrEdit.getBackground();
        setBackgroundColorWithAnimationAndRetainShape(card.color(), card.color(), cardBackground);
        setDataEditTextFields(card);
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
            var color = ContextCompat.getColor(requireContext(), R.color.light_navy);
            tvNavy.setBackgroundResource(R.drawable.shape_selected_card_color_light_navy_bg);
            setBackgroundColorWithAnimationAndRetainShape(currentColor, color, cardBackground);
            currentColor = color;
        } else if (clickedId == R.id.tv_color_aqua_marine) {
            var color = ContextCompat.getColor(requireContext(), R.color.aqua_marine);
            tvAqua.setBackgroundResource(R.drawable.shape_selected_card_color_aqua_marine_bg);
            setBackgroundColorWithAnimationAndRetainShape(currentColor, color, cardBackground);
            currentColor = color;
        } else if (clickedId == R.id.tv_color_ugly_yellow) {
            var color = ContextCompat.getColor(requireContext(), R.color.ugly_yellow);
            tvYellow.setBackgroundResource(R.drawable.shape_selected_card_color_ugly_yellow_bg);
            setBackgroundColorWithAnimationAndRetainShape(currentColor, color, cardBackground);
            currentColor = color;
        } else if (clickedId == R.id.tv_color_shamrock_green) {
            var color = ContextCompat.getColor(requireContext(), R.color.shamrock_green);
            tvGreen.setBackgroundResource(R.drawable.shape_selected_card_color_shamrock_green_bg);
            setBackgroundColorWithAnimationAndRetainShape(currentColor, color, cardBackground);
            currentColor = color;
        } else if (clickedId == R.id.tv_color_black_three) {
            var color = ContextCompat.getColor(requireContext(), R.color.black_three);
            tvBlack.setBackgroundResource(R.drawable.shape_selected_card_color_black_bg);
            setBackgroundColorWithAnimationAndRetainShape(currentColor, color, cardBackground);
            currentColor = color;
        } else if (clickedId == R.id.tv_color_pumpkin) {
            var color = ContextCompat.getColor(requireContext(), R.color.pumpkin);
            tvPumpkin.setBackgroundResource(R.drawable.shape_selected_card_color_pumpkin_bg);
            setBackgroundColorWithAnimationAndRetainShape(currentColor, color, cardBackground);
            currentColor = color;
        } else if (clickedId == R.id.tv_color_darkish_purple) {
            var color = ContextCompat.getColor(requireContext(), R.color.darkish_purple);
            tvPurple.setBackgroundResource(R.drawable.shape_selected_card_color_darkish_purple_bg);
            setBackgroundColorWithAnimationAndRetainShape(currentColor, color, cardBackground);
            currentColor = color;
        } else if (clickedId == R.id.btn_create_or_save) {

            var name = etFullName.getText().toString();
            var company = etCompany.getText().toString();
            var position = etPosition.getText().toString();
            var email = etEmail.getText().toString();
            var phoneMobile = etPhoneMobile.getText().toString();
            var phoneOffice = etPhoneOffice.getText().toString();

            if (isCreate) {
                Contact contact = new Contact(0, name, company, position, email, phoneMobile, phoneOffice, System.currentTimeMillis(), currentColor, true);
                presenter.createContact(contact);
            } else {
                var card = presenter.getCurrentCard();
                card.setName(name);
                card.setJob(company);
                card.setPosition(position);
                card.setEmail(email);
                card.setPhoneMobile(phoneMobile);
                card.setPhoneOffice(phoneOffice);
                presenter.editContact(card);
            }
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

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateUp() {
        getParentFragmentManager().popBackStack();
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

    public static CreateOrEditCardFragment newInstance(int id, boolean isCreate) {
        var args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putBoolean(ARG_IS_CREATE, isCreate);
        CreateOrEditCardFragment fragment = new CreateOrEditCardFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private static final String ARG_ID = "id";
    private static final String ARG_IS_CREATE = "isCreate";
}
