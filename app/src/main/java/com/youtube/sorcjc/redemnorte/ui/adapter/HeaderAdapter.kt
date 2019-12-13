package com.youtube.sorcjc.redemnorte.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.model.Sheet
import com.youtube.sorcjc.redemnorte.ui.activity.DetailsActivity
import com.youtube.sorcjc.redemnorte.ui.activity.PanelActivity
import com.youtube.sorcjc.redemnorte.ui.fragment.HeaderDialogFragment.Companion.newInstance

class HeaderAdapter(private var dataSet: ArrayList<Sheet> = ArrayList()) : RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {
    private var filteredDataSet: ArrayList<Sheet>

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        // context
        var context: Context = v.context

        // text views
        var headerCode: TextView = v.findViewById(R.id.headerCode)
        var responsibleName: TextView = v.findViewById(R.id.responsibleName)
        var headerDate: TextView = v.findViewById(R.id.headerDate)
        var tvPrinted: TextView = v.findViewById(R.id.tvPrinted)

        // buttons
        private var btnDetails: Button = v.findViewById(R.id.btnDetails)
        private var btnEditHeader: Button = v.findViewById(R.id.btnEditHeader)

        // params
        var sheetId: Int = -1
        var responsible: String? = null

        fun setOnClickListeners() {
            btnDetails.setOnClickListener(this)
            btnEditHeader.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when (view.id) {
                R.id.btnDetails -> {
                    val intent = Intent(context, DetailsActivity::class.java)
                    intent.putExtra("hoja_id", sheetId)
                    intent.putExtra("responsable", responsible)
                    context.startActivity(intent)
                }
                R.id.btnEditHeader -> showEditHeaderDialog(sheetId)
            }
        }

        private fun showEditHeaderDialog(sheetId: Int) {
            val fragmentManager = (context as PanelActivity).supportFragmentManager

            // sheet_id is required to => edit a specific header
            val newFragment = newInstance(sheetId)
            val transaction = fragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction.add(android.R.id.content, newFragment)
                    .addToBackStack(null).commit()
        }

    }

    fun setDataSet(dataSet: ArrayList<Sheet>) {
        this.dataSet = dataSet
        filteredDataSet = dataSet
        notifyDataSetChanged()
    }

    fun filterResponsibleStartsWith(query: String) {
        if (query.isEmpty()) {
            filteredDataSet = dataSet
        } else {
            filteredDataSet = ArrayList()
            for (item in dataSet) {
                if (item.responsible_user!!.toLowerCase().contains(query.toLowerCase()))
                    filteredDataSet.add(item)
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        // viewType 1 means active, 0 means pending
        return if (dataSet[position].pending)
            0
        else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_header, parent, false)

        if (viewType == 0) {
            // no active
            v.findViewById<View>(R.id.tvPending).visibility = View.VISIBLE
        } else {
            // is active
            v.findViewById<View>(R.id.tvPending).visibility = View.GONE
        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // get element from dataSet at this position
        // and replace the contents of the view
        val (id, fecha, _, _, responsible, _, _, _, _, _, _, _, printed) = filteredDataSet[position]

        holder.headerCode.text = id.toString()
        holder.responsibleName.text = responsible
        holder.headerDate.text = fecha

        holder.tvPrinted.visibility = if (printed) {
            View.VISIBLE
        } else {
            View.GONE
        }

        // set events
        holder.setOnClickListeners()

        // params needed to show the details
        holder.sheetId = id
        holder.responsible = responsible?.trim()
    }

    override fun getItemCount(): Int {
        return filteredDataSet.size
    }

    init {
        filteredDataSet = dataSet
    }
}