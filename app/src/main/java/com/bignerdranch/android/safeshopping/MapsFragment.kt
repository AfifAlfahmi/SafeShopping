package com.bignerdranch.android.safeshopping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
private const val TAG = "MapsFragment"

class MapsFragment : BottomSheetDialogFragment() {
    private lateinit var shopsListViewModel: ShopsListViewModel

    private val callback = OnMapReadyCallback { googleMap ->
        shopsListViewModel = ViewModelProviders.of(this).get(ShopsListViewModel::class.java)

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        Log.d(TAG,"before click"+ShopsListFragment.lat)
        googleMap.setOnMapClickListener {
            ShopsListFragment.lat = it.longitude
            Log.d(TAG,"after click"+ShopsListFragment.lat)

            Toast.makeText(context,"map clicked in ${it.longitude}",Toast.LENGTH_LONG).show()


        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
//        if (view != null) {
//            val parent = view!!.parent as ViewGroup
//            parent?.removeView(view)
//        }
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
    companion object {
        fun newInstance(): MapsFragment {

            return MapsFragment()
        }
    }
}