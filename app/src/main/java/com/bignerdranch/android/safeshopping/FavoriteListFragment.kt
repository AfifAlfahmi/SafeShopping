
package com.bignerdranch.android.safeshopping

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


private const val ARG_LAT = "lat"
private const val ARG_LONG = "long"
private const val DIALOG_MAP = "DIALOG_MAP"
private const val TAG = "ShopsListFragment"



class FavoriteListFragment : Fragment() {

    lateinit var tvLocation:TextView
    private lateinit var shopsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var favoritesListViewModel: FavoritesListViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        favoritesListViewModel = ViewModelProviders.of(this).get(FavoritesListViewModel::class.java)




    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_favorites_list, container, false)
        tvLocation = view.findViewById(R.id.tvLocation)
        shopsRecyclerView = view.findViewById(R.id.shopsRecyclerView)
        progressBar =view.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE

        val fetchShops = FetchShops()
        fetchShops.fetchShops()
        shopsRecyclerView.layoutManager = GridLayoutManager(context, 1)

        return view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



            Log.d(TAG,"network Lost")

        favoritesListViewModel.getFavoritesShops()
        favoritesListViewModel.favoritesListLiveData.observe(
                    viewLifecycleOwner,
                    Observer { shopsList ->

                        shopsRecyclerView.adapter = ShopAdapter(shopsList)
                        Log.d(TAG,shopsList.size.toString())


                    })



        Log.d(TAG,"onView Created")





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

        private lateinit var shop: FavoriteShop
        private val tvShpoName: TextView = itemView.findViewById(R.id.tvShopName)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val tvAddress: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvDistanceInKilo)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val colorLayout: ConstraintLayout = itemView.findViewById(R.id.colorLayout)
        private val imageViewShop: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener {
//                val action = ShopsListFragmentDirections.actionShopsListFragmentToShopFragment(shop)
//                findNavController().navigate(action)
            }

        }

        fun bindGalleryItem(shop: FavoriteShop) {
            this.shop=shop
            tvTitle.text = shop.title
            tvShpoName.text =shop.name
            ratingBar.rating = shop.rating.toFloat()
            tvAddress.text = shop.location.address
            tvCategory.text = shop.categories[0].title
            colorLayout.setBackgroundColor(Color.parseColor(shop.color))
            Picasso.get().load(shop.imageUrl).resize(100, 100 ).placeholder(R.drawable.ic_launcher_foreground)
                    .into(imageViewShop)

        }

        override fun onClick(v: View) {
        }


    }

    private inner class ShopAdapter(private val galleryItems: List<FavoriteShop>)
        : RecyclerView.Adapter<ShopHolder>() {

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int
        ): ShopHolder {
            val view = layoutInflater.inflate(R.layout.list_item_favorite, parent, false)
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


        fun newInstance() =
                ShopsListFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}
