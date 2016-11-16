package com.youtube.sorcjc.redemnorte.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.youtube.sorcjc.redemnorte.R;
import com.youtube.sorcjc.redemnorte.model.Hoja;
import com.youtube.sorcjc.redemnorte.ui.DetailsActivity;
import com.youtube.sorcjc.redemnorte.ui.PanelActivity;
import com.youtube.sorcjc.redemnorte.ui.fragment.HeaderDialogFragment;

import java.util.ArrayList;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> {
    private ArrayList<Hoja> dataSet, filteredDataSet;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // context
        Context context;
        // text views
        TextView headerCode;
        TextView responsibleName;
        TextView headerDate;
        TextView tvImpreso;
        // buttons
        Button btnDetails;
        Button btnEditHeader;
        // params
        String hoja_id, responsable;

        ViewHolder(View v) {
            super(v);
            context = v.getContext();

            headerCode = (TextView) v.findViewById(R.id.headerCode);
            responsibleName = (TextView) v.findViewById(R.id.responsibleName);
            headerDate = (TextView) v.findViewById(R.id.headerDate);
            tvImpreso = (TextView) v.findViewById(R.id.tvImpreso);

            btnDetails = (Button) v.findViewById(R.id.btnDetails);
            btnEditHeader = (Button) v.findViewById(R.id.btnEditHeader);
        }

        void setOnClickListeners() {
            btnDetails.setOnClickListener(this);
            btnEditHeader.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnDetails:
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("hoja_id", hoja_id);
                    intent.putExtra("responsable", responsable);
                    context.startActivity(intent);
                    break;
                case R.id.btnEditHeader:
                    showEditHeaderDialog(hoja_id);
                    break;
            }
        }

        private void showEditHeaderDialog(final String hoja_id) {
            FragmentManager fragmentManager = ((PanelActivity) context).getSupportFragmentManager();

            // hoja_id is required to => edit a specific header
            HeaderDialogFragment newFragment = HeaderDialogFragment.newInstance(hoja_id);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, newFragment)
                    .addToBackStack(null).commit();
        }
    }

    public HeaderAdapter(ArrayList<Hoja> dataSet) {
        this.dataSet = dataSet;
        this.filteredDataSet = dataSet;
    }

    public void setDataSet(ArrayList<Hoja> dataSet) {
        this.dataSet = dataSet;
        this.filteredDataSet = dataSet;
        notifyDataSetChanged();
    }

    public void filterResponsibleStartsWith(String query) {
        if (query.isEmpty()) {
            filteredDataSet = dataSet;
        } else {
            filteredDataSet = new ArrayList<>();
            for (Hoja item : dataSet) {
                if (item.getResponsable().toLowerCase().contains(query.toLowerCase()))
                    filteredDataSet.add(item);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int active = 1;
        if (dataSet.get(position).getActivo().equals("0"))
            active = 0;

        return active;
    }

    @Override
    public HeaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_header, parent, false);

        if (viewType == 0) { // no active
            v.findViewById(R.id.tvInactive).setVisibility(View.VISIBLE);
        } else { // is active
            v.findViewById(R.id.tvInactive).setVisibility(View.GONE);
        }

        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get element from dataSet at this position
        // and replace the contents of the view
        Hoja currentHeader = filteredDataSet.get(position);

        holder.headerCode.setText(currentHeader.getId());
        holder.responsibleName.setText(currentHeader.getResponsable());
        holder.headerDate.setText(currentHeader.getFecha());

        if (currentHeader.getImpreso().trim().equals("1")) {
            holder.tvImpreso.setVisibility(View.VISIBLE);
        } else {
            holder.tvImpreso.setVisibility(View.GONE);
        }

        // set events
        holder.setOnClickListeners();

        // params needed to show the details
        holder.hoja_id = currentHeader.getId();
        holder.responsable = currentHeader.getResponsable().trim();
    }

    @Override
    public int getItemCount() {
        return filteredDataSet.size();
    }
}
