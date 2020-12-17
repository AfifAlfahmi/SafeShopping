package com.bignerdranch.android.safeshopping

import android.R.attr.radius
import android.app.ProgressDialog.show
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation


private const val ARG_LAT = "lat"
private const val ARG_LONG = "long"
private const val DIALOG_MAP = "DIALOG_MAP"
private const val TAG = "ShopsListFragment"



class ShopsListFragment : Fragment() {

    private var long: Double? = null
    lateinit var tvLocation:TextView
    private lateinit var shopsRecyclerView: RecyclerView
    private lateinit var btnMap: Button


    private lateinit var shopsListViewModel: ShopsListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shopsListViewModel = ViewModelProviders.of(this).get(ShopsListViewModel::class.java)

        arguments?.let {
            if(it.getDouble(ARG_LAT) != null){
                shopsListViewModel.lat = it.getDouble(ARG_LAT)
                shopsListViewModel.long = it.getDouble(ARG_LONG)

            }
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_shops_list, container, false)
        tvLocation = view.findViewById(R.id.tvLocation)
        shopsRecyclerView = view.findViewById(R.id.shopsRecyclerView)
        btnMap = view.findViewById(R.id.btnMap)


         val fetchShops = FetchShops()
        fetchShops.fetchShops()
        shopsRecyclerView.layoutManager = GridLayoutManager(context, 1)
        btnMap.setOnClickListener {

            findNavController(view).navigate(R.id.action_shopsListFragment_to_mapsFragment)




        }
        return view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         val fetchShops = FetchShops()
        fetchShops.fetchWeather()
        Log.d(TAG,"onViewCreated")

            tvLocation.text = lat.toString()
            Log.d(TAG,shopsListViewModel.lat.toString())



        shopsListViewModel.shopsListLiveData.observe(
                viewLifecycleOwner,
                Observer { shopsList ->


                                if(shopsList.size == 0 )

                                    shopsListViewModel.fetchShops()


                    shopsRecyclerView.adapter = ShopAdapter(shopsList)

                })



    }

    private inner class ShopHolder(view: View)
        : RecyclerView.ViewHolder(view),View.OnClickListener {

        private lateinit var shop: Shop
        private val tvShpoName: TextView = itemView.findViewById(R.id.tvShopName)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val tvReview: TextView = itemView.findViewById(R.id.tvReviews)
        private val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val imageViewShop: ImageView = itemView.findViewById(R.id.imageView)




        init {
            itemView.setOnClickListener {



            }

        }

        fun bindGalleryItem(shop: Shop) {
            this.shop=shop

            tvShpoName.text =shop.name
            ratingBar.rating = shop.rating.toFloat()
            tvReview.text = "${shop.numReviews} Review"
            tvAddress.text = shop.coordinates.latitude
            tvCategory.text = shop.categories[0].title
            Picasso.get().load(shop.imageUrl).resize(100, 100 ).placeholder(R.drawable.ic_launcher_foreground)
                .into(imageViewShop)

        }

        override fun onClick(v: View) {
//            callbacks?.onPhotoSelected(galleryItem.url)
        }


    }

    private inner class ShopAdapter(private val galleryItems: List<Shop>)
        : RecyclerView.Adapter<ShopHolder>() {

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int
        ): ShopHolder {
            val view = layoutInflater.inflate(R.layout.list_item_shop, parent, false)
            return ShopHolder(view)

        }

        override fun getItemCount(): Int = galleryItems.size

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onBindViewHolder(holder: ShopHolder, position: Int) {
            val galleryItem = galleryItems[position]



            holder.bindGalleryItem(galleryItem)
        }
    }

    companion object {
         var lat: Double? = null

        fun newInstance(lat: Double?, long: Double?) =
                ShopsListFragment().apply {
                    arguments = Bundle().apply {
                        if (lat != null) {
                            putDouble(ARG_LAT, lat)
                        }
                        if (long != null) {
                            putDouble(ARG_LONG, long)
                        }
                    }
                }
    }
}