package com.anything.chattingapp.view.main.ui.stories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.anything.chattingapp.BuildConfig
import com.anything.chattingapp.R
import com.anything.chattingapp.view.main.ui.MainFragment
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import org.osmdroid.config.Configuration
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.compass.CompassOverlay



class StoriesFragment : MainFragment() {

    override fun onLogOut() {

    }

    override fun onQueryTextSubmit(p0: String?): Boolean {

        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
    }


    private lateinit var storiesViewModel: StoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        storiesViewModel =
            ViewModelProviders.of(this).get(StoriesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        view.mapview.setBuiltInZoomControls(true)
        view.mapview.setMultiTouchControls(true)
        val provider = GpsMyLocationProvider(context)
        provider.locationUpdateMinDistance = 6000f
        val overlay = MyLocationNewOverlay(provider, view.mapview)
        val compassOverlay = CompassOverlay(context, InternalCompassOrientationProvider(context), view.mapview)
        compassOverlay.enableCompass()
        overlay.enableMyLocation()
        view.mapview.overlays.add(overlay)
        view.mapview.overlays.add(compassOverlay)

        overlay.runOnFirstFix{
            activity?.runOnUiThread {
                view.mapview.controller.animateTo(overlay.myLocation)
                view.mapview.controller.setZoom(18)
            }
        }
    }
}