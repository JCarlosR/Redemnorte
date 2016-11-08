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
import android.widget.Toast;

import com.youtube.sorcjc.redemnorte.R;
import com.youtube.sorcjc.redemnorte.io.RedemnorteApiAdapter;
import com.youtube.sorcjc.redemnorte.io.response.BienesResponse;
import com.youtube.sorcjc.redemnorte.model.Bien;
import com.youtube.sorcjc.redemnorte.ui.adapter.DetailAdapter;
import com.youtube.sorcjc.redemnorte.ui.fragment.DetailDialogFragment;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, Callback<BienesResponse> {

    private DetailAdapter detailAdapter;
    private String hoja_id, responsable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hoja_id = extras.getString("hoja_id");
            responsable = extras.getString("responsable");
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<Bien> myDataSet = new ArrayList<>();

        detailAdapter = new DetailAdapter(myDataSet, hoja_id, responsable);
        recyclerView.setAdapter(detailAdapter);

        cargarBienes();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Hoja " + hoja_id);
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
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fab.show();

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    public void cargarBienes() {
        Call<BienesResponse> call = RedemnorteApiAdapter.getApiService().getBienes(hoja_id);
        call.enqueue(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                showCreateDetailDialog();
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
        DetailDialogFragment newFragment = DetailDialogFragment.newInstance(hoja_id, "", responsable);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onResponse(Call<BienesResponse> call, Response<BienesResponse> response) {
        if (response.isSuccessful()) {
            ArrayList<Bien> bienes = response.body().getBienes();
            detailAdapter.setDataSet(bienes);
            Toast.makeText(this, "Cantidad de bienes => " + bienes.size(), Toast.LENGTH_SHORT).show();
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
    public void onFailure(Call<BienesResponse> call, Throwable t) {
        Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
