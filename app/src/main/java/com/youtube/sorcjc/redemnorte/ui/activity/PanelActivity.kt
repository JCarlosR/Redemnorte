package com.youtube.sorcjc.redemnorte.ui

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.youtube.sorcjc.redemnorte.Global
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.io.RedemnorteApiAdapter
import com.youtube.sorcjc.redemnorte.io.response.HojasResponse
import com.youtube.sorcjc.redemnorte.model.Sheet
import com.youtube.sorcjc.redemnorte.ui.adapter.HeaderAdapter
import com.youtube.sorcjc.redemnorte.ui.fragment.HeaderDialogFragment
import kotlinx.android.synthetic.main.activity_panel.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PanelActivity : AppCompatActivity(), View.OnClickListener, Callback<HojasResponse> {
    private var headerAdapter: HeaderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        val sheetsDataSet = ArrayList<Sheet>()
        loadInventorySheets()

        headerAdapter = HeaderAdapter(sheetsDataSet)
        recyclerView.adapter = headerAdapter

        setSupportActionBar(toolbar)

        fab.setOnClickListener(this)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fab.isShown) {
                    fab.hide()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        toolbar.setOnMenuItemClickListener(
                Toolbar.OnMenuItemClickListener { item ->
                    val id = item.itemId
                    if (id == R.id.search) {
                        if (layoutSearchBox.visibility == View.VISIBLE)
                            layoutSearchBox.visibility = View.GONE
                        else
                            layoutSearchBox.visibility = View.VISIBLE

                        return@OnMenuItemClickListener true
                    }
                    true
                })
        val btnQuery = findViewById<Button>(R.id.btnQuery)
        btnQuery.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun loadInventorySheets() {
        val username = Global.getFromSharedPreferences(this, "username")
        val call = RedemnorteApiAdapter.getApiService().getHojas(username)
        call.enqueue(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fab -> showCreateHeaderDialog()
            R.id.btnQuery -> headerAdapter!!.filterResponsibleStartsWith(etQueryHeader.text.toString().trim { it <= ' ' })
        }
    }

    private fun showCreateHeaderDialog() {
        val fragmentManager = supportFragmentManager

        // Empty hoja_id => Register new header
        val newFragment = HeaderDialogFragment.newInstance("")
        val transaction = fragmentManager.beginTransaction()

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit()
    }

    override fun onResponse(call: Call<HojasResponse>, response: Response<HojasResponse>) {
        if (response.isSuccessful) {
            val hojas = response.body()!!.sheets
            headerAdapter!!.setDataSet(hojas)
            Toast.makeText(this, getString(R.string.sheets_count_message) + hojas.size, Toast.LENGTH_SHORT).show()
        } else {
            try {
                val jObjError = JSONObject(response.errorBody()!!.string())
                Toast.makeText(this, jObjError.getString("message"), Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onFailure(call: Call<HojasResponse>, t: Throwable) {
        Toast.makeText(this, t.localizedMessage, Toast.LENGTH_SHORT).show()
    }
}