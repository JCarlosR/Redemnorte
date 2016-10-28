package com.youtube.sorcjc.redemnorte;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class PanelActivity extends AppCompatActivity {

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
}
