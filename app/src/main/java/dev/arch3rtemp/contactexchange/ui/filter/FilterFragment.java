package dev.arch3rtemp.contactexchange.ui.filter;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.App;
import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.ui.card.CardActivity;
import dev.arch3rtemp.contactexchange.ui.card.FragmentType;
import dev.arch3rtemp.contactexchange.ui.home.adapter.ContactRecyclerAdapter;
import dev.arch3rtemp.contactexchange.ui.model.ContactUi;

public class FilterFragment extends Fragment implements FilterContract.View {

    private TextInputEditText etSearch;
    private ImageView ivSearch;
    private RecyclerView rvContacts;
    private ContactRecyclerAdapter rvContactAdapter;
    @Inject
    FilterContract.Presenter presenter;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        setListeners();
        presenter.onCreate(this);
        presenter.getContacts();
    }

    private void initUI(View view) {
        ivSearch = view.findViewById(R.id.iv_search);
        etSearch = view.findViewById(R.id.et_search);
        etSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
        rvContacts = view.findViewById(R.id.rv_contacts);
        createContactRecyclerView();
    }

    private void createContactRecyclerView() {
        rvContactAdapter = new ContactRecyclerAdapter(this::onContactClick);
        rvContacts.setAdapter(rvContactAdapter);
        rvContactAdapter.setDeleteClickListener(this::onDeleteClick);
    }

    private void setListeners() {
        ivSearch.setOnClickListener(v -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                var filtered = presenter.filterContacts(s.toString());
                rvContactAdapter.updateItems(filtered);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void onContactClick(ContactUi contact) {
        CardActivity.start(requireContext(), contact.id(), contact.isMy(), FragmentType.CARD);
    }

    public void onDeleteClick(ContactUi contact) {
        presenter.deleteContact(contact.id());
    }

    @Override
    public void onGetContacts(List<ContactUi> contacts) {
        rvContactAdapter.updateItems(contacts);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
