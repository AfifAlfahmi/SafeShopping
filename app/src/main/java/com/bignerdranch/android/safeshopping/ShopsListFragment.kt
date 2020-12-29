@file:Suppress("DEPRECATION")

package com.bignerdranch.android.safeshopping

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.database.Cursor
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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.cursoradapter.widget.CursorAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


private const val TAG = "ShopsListFragment"
private const val KILO = 1000
private const val FIRST_ITEM = 0
private const val SHOP_IMG_WIDTH = 100
private const val SHOP_IMG_HEIGHT = 100
private const val SPAN_COUNT = 1

class ShopsListFragment : Fragment() {

    lateinit var tvLocation: TextView
    private lateinit var shopsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    lateinit var matSearchView: MaterialSearchView
    lateinit var toolBar: androidx.appcompat.widget.Toolbar
    private lateinit var shopsList: List<Shop>
    private var searchList = mutableListOf<String>()
    private lateinit var shopsListViewModel: ShopsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopsListViewModel = ViewModelProviders.of(this)
            .get(ShopsListViewModel::class.java)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shops_list, container, false)
        tvLocation = view.findViewById(R.id.tvLocation)
        shopsRecyclerView = view.findViewById(R.id.shopsRecyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        matSearchView = view.findViewById(R.id.search_view)
        toolBar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        (activity as AppCompatActivity).supportActionBar?.title = ""
        progressBar.visibility = View.GONE
        shopsRecyclerView.layoutManager = GridLayoutManager(context, SPAN_COUNT)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connectionManager: ConnectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectionManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if (!isConnected) {
            Log.d(TAG, " not connected")
            shopsListViewModel.getShopsFromRoom()
            shopsListViewModel.shopsLocalListLiveData.observe(
                viewLifecycleOwner,
                Observer { shopsList ->
                    shopsRecyclerView.adapter = ShopAdapter(shopsList)
                    Log.d(TAG, " local list " + shopsList.size)
                    Log.d(TAG, shopsList.size.toString())
                })
        } else {
            Log.d(TAG, " connected")
        }
        shopsListViewModel.getSearchTerms()
        observeSearchLiveData()
        Log.d(TAG, "onView Created")
        //  tvLocation.text = lat.toString()
        Log.d(TAG, shopsListViewModel.lat.toString())
        observeShopsLiveData()
    }

    fun observeShopsLiveData() {

        shopsListViewModel.shopsListLiveData.observe(
            viewLifecycleOwner,
            Observer { shops ->
                this.shopsList = shops
                shopsRecyclerView.adapter = ShopAdapter(shopsList)
                progressBar.visibility = View.GONE
                shopsListViewModel.deleteShops()
                shopsListViewModel.addShops(shopsList)
            })
    }

    fun observeSearchLiveData() {
        shopsListViewModel.searchListLiveData.observe(
            viewLifecycleOwner, Observer {
                searchList.clear()
                it.map {
                    searchList.add(it.searchTerm)
                    matSearchView.setSuggestions(searchList.toTypedArray())
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.shop_list_menu, menu)
        var search: Search
        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        matSearchView.setEllipsize(true)
        matSearchView.setMenuItem(searchItem)
        matSearchView.setVoiceSearch(true)

        matSearchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(searchTerm: String): Boolean {
                Log.d(TAG, "QueryTextSubmit: $searchTerm")
                progressBar.visibility = View.VISIBLE
                shopsListViewModel.searchShops(searchTerm)
                progressBar.visibility = View.VISIBLE
                matSearchView.closeSearch()
                search = Search(searchTerm)
                shopsListViewModel.addSearchTerm(search)
                observeSearchLiveData()
                observeShopsLiveData()

                return true
            }

            override fun onQueryTextChange(queryText: String): Boolean {
                matSearchView.setSuggestions(searchList.toTypedArray())
                Log.d(TAG, "QueryTextChange: $queryText")
                return false
            }
        }
        )
        matSearchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
            }

            override fun onSearchViewShown() {
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.menu_item_clear -> {
                val emptyList = resources.getStringArray(R.array.empty_list)
                searchList.clear()
                shopsListViewModel.deleteSearchTerms()
                matSearchView.setSuggestions(emptyList)
                Toast.makeText(context, "cleared", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private inner class ShopHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var shop: Shop
        private val tvShpoName: TextView = itemView.findViewById(R.id.tvShopName)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvDistance: TextView = itemView.findViewById(R.id.tvDistanceInKilo)
        private val imageViewShop: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener {
                val action = ShopsListFragmentDirections.actionShopsListFragmentToShopFragment(shop)
                findNavController().navigate(action)
            }
        }


        fun bindGalleryItem(shop: Shop) {
            this.shop = shop
            val distanceInKilo = shop.distanceInMeters / KILO
            val roundedDistanceInKilo = String.format(
                getString(R.string.kilo_format),
                distanceInKilo
            )
            tvShpoName.text = shop.name
            ratingBar.rating = shop.rating.toFloat()
            tvDistance.text = roundedDistanceInKilo + getString(R.string.km)
            tvCategory.text = getString(R.string.category) + shop.categories[FIRST_ITEM].title
            if (!shop.imageUrl.isEmpty()) {
                Picasso.get().load(shop.imageUrl).resize(SHOP_IMG_WIDTH, SHOP_IMG_HEIGHT)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imageViewShop)
            }
        }

        override fun onClick(v: View) {

        }
    }

    private inner class ShopAdapter(private val galleryItems: List<Shop>) :
        RecyclerView.Adapter<ShopHolder>() {

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onCreateViewHolder(
            parent: ViewGroup,
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
    }
}