package com.youtube.sorcjc.redemnorte.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.youtube.sorcjc.redemnorte.R;
import com.youtube.sorcjc.redemnorte.model.Detail;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {
    private ArrayList<Detail> mDataSet;

    // Provide a reference to the views for each data item
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // context
        Context context;
        // text views
        TextView tvDetailQR;
        TextView tvDescription;
        TextView tvPatrimonial;
        // buttons
        Button btnShowProduct;
        Button btnEditProduct;

        ViewHolder(View v) {
            super(v);
            context = v.getContext();

            tvDetailQR = (TextView) v.findViewById(R.id.tvDetailQR);
            tvDescription = (TextView) v.findViewById(R.id.tvDescription);
            tvPatrimonial = (TextView) v.findViewById(R.id.tvPatrimonial);

            btnShowProduct = (Button) v.findViewById(R.id.btnShowProduct);
            btnEditProduct = (Button) v.findViewById(R.id.btnEditProduct);
        }

        void setOnClickListeners() {
            btnShowProduct.setOnClickListener(this);
            btnEditProduct.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnShowProduct:
                    break;
                case R.id.btnEditProduct:
                    break;
            }
        }
    }

    // Provide a suitable constructor
    public DetailAdapter(ArrayList<Detail> myDataSet) {
        mDataSet = myDataSet;
    }

    // Create new views
    @Override
    public DetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get element from your data-set at this position
        // replace the contents of the view with that element
        holder.tvDetailQR.setText("QR: " + mDataSet.get(position).getQr());
        holder.tvDescription.setText(mDataSet.get(position).getDescription());
        holder.tvPatrimonial.setText("Cód Patrimonial: " + mDataSet.get(position).getPatrimonial());

        // set events
        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
