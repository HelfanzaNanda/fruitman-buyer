package com.one.fruitmanbuyer.ui.maps

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager
import com.one.fruitmanbuyer.R
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity() {

    private lateinit var markerManager : MarkerViewManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instanceMapbox()
        setContentView(R.layout.activity_maps)
        setUpMaps(savedInstanceState)
    }

    private fun setUpMaps(savedInstanceState: Bundle?){
        map_view.onCreate(savedInstanceState)
        map_view.getMapAsync{ mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                markerManager = MarkerViewManager(map_view, mapboxMap)
                addMarker(LatLng(getPassedLat().toDouble(), getPassedLng().toDouble()))
            }
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(getPassedLat().toDouble(), getPassedLng().toDouble()), 12.0))
        }
    }

    private fun iconMarker(): ImageView {
        return ImageView(this@MapsActivity).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT)
            setImageBitmap(BitmapFactory.decodeResource(this@MapsActivity.resources, R.drawable.mapbox_marker_icon_default))
        }
    }

    private fun getPassedLat() = intent.getStringExtra("LAT")
    private fun getPassedLng() = intent.getStringExtra("LNG")
    private fun instanceMapbox() = Mapbox.getInstance(this@MapsActivity, getString(R.string.mapbox_access_token))

    private fun addMarker(latlng: LatLng){
        val parent = LinearLayout(this@MapsActivity)
        parent.apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            orientation = LinearLayout.VERTICAL
            addView(iconMarker())
        }
        val marker = MarkerView(latlng, parent)
        markerManager.addMarker(marker)
    }

    override fun onStart() {
        super.onStart()
        map_view?.onStart()
    }

    override fun onResume() {
        super.onResume()
        map_view?.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_view?.onPause()
    }

    override fun onStop() {
        super.onStop()
        map_view?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view?.onDestroy()
    }
}
