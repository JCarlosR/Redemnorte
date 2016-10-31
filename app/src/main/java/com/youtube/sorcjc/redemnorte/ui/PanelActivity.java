package com.youtube.sorcjc.redemnorte.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.youtube.sorcjc.redemnorte.R;
import com.youtube.sorcjc.redemnorte.model.Header;
import com.youtube.sorcjc.redemnorte.ui.adapter.HeaderAdapter;
import com.youtube.sorcjc.redemnorte.ui.fragment.HeaderDialogFragment;

import java.util.ArrayList;

public class PanelActivity extends AppCompatActivity implements View.OnClickListener {

    private HeaderAdapter headerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<Header> myDataset = new ArrayList<>();
        myDataset.add(new Header("100001", "Juan Ramos", "27 de Octubre del 2016"));
        myDataset.add(new Header("100002", "Juan Ramos", "28 de Octubre del 2016"));
        myDataset.add(new Header("100003", "Juan Ramos", "29 de Octubre del 2016"));

        myDataset.add(new Header("100004", "Juan Ramos", "27 de Noviembre del 2016"));
        myDataset.add(new Header("100005", "Juan Ramos", "28 de Noviembre del 2016"));
        myDataset.add(new Header("100006", "Juan Ramos", "29 de Noviembre del 2016"));

        myDataset.add(new Header("100007", "Juan Ramos", "27 de Diciembre del 2016"));
        myDataset.add(new Header("100008", "Juan Ramos", "28 de Diciembre del 2016"));
        myDataset.add(new Header("100009", "Juan Ramos", "29 de Diciembre del 2016"));

        headerAdapter = new HeaderAdapter(myDataset);
        recyclerView.setAdapter(headerAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0 || dy < 0 && fab.isShown())
                {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    fab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                showCreateHeaderDialog();
                break;
        }
    }

    private void showCreateHeaderDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        HeaderDialogFragment newFragment = new HeaderDialogFragment();

        // The device is smaller, so show the fragment fullscreen
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
    }
}
