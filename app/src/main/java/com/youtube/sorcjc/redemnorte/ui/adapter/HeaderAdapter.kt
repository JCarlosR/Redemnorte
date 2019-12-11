package com.youtube.sorcjc.redemnorte.ui.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.model.Sheet
import com.youtube.sorcjc.redemnorte.ui.activity.DetailsActivity
import com.youtube.sorcjc.redemnorte.ui.activity.PanelActivity
import com.youtube.sorcjc.redemnorte.ui.fragment.HeaderDialogFragment.Companion.newInstance
import java.util.*

class HeaderAdapter(private var dataSet: ArrayList<Sheet>) : RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {
    private var filteredDataSet: ArrayList<Sheet>

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        // context
        var context: Context = v.context
        // text views
        var headerCode: TextView = v.findViewById(R.id.headerCode)
        var responsibleName: TextView = v.findViewById(R.id.responsibleName)
        var headerDate: TextView = v.findViewById(R.id.headerDate)
        var tvImpreso: TextView = v.findViewById(R.id.tvImpreso)
        // buttons
        var btnDetails: Button = v.findViewById(R.id.btnDetails)
        var btnEditHeader: Button = v.findViewById(R.id.btnEditHeader)
        // params
        var hoja_id: String? = null
        var responsable: String? = null
        fun setOnClickListeners() {
            btnDetails.setOnClickListener(this)
            btnEditHeader.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when (view.id) {
                R.id.btnDetails -> {
                    val intent = Intent(context, DetailsActivity::class.java)
                    intent.putExtra("hoja_id", hoja_id)
                    intent.putExtra("responsable", responsable)
                    context.startActivity(intent)
                }
                R.id.btnEditHeader -> showEditHeaderDialog(hoja_id)
            }
        }

        private fun showEditHeaderDialog(hoja_id: String?) {
            val fragmentManager = (context as PanelActivity).supportFragmentManager
            // hoja_id is required to => edit a specific header
            val newFragment = newInstance(hoja_id)
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
                if (item.responsable!!.toLowerCase().contains(query.toLowerCase())) filteredDataSet.add(item)
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        var active = 1
        if (dataSet[position].activo == "0") active = 0
        return active
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder { // create a new view

        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_header, parent, false)

        if (viewType == 0) {
            // no active
            v.findViewById<View>(R.id.tvInactive).visibility = View.VISIBLE
        } else {
            // is active
            v.findViewById<View>(R.id.tvInactive).visibility = View.GONE
        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // get element from dataSet at this position
        // and replace the contents of the view
        val (id, fecha, _, _, responsable, _, _, _, _, _, _, _, impreso) = filteredDataSet[position]
        holder.headerCode.text = id
        holder.responsibleName.text = responsable
        holder.headerDate.text = fecha
        if (impreso!!.trim { it <= ' ' } == "1") {
            holder.tvImpreso.visibility = View.VISIBLE
        } else {
            holder.tvImpreso.visibility = View.GONE
        }
        // set events
        holder.setOnClickListeners()
        // params needed to show the details
        holder.hoja_id = id
        holder.responsable = responsable!!.trim { it <= ' ' }
    }

    override fun getItemCount(): Int {
        return filteredDataSet.size
    }

    init {
        filteredDataSet = dataSet
    }
}