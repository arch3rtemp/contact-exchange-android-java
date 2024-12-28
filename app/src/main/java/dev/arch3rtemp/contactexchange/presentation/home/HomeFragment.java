package dev.arch3rtemp.contactexchange.presentation.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.databinding.FragmentHomeBinding;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.presentation.home.adapter.CardsAdapter;
import dev.arch3rtemp.contactexchange.presentation.home.adapter.listener.CardClickListener;
import dev.arch3rtemp.contactexchange.presentation.home.adapter.listener.ContactClickListener;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dev.arch3rtemp.ui.base.BaseFragment;
import dev.arch3rtemp.ui.view.AppSearchView;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<HomeContract.HomeEvent, HomeContract.HomeEffect, HomeContract.HomeState, FragmentHomeBinding, HomePresenter> implements CardClickListener, ContactClickListener {

    @Inject
    HomePresenter presenter;

    private CardsAdapter cardsAdapter;
    private CardsAdapter contactsAdapter;

    @Override
    protected FragmentHomeBinding bindLayout(LayoutInflater inflater, ViewGroup container, boolean attachToRoot) {
        return FragmentHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    protected HomePresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void prepareView(@Nullable Bundle savedInstanceState) {
        setupToolbar();
        setCardsRV();
        setContactsRV();
        setListeners();
    }

    @Override
    protected void renderState(HomeContract.HomeState state) {
        renderCardsState(state.myCards());
        renderContactsState(state.scannedCards());
    }

    @Override
    protected void renderEffect(HomeContract.HomeEffect effect) {
        if (effect instanceof HomeContract.HomeEffect.ShowError showError) {
            Toast.makeText(requireContext(), showError.message(), Toast.LENGTH_SHORT).show();
        } else if (effect instanceof HomeContract.HomeEffect.ShowUndo showUndo) {
            Snackbar.make(requireView(), getString(R.string.msg_contact_deleted), Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.msg_undo), (v) -> presenter.setEvent(new HomeContract.HomeEvent.OnContactSaved(showUndo.card())))
                    .show();
        }
    }

    private void renderCardsState(HomeContract.ViewState cardsState) {
        if (cardsState instanceof HomeContract.ViewState.Idle) {
            getMyCards();
        } else if (cardsState instanceof HomeContract.ViewState.Loading) {
            showCardsLoading();
        } else if (cardsState instanceof HomeContract.ViewState.Empty) {
            showCardsEmpty();
        } else if (cardsState instanceof HomeContract.ViewState.Error) {
            showCardsError();
        } else if (cardsState instanceof HomeContract.ViewState.Success success) {
            showCardsSuccess();
            cardsAdapter.submitList(success.data());
        }
    }

    private void renderContactsState(HomeContract.ViewState contactsState) {
        if (contactsState instanceof HomeContract.ViewState.Idle) {
            getContacts();
        } else if (contactsState instanceof HomeContract.ViewState.Loading) {
            showContactsLoading();
        } else if (contactsState instanceof HomeContract.ViewState.Empty) {
            showContactsEmpty();
        } else if (contactsState instanceof HomeContract.ViewState.Error) {
            showContactsError();
        } else if (contactsState instanceof HomeContract.ViewState.Success success) {
            showContactsSuccess();
            contactsAdapter.submitList(success.data());
        }
    }

    private void setupToolbar() {

        var searchView = (AppSearchView) getBinding().toolbarContact.getMenu().findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.setEvent(new HomeContract.HomeEvent.OnSearchTyped(newText));
                return false;
            }
        });
    }

    private void setListeners() {
        getBinding().fabAdd.setOnClickListener(v -> {
            var action = HomeFragmentDirections.actionHomeFragmentToCreateCardFragment();
            NavHostFragment.findNavController(this).navigate(action);
        });
    }

    private void setCardsRV() {
        cardsAdapter = new CardsAdapter(this, null);
        getBinding().rvCards.setAdapter(cardsAdapter);
        observeRecyclerListener();
    }

    private void setContactsRV() {
        contactsAdapter = new CardsAdapter(null, this);
        getBinding().rvContacts.setAdapter(contactsAdapter);
    }

    @Override
    public void onCardClick(int id) {
        goToCardFragment(id);
    }

    @Override
    public void onContactClick(int id) {
        goToCardFragment(id);
    }

    @Override
    public void onDeleteClick(Card card) {
        presenter.setEvent(new HomeContract.HomeEvent.OnContactDeleted(card));
    }

    private void goToCardFragment(int id) {
        NavHostFragment.findNavController(this).navigate(HomeFragmentDirections.actionHomeFragmentToCardFragment(id));
    }

    private void observeRecyclerListener() {
        getBinding().rvCards.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // If scrolling towards the left (dx < 0), hide the FAB
                if (dx > 0 && getBinding().fabAdd.getVisibility() == View.VISIBLE) {
                    getBinding().fabAdd.hide();
                }
                // If scrolling towards the right (dx > 0), show the FAB
                if (dx < 0 && getBinding().fabAdd.getVisibility() != View.VISIBLE) {
                    getBinding().fabAdd.show();
                }
            }
        });
    }

    private void getMyCards() {
        presenter.setEvent(new HomeContract.HomeEvent.OnCardsLoad());
    }

    private void getContacts() {
        presenter.setEvent(new HomeContract.HomeEvent.OnContactsLoad());
    }

    private void showCardsEmpty() {
        getBinding().ivCardsEmpty.setVisibility(View.VISIBLE);
        getBinding().progressCircularCards.setVisibility(View.INVISIBLE);
        getBinding().ivCardsError.setVisibility(View.INVISIBLE);
        getBinding().rvCards.setVisibility(View.INVISIBLE);
    }

    private void showCardsLoading() {
        getBinding().progressCircularCards.setVisibility(View.VISIBLE);
        getBinding().ivCardsEmpty.setVisibility(View.INVISIBLE);
        getBinding().ivCardsError.setVisibility(View.INVISIBLE);
        getBinding().rvCards.setVisibility(View.INVISIBLE);
    }

    private void showCardsError() {
        getBinding().ivCardsError.setVisibility(View.VISIBLE);
        getBinding().rvCards.setVisibility(View.INVISIBLE);
        getBinding().progressCircularCards.setVisibility(View.INVISIBLE);
        getBinding().ivCardsEmpty.setVisibility(View.INVISIBLE);
    }

    private void showCardsSuccess() {
        getBinding().rvCards.setVisibility(View.VISIBLE);
        getBinding().progressCircularCards.setVisibility(View.INVISIBLE);
        getBinding().ivCardsEmpty.setVisibility(View.INVISIBLE);
        getBinding().ivCardsError.setVisibility(View.INVISIBLE);
    }

    private void showContactsEmpty() {
        getBinding().ivContactsEmpty.setVisibility(View.VISIBLE);
        getBinding().progressCircularContacts.setVisibility(View.GONE);
        getBinding().ivContactsError.setVisibility(View.GONE);
        getBinding().rvContacts.setVisibility(View.GONE);
    }

    private void showContactsLoading() {
        getBinding().progressCircularContacts.setVisibility(View.VISIBLE);
        getBinding().ivContactsEmpty.setVisibility(View.GONE);
        getBinding().ivContactsError.setVisibility(View.GONE);
        getBinding().rvContacts.setVisibility(View.GONE);
    }

    private void showContactsError() {
        getBinding().ivContactsError.setVisibility(View.VISIBLE);
        getBinding().rvContacts.setVisibility(View.GONE);
        getBinding().progressCircularContacts.setVisibility(View.GONE);
        getBinding().ivContactsEmpty.setVisibility(View.GONE);
    }

    private void showContactsSuccess() {
        getBinding().rvContacts.setVisibility(View.VISIBLE);
        getBinding().progressCircularContacts.setVisibility(View.GONE);
        getBinding().ivContactsEmpty.setVisibility(View.GONE);
        getBinding().ivContactsError.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cardsAdapter = null;
        contactsAdapter = null;
    }
}
