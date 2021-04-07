package com.example.contactsexchangejava.ui.home;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsexchangejava.R;
import com.example.contactsexchangejava.constants.FragmentType;
import com.example.contactsexchangejava.ui.card.CardActivity;
import com.example.contactsexchangejava.db.models.Contact;
import com.example.contactsexchangejava.ui.search.SearchActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements ContactRecyclerAdapter.IContactClickListener, IHomeContract.View{

    View view;
    TextView card;
    RecyclerView rvCards;
    RecyclerView rvContacts;
    FloatingActionButton fab;
    ImageView ivSearch;
    ContactRecyclerAdapter rvCardAdapter;
    ContactRecyclerAdapter rvContactAdapter;
    LinearLayoutManager llCardManager;
    LinearLayoutManager llContactManager;
    ConstraintLayout clContacts;
    IHomeContract.Presenter presenter;
    List<Contact> myCards = new ArrayList<>();
    List<Contact> contacts = new ArrayList<>();
    TextView tvContactHeader;
    float startingPosition;
    AppCompatActivity activity;
    private boolean launchNextActivity = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        setListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        myCards.clear();
        this.contacts.clear();
        getMyCards();
        getContacts();
    }

    private void initUI() {
        fab = view.findViewById(R.id.fb_add);
        ivSearch = view.findViewById(R.id.iv_search);
        setPresenter(new HomePresenter(this));
        presenter.onViewCreated(getActivity());
        clContacts = view.findViewById(R.id.cl_contacts);
        tvContactHeader = view.findViewById(R.id.tv_contact_header);
    }

    private void setListeners() {
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CardActivity.class);
            intent.putExtra("isCreate", true);
            startActivity(intent);
        });

        ivSearch.setOnClickListener(v -> {
            animateSearchMenu();
        });
    }

    private void animateSearchMenu() {
        startingPosition = ivSearch.getTranslationX();
        ObjectAnimator moveLeftX = ObjectAnimator.ofFloat(ivSearch, View.X, 0f);
        moveLeftX.setDuration(400)
                .setInterpolator(new DecelerateInterpolator());
        ObjectAnimator fade = ObjectAnimator.ofFloat(tvContactHeader, View.ALPHA, 1f, 0f);
        AnimatorSet animator = new AnimatorSet();
        animator.playTogether(moveLeftX, fade);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(activity, SearchActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(activity, clContacts, "transition_contacts");
                startActivityForResult(intent, 122, options.toBundle());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 122) {
            if (resultCode == RESULT_OK) {
                launchNextActivity = true;
            }
        }
    }

    private void createCardRecyclerView() {
        card = view.findViewById(R.id.tv_card);
        rvCards = view.findViewById(R.id.rv_cards);
        rvCardAdapter = new ContactRecyclerAdapter(myCards);
        llCardManager = new LinearLayoutManager(getContext());
        llCardManager.setOrientation(RecyclerView.HORIZONTAL);
        rvCards.setLayoutManager(llCardManager);
        rvCards.setAdapter(rvCardAdapter);
        rvCardAdapter.setContactClickListener(this);
        observeRecyclerListener();
//        rvCardAdapter.addContacts(getMyCards());
    }

    private void createContactRecyclerView() {
        rvContacts = view.findViewById(R.id.rv_contacts);
        rvContactAdapter = new ContactRecyclerAdapter(contacts);
        llContactManager = new LinearLayoutManager(getContext());
        llContactManager.setOrientation(RecyclerView.VERTICAL);
        rvContacts.setLayoutManager(llContactManager);
        rvContacts.setAdapter(rvContactAdapter);
        rvContactAdapter.setContactClickListener(this);
//        rvContactAdapter.addContacts(getContacts());
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
                if (dx > 0) {
                    fab.setVisibility(View.INVISIBLE);
                } else {
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
  

    @Override
    public void contactClicked(Contact contact, int contactPosition) {
        Intent intent = new Intent(getContext(), CardActivity.class);
        intent.putExtra("isMe", contact.getIsMe());
        intent.putExtra("id", contact.getId());
        startActivity(intent);
    }

    @Override
    public void setPresenter(IHomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onPause() {
        super.onPause();
        rvContactAdapter.clearAdapter();
        rvCardAdapter.clearAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (launchNextActivity) {
            launchNextActivity = false;
            ObjectAnimator moveRightX = ObjectAnimator.ofFloat(ivSearch, View.X, 0 - ivSearch.getTranslationX());
            moveRightX.setDuration(400)
                    .setInterpolator(new DecelerateInterpolator());
            ObjectAnimator appear = ObjectAnimator.ofFloat(tvContactHeader, View.ALPHA, 0f, 1f);
            AnimatorSet animator = new AnimatorSet();
            animator.playTogether(moveRightX, appear);
            animator.start();
        }
        if (rvContactAdapter != null)
            rvContactAdapter.clearAdapter();
        if (rvCardAdapter != null)
            rvCardAdapter.clearAdapter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;
        presenter.onDestroy();
    }

    @Override
    public void onGetMyCards(List<Contact> cards) {
        myCards.addAll(cards);
        createCardRecyclerView();
    }

    @Override
    public void onGetContacts(List<Contact> contacts) {
        this.contacts.addAll(contacts);
        createContactRecyclerView();
    }
}
