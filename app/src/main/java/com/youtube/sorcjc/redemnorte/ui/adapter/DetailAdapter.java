package com.youtube.sorcjc.redemnorte.ui.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.youtube.sorcjc.redemnorte.R;
import com.youtube.sorcjc.redemnorte.model.Bien;
import com.youtube.sorcjc.redemnorte.ui.DetailsActivity;
import com.youtube.sorcjc.redemnorte.ui.fragment.DetailDialogFragment;
import com.youtube.sorcjc.redemnorte.ui.fragment.ShowDetailDialog;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private ArrayList<Bien> dataSet;
    private static String hoja_id, responsable;

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
        // id
        String qr_code;

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
                    showProductDataDialog();
                    break;
                case R.id.btnEditProduct:
                    editProductDialog();
                    break;
            }
        }

        private void showProductDataDialog() {
            FragmentManager fragmentManager = ((DetailsActivity) context).getSupportFragmentManager();
            ShowDetailDialog newFragment = ShowDetailDialog.newInstance(hoja_id, qr_code);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.add(android.R.id.content, newFragment)
                    .addToBackStack(null).commit();
        }

        private void editProductDialog() {
            FragmentManager fragmentManager = ((DetailsActivity) context).getSupportFragmentManager();
            DetailDialogFragment newFragment = DetailDialogFragment.newInstance(hoja_id, qr_code, responsable);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.add(android.R.id.content, newFragment)
                    .addToBackStack(null).commit();
        }
    }

    // Provide a suitable constructor
    public DetailAdapter(ArrayList<Bien> myDataSet, String _hoja_id, String _responsable) {
        dataSet = myDataSet;
        hoja_id = _hoja_id;
        responsable = _responsable;
    }

    public void setDataSet(ArrayList<Bien> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
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
        Bien bien = dataSet.get(position);
        holder.qr_code = bien.getQr();
        holder.tvDetailQR.setText("QR: " + bien.getQr());
        holder.tvDescription.setText(bien.getDescription());
        holder.tvPatrimonial.setText("Cód Patrimonial: " + bien.getPatrimonial());

        // set events
        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
