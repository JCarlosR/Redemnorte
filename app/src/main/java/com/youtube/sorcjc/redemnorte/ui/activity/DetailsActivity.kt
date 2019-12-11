package com.youtube.sorcjc.redemnorte.ui.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.FragmentTransaction
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.io.RedemnorteApiAdapter
import com.youtube.sorcjc.redemnorte.io.response.BienesResponse
import com.youtube.sorcjc.redemnorte.model.Item
import com.youtube.sorcjc.redemnorte.ui.adapter.DetailAdapter
import com.youtube.sorcjc.redemnorte.ui.fragment.DetailDialogFragment
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DetailsActivity : AppCompatActivity(), View.OnClickListener, Callback<BienesResponse> {
    private var detailAdapter: DetailAdapter? = null
    private var hoja_id: String? = null
    private var responsable: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val extras = intent.extras
        if (extras != null) {
            hoja_id = extras.getString("hoja_id")
            responsable = extras.getString("responsable")
        }
        val recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        val myDataSet = ArrayList<Item>()
        detailAdapter = DetailAdapter(myDataSet, hoja_id, responsable)
        recyclerView.adapter = detailAdapter
        cargarBienes()
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = "Sheet $hoja_id"
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener(this)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fab.isShown) fab.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) fab.show()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    fun cargarBienes() {
        val call = RedemnorteApiAdapter.getApiService().getBienes(hoja_id)
        call.enqueue(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fab -> showCreateDetailDialog()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCreateDetailDialog() {
        val fragmentManager = supportFragmentManager
        val newFragment = DetailDialogFragment.newInstance(hoja_id, "", responsable)
        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit()
    }

    override fun onResponse(call: Call<BienesResponse>, response: Response<BienesResponse>) {
        if (response.isSuccessful) {
            val bienes = response.body()!!.bienes
            detailAdapter!!.setDataSet(bienes)
            Toast.makeText(this, "Cantidad de bienes => " + bienes.size, Toast.LENGTH_SHORT).show()
        } else {
            try {
                val jObjError = JSONObject(response.errorBody()!!.string())
                Toast.makeText(this, jObjError.getString("message"), Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onFailure(call: Call<BienesResponse>, t: Throwable) {
        Toast.makeText(this, t.localizedMessage, Toast.LENGTH_SHORT).show()
    }
}