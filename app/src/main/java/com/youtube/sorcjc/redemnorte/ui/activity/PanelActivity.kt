package com.youtube.sorcjc.redemnorte.ui.activity

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.io.MyApiAdapter
import com.youtube.sorcjc.redemnorte.model.Sheet
import com.youtube.sorcjc.redemnorte.ui.adapter.HeaderAdapter
import com.youtube.sorcjc.redemnorte.ui.fragment.HeaderDialogFragment
import com.youtube.sorcjc.redemnorte.util.PreferenceHelper
import com.youtube.sorcjc.redemnorte.util.PreferenceHelper.get
import com.youtube.sorcjc.redemnorte.util.toast
import kotlinx.android.synthetic.main.activity_panel.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PanelActivity : AppCompatActivity(), View.OnClickListener, Callback<ArrayList<Sheet>> {

    private val headerAdapter by lazy {
        HeaderAdapter()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel)

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        loadInventorySheets()

        recyclerView.adapter = headerAdapter

        setSupportActionBar(toolbar)
        hideAndDisplayToolbarAccordingly()

        fab.setOnClickListener(this)

        btnQuery.setOnClickListener(this)
    }

    private fun hideAndDisplayToolbarAccordingly() {
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun loadInventorySheets() {
        val username = preferences["username", ""]

        val call = MyApiAdapter.getApiService().getSheets(username)
        call.enqueue(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fab -> showCreateHeaderDialog()
            R.id.btnQuery ->
                headerAdapter.filterResponsibleStartsWith(etQueryHeader.text.toString().trim())
        }
    }

    private fun showCreateHeaderDialog() {
        val fragmentManager = supportFragmentManager

        // Empty sheetId => Create a new sheet
        val newFragment = HeaderDialogFragment.newInstance("")
        val transaction = fragmentManager.beginTransaction()

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit()
    }

    override fun onResponse(call: Call<ArrayList<Sheet>>, response: Response<ArrayList<Sheet>>) {
        if (response.isSuccessful) {
            val sheets = response.body()
            if (sheets != null) {
                headerAdapter.setDataSet(sheets)
                toast(getString(R.string.sheets_count_message) + sheets.size)
            }
        } else {
            toast(getString(R.string.error_format_server_response))
        }
    }

    override fun onFailure(call: Call<ArrayList<Sheet>>, t: Throwable) {
        toast(t.localizedMessage)
    }
}