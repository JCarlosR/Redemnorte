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
import android.widget.Toast;

import com.youtube.sorcjc.redemnorte.Global;
import com.youtube.sorcjc.redemnorte.R;
import com.youtube.sorcjc.redemnorte.io.RedemnorteApiAdapter;
import com.youtube.sorcjc.redemnorte.io.response.HojasResponse;
import com.youtube.sorcjc.redemnorte.model.Hoja;
import com.youtube.sorcjc.redemnorte.ui.adapter.HeaderAdapter;
import com.youtube.sorcjc.redemnorte.ui.fragment.HeaderDialogFragment;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class PanelActivity extends AppCompatActivity implements View.OnClickListener, Callback<HojasResponse> {

    private HeaderAdapter headerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<Hoja> myDataset = new ArrayList<>();
        cargarHojas();

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

    public void cargarHojas() {
        final String username = Global.getFromSharedPreferences(this, "username");
        Call<HojasResponse> call = RedemnorteApiAdapter.getApiService().getHojas(username);
        call.enqueue(this);
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

        // Empty hoja_id => Register new header
        HeaderDialogFragment newFragment = HeaderDialogFragment.newInstance("");

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onResponse(Call<HojasResponse> call, Response<HojasResponse> response) {
        if (response.isSuccessful()) {
            ArrayList<Hoja> hojas = response.body().getHojas();
            headerAdapter.setDataSet(hojas);
            Toast.makeText(this, "Cantidad de hojas => " + hojas.size(), Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onFailure(Call<HojasResponse> call, Throwable t) {
        Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
