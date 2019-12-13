package com.youtube.sorcjc.redemnorte.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.model.Item
import com.youtube.sorcjc.redemnorte.ui.activity.DetailsActivity
import com.youtube.sorcjc.redemnorte.ui.fragment.DetailDialogFragment.Companion.newInstance
import com.youtube.sorcjc.redemnorte.ui.fragment.ShowDetailDialog.Companion.newInstance
import java.util.*

class DetailAdapter
(private var dataSet: ArrayList<Item>, private val sheetId: Int, private val responsible: String) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        // context
        var context: Context = v.context
        // text views
        var tvDetailQR: TextView = v.findViewById<View>(R.id.tvDetailQR) as TextView
        var tvDescription: TextView = v.findViewById<View>(R.id.tvDescription) as TextView
        var tvPatrimonial: TextView = v.findViewById<View>(R.id.tvPatrimonial) as TextView
        // buttons
        private var btnShowProduct: Button = v.findViewById<View>(R.id.btnShowProduct) as Button
        private var btnEditProduct: Button = v.findViewById<View>(R.id.btnEditProduct) as Button
        // id
        var sheetId: Int = -1
        var qrCode: String = ""
        var responsible: String = ""

        fun setOnClickListeners() {
            btnShowProduct.setOnClickListener(this)
            btnEditProduct.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when (view.id) {
                R.id.btnShowProduct -> showProductDataDialog()
                R.id.btnEditProduct -> editProductDialog()
            }
        }

        private fun showProductDataDialog() {
            val fragmentManager = (context as DetailsActivity).supportFragmentManager
            val newFragment = newInstance(sheetId, qrCode)
            val transaction = fragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.add(android.R.id.content, newFragment)
                    .addToBackStack(null).commit()
        }

        private fun editProductDialog() {
            val fragmentManager = (context as DetailsActivity).supportFragmentManager
            val newFragment = newInstance(sheetId, qrCode, responsible)
            val transaction = fragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.add(android.R.id.content, newFragment)
                    .addToBackStack(null).commit()
        }

    }

    fun setDataSet(dataSet: ArrayList<Item>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_detail, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (qr, patrimonial, denomination) = dataSet[position]

        holder.qrCode = qr
        holder.sheetId = sheetId
        holder.responsible = responsible

        holder.tvDetailQR.text = holder.context.getString(R.string.label_qr_code, qr)
        holder.tvDescription.text = denomination
        holder.tvPatrimonial.text = holder.context.getString(R.string.label_patrimonial_code, patrimonial)

        // set events
        holder.setOnClickListeners()
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}