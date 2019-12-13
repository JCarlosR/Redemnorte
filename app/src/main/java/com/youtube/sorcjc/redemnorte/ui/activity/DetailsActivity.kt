package com.youtube.sorcjc.redemnorte.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.io.MyApiAdapter
import com.youtube.sorcjc.redemnorte.model.Item
import com.youtube.sorcjc.redemnorte.ui.adapter.DetailAdapter
import com.youtube.sorcjc.redemnorte.ui.fragment.DetailDialogFragment
import com.youtube.sorcjc.redemnorte.util.toast
import kotlinx.android.synthetic.main.activity_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DetailsActivity : AppCompatActivity(), View.OnClickListener, Callback<ArrayList<Item>> {

    private val myDataSet by lazy {
        ArrayList<Item>()
    }

    private val detailAdapter by lazy {
        DetailAdapter(myDataSet, sheetId, responsible)
    }

    private var sheetId: Int = -1
    private var responsible: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        intent.extras?.let {
            sheetId = it.getInt("hoja_id")
            responsible = it.getString("responsable", "")
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = detailAdapter
        loadItems()

        toolbar.title = getString(R.string.title_activity_details) + sheetId
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        fab.setOnClickListener(this)
        hideFabOnRecyclerViewScroll()
    }

    private fun hideFabOnRecyclerViewScroll()
    {
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
        val call = MyApiAdapter.getApiService().getItems(sheetId)
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
        val newFragment = DetailDialogFragment.newInstance(sheetId, -1, responsible)
        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit()
    }

    override fun onResponse(call: Call<ArrayList<Item>>, response: Response<ArrayList<Item>>) {
        if (response.isSuccessful) {
            response.body()?.let {
                detailAdapter.setDataSet(it)
                toast(getString(R.string.items_count_message) + " " + it.size)
            }
        } else {
            toast(getString(R.string.error_format_server_response))
        }
    }

    override fun onFailure(call: Call<ArrayList<Item>>, t: Throwable) {
        toast(t.localizedMessage ?: "")
    }
}