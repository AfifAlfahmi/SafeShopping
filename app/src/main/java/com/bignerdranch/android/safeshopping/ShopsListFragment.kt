package com.bignerdranch.android.safeshopping

import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_LAT = "lat"
private const val ARG_LONG = "long"


class ShopsListFragment : Fragment() {

    private var lat: Double? = null
    private var long: Double? = null
    lateinit var tvLocation:TextView
    private lateinit var shopsRecyclerView: RecyclerView
    private lateinit var shopsListViewModel: ShopsListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lat = it.getDouble(ARG_LAT)
            long = it.getDouble(ARG_LONG)
        }
        shopsListViewModel = ViewModelProviders.of(this).get(ShopsListViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_shops_list, container, false)
        tvLocation = view.findViewById(R.id.tvLocation)
        shopsRecyclerView = view.findViewById(R.id.shopsRecyclerView)

        tvLocation.text = lat.toString()
         val fetchShops = FetchShops()
        fetchShops.fetchShops()
        shopsRecyclerView.layoutManager = GridLayoutManager(context, 1)

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




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

        init {
            itemView.setOnClickListener {
                //callbacks?.onPhotoSelected(galleryItem.url)
            }
        }

        fun bindGalleryItem(shop: Shop) {
            this.shop=shop

            tvShpoName.text =shop.name


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