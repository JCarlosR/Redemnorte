package com.youtube.sorcjc.redemnorte.ui.activity

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.io.MyApiAdapter
import com.youtube.sorcjc.redemnorte.model.Item
import com.youtube.sorcjc.redemnorte.ui.adapter.DetailAdapter
import com.youtube.sorcjc.redemnorte.ui.fragment.DetailDialogFragment
import com.youtube.sorcjc.redemnorte.util.toast
import kotlinx.android.synthetic.main.activity_details.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DetailsActivity : AppCompatActivity(), View.OnClickListener, Callback<ArrayList<Item>> {

    private val myDataSet by lazy {
        ArrayList<Item>()
    }

    private val detailAdapter by lazy {
        DetailAdapter(myDataSet, hoja_id, responsable)
    }

    private var hoja_id: String? = ""
    private var responsable: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val extras = intent.extras
        if (extras != null) {
            hoja_id = extras.getString("hoja_id")
            responsable = extras.getString("responsable")
        }

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.adapter = detailAdapter
        loadItems()

        toolbar.title = "Sheet $hoja_id"
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

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

    fun loadItems() {
        val call = MyApiAdapter.getApiService().getItems(hoja_id)
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

    override fun onResponse(call: Call<ArrayList<Item>>, response: Response<ArrayList<Item>>) {
        if (response.isSuccessful) {
            response.body()?.let {
                detailAdapter.setDataSet(it)
                toast(getString(R.string.sheets_count_message) + it.size)
            }
        } else {
            toast(getString(R.string.error_format_server_response))
        }
    }

    override fun onFailure(call: Call<ArrayList<Item>>, t: Throwable) {
        toast(t.localizedMessage)
    }
}