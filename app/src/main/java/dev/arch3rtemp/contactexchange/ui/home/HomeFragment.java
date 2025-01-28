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
import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.contactexchange.router.Router;
import dev.arch3rtemp.contactexchange.ui.card.CardActivity;
import dev.arch3rtemp.contactexchange.ui.card.FragmentType;
import dev.arch3rtemp.contactexchange.ui.filter.FilterFragment;
import dev.arch3rtemp.contactexchange.ui.home.adapter.ContactRecyclerAdapter;

public class HomeFragment extends Fragment implements HomeContract.View {

    private FloatingActionButton fab;
    private RecyclerView rvCards;
    private ImageView ivSearch;
    private RecyclerView rvContacts;
    private ContactRecyclerAdapter rvCardAdapter;
    private ContactRecyclerAdapter rvContactAdapter;
    @Inject
    HomeContract.Presenter presenter;
    @Inject
    Router router;

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
            CardActivity.start(requireContext(), -1, false, FragmentType.CREATE);
        });

        ivSearch.setOnClickListener(v -> {
            router.navigate(FilterFragment.class, null, true);
        });
    }

    private void createCardRecyclerView() {
        rvCardAdapter = new ContactRecyclerAdapter();
        rvCards.setAdapter(rvCardAdapter);
        rvCardAdapter.setContactClickListener(this::onContactClick);
        observeRecyclerListener();
    }

    private void createContactRecyclerView() {
        rvContactAdapter = new ContactRecyclerAdapter();
        rvContacts.setAdapter(rvContactAdapter);
        rvContactAdapter.setContactClickListener(this::onContactClick);
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

    public void onContactClick(Contact contact, int contactPosition) {
        CardActivity.start(requireContext(), contact.getId(), contact.getIsMy(), FragmentType.CARD);
    }

    public void onDeleteClick(Contact contact, int contactPosition) {
        presenter.deleteContact(contact.getId(), contactPosition);
    }

    @Override
    public void onGetMyCards(List<Contact> cards) {
        rvCardAdapter.updateItems(cards);
    }

    @Override
    public void onGetContacts(List<Contact> contacts) {
        rvContactAdapter.updateItems(contacts);
    }

    @Override
    public void onContactDelete(int position) {
        rvContactAdapter.removeItem(position);
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
}
