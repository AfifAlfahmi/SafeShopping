package com.bignerdranch.android.safeshopping

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


private const val ARG_LAT = "lat"
private const val ARG_LONG = "long"
private const val DIALOG_MAP = "DIALOG_MAP"
private const val TAG = "ShopsListFragment"



class ShopsListFragment : Fragment() {

    private var long: Double? = null
    lateinit var tvLocation:TextView
    private lateinit var shopsRecyclerView: RecyclerView
    private lateinit var btnMap: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var shopsListViewModel: ShopsListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shopsListViewModel = ViewModelProviders.of(this).get(ShopsListViewModel::class.java)
        setHasOptionsMenu(true)
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
        progressBar =view.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE

         val fetchShops = FetchShops()
        fetchShops.fetchShops()
        shopsRecyclerView.layoutManager = GridLayoutManager(context, 1)
        btnMap.setOnClickListener {

            //findNavController(view).navigate(R.id.action_shopsListFragment_to_mapsFragment)

            val action = ShopsListFragmentDirections.actionShopsListFragmentToMapsFragment()
            findNavController().navigate(action)


        }
        return view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(!isInternetConnected()){
            Log.d(TAG,"network Lost")
            shopsListViewModel.getShopsFromRoom()
            shopsListViewModel.shopsLocalListLiveData.observe(
                viewLifecycleOwner,
                Observer { shopsList ->

                    shopsRecyclerView.adapter = ShopAdapter(shopsList)
                    Log.d(TAG,shopsList.size.toString())


                })
        }
        else{
            Log.d(TAG,"network  Available")

        }

        Log.d(TAG,"onView Created")

            tvLocation.text = lat.toString()
            Log.d(TAG,shopsListViewModel.lat.toString())


        shopsListViewModel.shopsListLiveData.observe(
                viewLifecycleOwner,
                Observer { shopsList ->


                                if(shopsList.size == 0 ){
                                    shopsListViewModel.fetchShops()
                                }





                    shopsRecyclerView.adapter = ShopAdapter(shopsList)
                    shopsListViewModel.addShops(shopsList)

                })



    }

    fun isInternetConnected() :Boolean{
        var internetConnection = true
        val cm: ConnectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE)    as ConnectivityManager
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()

        cm.registerNetworkCallback(
            builder.build(),
            object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {


                    internetConnection = true

                }

                override fun onLost(network: Network) {

                    internetConnection = false

                }
            })
        cm.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
        return internetConnection
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.shop_list_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView
        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(searchTerm: String): Boolean {
                    Log.d(TAG, "QueryTextSubmit: $searchTerm")
                    progressBar.visibility = View.VISIBLE
                    shopsListViewModel.searchShops(searchTerm)
                    searchView.onActionViewCollapsed()

                    shopsListViewModel.shopsListLiveData.observe(
                        viewLifecycleOwner,
                        Observer { shopsList ->

                            shopsRecyclerView.adapter = ShopAdapter(shopsList)
                            progressBar.visibility = View.GONE
                            shopsListViewModel.addShops(shopsList)


                        })
                    return true
                }

                override fun onQueryTextChange(queryText: String): Boolean {
                    Log.d(TAG, "QueryTextChange: $queryText")
                    return false
                }
            }
            )
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {R.id.menu_item_clear -> {
            //photoGalleryViewModel.fetchPhotos("")
             Toast.makeText(context,"cleared",Toast.LENGTH_LONG).show()
            true
        }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private inner class ShopHolder(view: View)
        : RecyclerView.ViewHolder(view),View.OnClickListener {

        private lateinit var shop: Shop
        private val tvShpoName: TextView = itemView.findViewById(R.id.tvShopName)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val imageViewShop: ImageView = itemView.findViewById(R.id.imageView)




        init {
            itemView.setOnClickListener {
                val action = ShopsListFragmentDirections.actionShopsListFragmentToShopFragment(shop)
                findNavController().navigate(action)



            }

        }

        fun bindGalleryItem(shop: Shop) {
            this.shop=shop

            tvShpoName.text =shop.name
            ratingBar.rating = shop.rating.toFloat()
            tvAddress.text = shop.location.address
            tvCategory.text = shop.categories[0].title
            Picasso.get().load(shop.imageUrl).resize(100, 100 ).placeholder(R.drawable.ic_launcher_foreground)
                .into(imageViewShop)

        }

        override fun onClick(v: View) {


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