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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private ImageView ivSearch;
    private RecyclerView rvContacts;
    private ContactRecyclerAdapter rvCardAdapter;
    private ContactRecyclerAdapter rvContactAdapter;

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
        ivSearch = view.findViewById(R.id.iv_search);
        rvContacts = view.findViewById(R.id.rv_contacts);
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
        rvCardAdapter = new ContactRecyclerAdapter(this::onContactClick);
        rvCards.setAdapter(rvCardAdapter);
        observeRecyclerListener();
    }

    private void createContactRecyclerView() {
        rvContactAdapter = new ContactRecyclerAdapter(this::onContactClick);
        rvContacts.setAdapter(rvContactAdapter);
        rvContactAdapter.setDeleteClickListener(this::onDeleteClick);
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
        presenter.deleteContact(contact.id());
    }

    @Override
    public void onGetMyCards(List<ContactUi> cards) {
        rvCardAdapter.updateItems(cards);
    }

    @Override
    public void onGetContacts(List<ContactUi> contacts) {
        rvContactAdapter.updateItems(contacts);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rvCardAdapter = null;
        rvContactAdapter = null;
        presenter.onDestroy();
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
}
