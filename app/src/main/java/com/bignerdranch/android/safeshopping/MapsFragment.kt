package com.bignerdranch.android.safeshopping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
private const val TAG = "MapsFragment"

class MapsFragment : BottomSheetDialogFragment() {
    private lateinit var shopsListViewModel: ShopsListViewModel
    private lateinit var btnNavToShops: Button


    private val callback = OnMapReadyCallback { googleMap ->
        shopsListViewModel = ViewModelProviders.of(this).get(ShopsListViewModel::class.java)


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

         var view =inflater.inflate(R.layout.fragment_maps, container, false)
        btnNavToShops = view.findViewById(R.id.btnNavToShops)

        btnNavToShops.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_mapsFragment_to_shopsListFragment)



        }

        return view
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