package dev.arch3rtemp.contactexchange.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.contactexchange.ui.card.CardActivity;
import dev.arch3rtemp.contactexchange.ui.card.FragmentType;
import dev.arch3rtemp.contactexchange.ui.filter.FilterFragment;
import dev.arch3rtemp.contactexchange.ui.home.adapter.ContactRecyclerAdapter;

public class HomeFragment extends Fragment implements IHomeContract.View {

    private RecyclerView rvCards;
    private RecyclerView rvContacts;
    private FloatingActionButton fab;
    private ContactRecyclerAdapter rvCardAdapter;
    private ContactRecyclerAdapter rvContactAdapter;
    private ImageView ivSearch;
    private LinearLayout llContacts;
    private IHomeContract.Presenter presenter;

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
        setPresenter(new HomePresenter(this));
        presenter.onCreate(getActivity());
        getMyCards();
        getContacts();
    }

    private void initUI(View view) {
        fab = view.findViewById(R.id.fab_add);
        ivSearch = view.findViewById(R.id.iv_search);
        rvCards = view.findViewById(R.id.rv_cards);
        rvContacts = view.findViewById(R.id.rv_contacts);
        llContacts = view.findViewById(R.id.ll_contacts);
        createCardRecyclerView();
        createContactRecyclerView();
    }

    private void setListeners() {
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CardActivity.class);
            intent.putExtra("type", FragmentType.CREATE);
            startActivity(intent);
        });

        ivSearch.setOnClickListener(v -> {
            var transaction = getParentFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.addSharedElement(llContacts, getString(R.string.transition_contacts));
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.replace(R.id.fl_main_frame_container, FilterFragment.class, null, FilterFragment.class.getSimpleName());
            transaction.addToBackStack(FilterFragment.class.getSimpleName());
            transaction.commit();
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
                } else if(dx < 0 && fab.getVisibility() == View.INVISIBLE) {
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

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    public void onContactClick(Contact contact, int contactPosition) {
        Intent intent = new Intent(getContext(), CardActivity.class);
        intent.putExtra("type", FragmentType.CARD);
        intent.putExtra("isMy", contact.getIsMy());
        intent.putExtra("id", contact.getId());
        startActivity(intent);
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
    public void setPresenter(IHomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rvCardAdapter = null;
        rvContactAdapter = null;
        presenter.onDestroy();
    }
}
