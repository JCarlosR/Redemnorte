package com.youtube.sorcjc.redemnorte;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> {
    private ArrayList<Header> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // context
        Context context;
        // text views
        TextView headerCode;
        TextView responsibleName;
        TextView headerDate;
        // buttons
        Button btnDetails;
        Button btnEditHeader;

        ViewHolder(View v) {
            super(v);
            context = v.getContext();

            headerCode = (TextView) v.findViewById(R.id.headerCode);
            responsibleName = (TextView) v.findViewById(R.id.responsibleName);
            headerDate = (TextView) v.findViewById(R.id.headerDate);

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
                    intent.putExtra("headerCode", headerCode.getText());
                    context.startActivity(intent);
                    break;
                case R.id.btnEditHeader:
                    break;
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    HeaderAdapter(ArrayList<Header> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HeaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_header, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get element from your data-set at this position
        // replace the contents of the view with that element
        holder.headerCode.setText(mDataset.get(position).getHeaderCode());
        holder.responsibleName.setText(mDataset.get(position).getResponsibleName());
        holder.headerDate.setText(mDataset.get(position).getHeaderDate());

        // set events
        holder.setOnClickListeners();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
