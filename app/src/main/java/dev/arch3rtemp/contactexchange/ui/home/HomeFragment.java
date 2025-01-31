package dev.arch3rtemp.contactexchange.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.App;
import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.router.Router;
import dev.arch3rtemp.contactexchange.ui.createoredit.CreateOrEditCardFragment;
import dev.arch3rtemp.contactexchange.ui.detail.CardDetailsFragment;
import dev.arch3rtemp.contactexchange.ui.filter.FilterFragment;
import dev.arch3rtemp.contactexchange.ui.home.adapter.ContactRecyclerAdapter;
import dev.arch3rtemp.contactexchange.ui.model.ContactUi;

public class HomeFragment extends Fragment implements HomeContract.View {

    @Inject
    HomeContract.Presenter presenter;
    @Inject
    Router router;

    private FloatingActionButton fab;
    private RecyclerView rvCards;
    private AppCompatTextView tvCardsErrorDesc;
    private ImageView ivCardsEmpty;

    private ImageView ivSearch;
    private RecyclerView rvContacts;
    private AppCompatTextView tvContactsErrorDesc;
    private ImageView ivContactsEmpty;

    private ContactRecyclerAdapter cardAdapter;
    private ContactRecyclerAdapter contactAdapter;

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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        setListeners();
        presenter.onCreate(this);
        getMyCards();
        getContacts();
    }

    private void initUI(View view) {
        fab = view.findViewById(R.id.fab_add);
        rvCards = view.findViewById(R.id.rv_cards);
        tvCardsErrorDesc = view.findViewById(R.id.tv_cards_error_desc);
        ivCardsEmpty = view.findViewById(R.id.iv_cards_empty);

        ivSearch = view.findViewById(R.id.iv_search);
        rvContacts = view.findViewById(R.id.rv_contacts);
        tvContactsErrorDesc = view.findViewById(R.id.tv_contacts_error_desc);
        ivContactsEmpty = view.findViewById(R.id.iv_contacts_empty);

        createCardRecyclerView();
        createContactRecyclerView();
    }

    private void setListeners() {
        fab.setOnClickListener(v -> {
            router.navigate(CreateOrEditCardFragment.newInstance(-1, true), true, true);
        });

        ivSearch.setOnClickListener(v -> {
            router.navigate(FilterFragment.newInstance(), false, true);
        });
    }

    private void createCardRecyclerView() {
        cardAdapter = new ContactRecyclerAdapter(this::onContactClick);
        rvCards.setAdapter(cardAdapter);
        observeRecyclerListener();
    }

    private void createContactRecyclerView() {
        contactAdapter = new ContactRecyclerAdapter(this::onContactClick);
        rvContacts.setAdapter(contactAdapter);
        contactAdapter.setDeleteClickListener(this::onDeleteClick);
    }

    private void observeRecyclerListener() {
        rvCards.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.setVisibility(View.INVISIBLE);
                } else if (dx < 0 && fab.getVisibility() == View.INVISIBLE) {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getMyCards() {
        presenter.getMyCards();
    }

    private void getContacts() {
        presenter.getContacts();
    }

    public void onContactClick(ContactUi contact) {
        router.navigate(CardDetailsFragment.newInstance(contact.id(), contact.isMy()), true, true);
    }

    public void onDeleteClick(ContactUi contact) {
        presenter.deleteContact(contact);
    }

    @Override
    public void onCardsResult(HomeContract.ViewState result) {
        if (result instanceof HomeContract.ViewState.Empty) showCardsEmpty();
        else if (result instanceof HomeContract.ViewState.Error) showCardsError();
        else if (result instanceof HomeContract.ViewState.Success) showCardsContent(((HomeContract.ViewState.Success) result).data());
    }

    @Override
    public void onContactsResult(HomeContract.ViewState result) {
        if (result instanceof HomeContract.ViewState.Empty) showContactsEmpty();
        else if (result instanceof HomeContract.ViewState.Error) showContactsError();
        else if (result instanceof HomeContract.ViewState.Success) showContactsContent(((HomeContract.ViewState.Success) result).data());
    }

    @Override
    public void onContactDeleted(ContactUi contact, String message) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.msg_undo), (v) -> presenter.saveContact(contact))
                .show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cardAdapter = null;
        contactAdapter = null;
        presenter.onDestroy();
    }

    private void showCardsContent(List<ContactUi> cards) {
        rvCards.setVisibility(View.VISIBLE);
        cardAdapter.updateItems(cards);
        tvCardsErrorDesc.setVisibility(View.GONE);
        ivCardsEmpty.setVisibility(View.GONE);
    }

    private void showContactsContent(List<ContactUi> contacts) {
        rvContacts.setVisibility(View.VISIBLE);
        contactAdapter.updateItems(contacts);
        tvContactsErrorDesc.setVisibility(View.GONE);
        ivContactsEmpty.setVisibility(View.GONE);
    }

    private void showCardsError() {
        rvCards.setVisibility(View.GONE);
        tvCardsErrorDesc.setVisibility(View.VISIBLE);
        ivCardsEmpty.setVisibility(View.GONE);
    }

    private void showContactsError() {
        rvContacts.setVisibility(View.GONE);
        tvContactsErrorDesc.setVisibility(View.VISIBLE);
        ivContactsEmpty.setVisibility(View.GONE);
    }

    private void showCardsEmpty() {
        rvCards.setVisibility(View.GONE);
        tvCardsErrorDesc.setVisibility(View.GONE);
        ivCardsEmpty.setVisibility(View.VISIBLE);
    }

    private void showContactsEmpty() {
        rvContacts.setVisibility(View.GONE);
        tvContactsErrorDesc.setVisibility(View.GONE);
        ivContactsEmpty.setVisibility(View.VISIBLE);
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
}
