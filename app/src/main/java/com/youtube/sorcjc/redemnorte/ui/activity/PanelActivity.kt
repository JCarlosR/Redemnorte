package com.youtube.sorcjc.redemnorte.ui.activity

import android.Manifest
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.io.MyApiAdapter
import com.youtube.sorcjc.redemnorte.model.Sheet
import com.youtube.sorcjc.redemnorte.ui.adapter.HeaderAdapter
import com.youtube.sorcjc.redemnorte.ui.fragment.HeaderDialog
import com.youtube.sorcjc.redemnorte.util.PreferenceHelper
import com.youtube.sorcjc.redemnorte.util.PreferenceHelper.get
import com.youtube.sorcjc.redemnorte.util.checkAndRequestPermission
import com.youtube.sorcjc.redemnorte.util.snack
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

    private var locationManager : LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel)

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadInventorySheets()

        recyclerView.adapter = headerAdapter

        setSupportActionBar(toolbar)
        hideAndDisplayToolbarAccordingly()

        fab.setOnClickListener(this)
        trackLocationIfPermissionIsGranted()

        btnQuery.setOnClickListener(this)
    }

    private fun trackLocationIfPermissionIsGranted() {
        checkAndRequestPermission(
                getString(R.string.dialog_location_title),
                getString(R.string.dialog_location_explanation),
                Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_LOCATION_PERMISSION,
                ::createLocationManagerReference
        )
    }

    private fun createLocationManagerReference() {
        // Create persistent LocationManager reference
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        try {
            // Request location updates
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch(ex: SecurityException) {
            Log.d("PanelActivity", "Security Exception, no location available")
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            postUserLocation(location.latitude, location.longitude)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun postUserLocation(lat: Double, lng: Double) {
        Log.d("PanelActivity", "longitude $lng, latitude $lat")

        MyApiAdapter.getApiService()
                .postUserLocation(preferences["user_id"], lat, lng)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Log.d("PanelActivity", "userLocation sent to the server")
                        } else {
                            Log.d("PanelActivity", "responseCode ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        toast(t.localizedMessage ?: "")
                    }
                })
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
                    if (id == R.id.itemSearch) {
                        actionSearch()
                    } else if (id == R.id.itemSignature) {
                        actionSignature()
                    }
                    return@OnMenuItemClickListener true
                })
    }

    private fun actionSearch() {
        if (layoutSearchBox.visibility == View.VISIBLE)
            layoutSearchBox.visibility = View.GONE
        else
            layoutSearchBox.visibility = View.VISIBLE
    }

    private fun actionSignature() {
        val intent = Intent(this, SignatureActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun loadInventorySheets() {
        val userId = preferences["user_id", -1]

        MyApiAdapter.getApiService()
                .getSheets(userId)
                .enqueue(this)
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
        val newFragment = HeaderDialog.newInstance(-1)
        val transaction = fragmentManager.beginTransaction()

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit()
    }

    override fun onResponse(call: Call<ArrayList<Sheet>>, response: Response<ArrayList<Sheet>>) {
        if (response.isSuccessful) {
            response.body()?.let { sheets ->
                headerAdapter.setDataSet(sheets)
                panelRootLayout.snack(getString(R.string.sheets_count_message, sheets.size))
            }
        } else {
            toast(getString(R.string.error_format_server_response))
        }
        showRecyclerView()
    }

    override fun onFailure(call: Call<ArrayList<Sheet>>, t: Throwable) {
        toast(t.localizedMessage ?: "")
        showRecyclerView()
    }

    private fun showRecyclerView() {
        progressBarSheets.visibility = View.GONE

        recyclerView.visibility = View.VISIBLE
        fab.show()
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 10100
    }
}