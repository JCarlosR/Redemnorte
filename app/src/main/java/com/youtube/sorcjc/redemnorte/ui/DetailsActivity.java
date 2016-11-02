package com.youtube.sorcjc.redemnorte.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.youtube.sorcjc.redemnorte.R;
import com.youtube.sorcjc.redemnorte.model.Detail;
import com.youtube.sorcjc.redemnorte.model.Header;
import com.youtube.sorcjc.redemnorte.ui.adapter.DetailAdapter;
import com.youtube.sorcjc.redemnorte.ui.adapter.HeaderAdapter;
import com.youtube.sorcjc.redemnorte.ui.fragment.DetailDialogFragment;
import com.youtube.sorcjc.redemnorte.ui.fragment.HeaderDialogFragment;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private DetailAdapter detailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String headerCode = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             headerCode = extras.getString("headerCode");
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<Detail> myDataSet = new ArrayList<>();
        myDataSet.add(new Detail("100001", "1002001", "Descripción"));
        myDataSet.add(new Detail("100002", "1002001", "Descripción"));
        myDataSet.add(new Detail("100003", "1002001", "Descripción"));
        myDataSet.add(new Detail("100004", "1002001", "Descripción"));
        myDataSet.add(new Detail("100005", "1002001", "Descripción"));

        detailAdapter = new DetailAdapter(myDataSet);
        recyclerView.setAdapter(detailAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Hoja " + headerCode);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

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
                showCreateDetailDialog();
                break;
            case R.id.btnShowProduct:
                break;
            case R.id.btnEditProduct:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCreateDetailDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailDialogFragment newFragment = new DetailDialogFragment();

        // The device is smaller, so show the fragment fullscreen
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
    }
}
