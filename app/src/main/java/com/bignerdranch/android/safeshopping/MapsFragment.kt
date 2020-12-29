package com.bignerdranch.android.safeshopping

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception


private const val TAG = "MapsFragment"
private const val SHOP_IMG_WIDTH = 100
private const val SHOP_IMG_HEIGHT = 100

class MapsFragment : Fragment() {
    private lateinit var btnNavToShops: Button
    var shopsList = mutableListOf<Shop>()
    lateinit var mapView: View
   private val zoom = 10.0f
    private lateinit var mapFragmentViewModel: MapsFragmentViewModel


    private val callback = OnMapReadyCallback { googleMap ->
        mapFragmentViewModel = ViewModelProviders.of(this).get(MapsFragmentViewModel::class.java)


        Log.d(TAG, "before click" + ShopsListFragment.lat)
        googleMap.setOnMapClickListener {
            ShopsListFragment.lat = it.longitude
            Log.d(TAG, "after click" + ShopsListFragment.lat)

            Toast.makeText(context, "map clicked in ${it.longitude}", Toast.LENGTH_LONG).show()


        }
        googleMap.setOnMarkerClickListener {
            for (shopItem in shopsList) {
                if (shopItem.id == it.title) {


                    navigateToShopFragment(shopItem)

                }

            }
            true

        }

        mapFragmentViewModel.shopsListLiveData.observe(
            viewLifecycleOwner,
            Observer { shopsList ->
                this.shopsList.addAll(shopsList)
                drawMarker(googleMap)

            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mapView = inflater.inflate(R.layout.fragment_maps, container, false)
        btnNavToShops = mapView.findViewById(R.id.btnNavToShops)
        btnNavToShops.setOnClickListener {
            Navigation.findNavController(mapView)
                .navigate(R.id.action_mapsFragment_to_shopsListFragment)


        }

        return mapView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


    }

    private fun navigateToShopFragment(shop: Shop) {


        try {
            val action = MapsFragmentDirections.actionMapsFragmentToShopFragment(shop)
            findNavController().navigate(action)

        } catch (ex: Exception) {


        }


    }

    private fun drawMarker(googleMap: GoogleMap) {
        for (shopItem in shopsList) {

            Glide.with(requireActivity())
                .asBitmap()
                .load(shopItem.imageUrl)
                .circleCrop()
                .apply(RequestOptions().override(SHOP_IMG_WIDTH, SHOP_IMG_HEIGHT))
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        //
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(
                                    LatLng(
                                        shopItem.coordinates.latitude.toDouble(),
                                        shopItem.coordinates.longitude.toDouble()
                                    )
                                )
                                .title(shopItem.id)
                                .icon(
                                    BitmapDescriptorFactory.fromBitmap(resource)
                                )


                        )
                        googleMap?.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(LatLng(
                                    shopItem.coordinates.latitude.toDouble(),
                                    shopItem.coordinates.longitude.toDouble()
                                ), zoom
                            )
                        )

                    }
                })


        }
    }


}