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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.safeshopping.yelpapi.Category
import com.bignerdranch.android.safeshopping.yelpapi.ShopLocation
import com.squareup.picasso.Picasso


private const val TAG = "FavoriteListFragment"
private const val SHOP_IMG_WIDTH = 125
private const val SHOP_IMG_HEIGHT = 125
private const val SPAN_COUNT = 1
private const val KILO = 1000
private const val FIRST_ITEM = 0

class FavoriteListFragment : Fragment() {

    lateinit var tvLocation: TextView
    private lateinit var shopsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var favoritesListViewModel: FavoritesListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        favoritesListViewModel = ViewModelProviders.of(this)
            .get(FavoritesListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites_list, container, false)
        tvLocation = view.findViewById(R.id.tvLocation)
        shopsRecyclerView = view.findViewById(R.id.shopsRecyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        shopsRecyclerView.layoutManager = GridLayoutManager(context, SPAN_COUNT)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesListViewModel.getFavoritesShops()
        favoritesListViewModel.favoritesListLiveData.observe(
            viewLifecycleOwner,
            Observer { shopsList ->
                shopsRecyclerView.adapter = ShopAdapter(shopsList)
                Log.d(TAG, shopsList.size.toString())
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.shop_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_clear -> {
                //photoGalleryViewModel.fetchPhotos("")
                Toast.makeText(context, "cleared", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private inner class ShopHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var shop: FavoriteShop
        private val tvShpoName: TextView = itemView.findViewById(R.id.tvShopName)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val tvDistance: TextView = itemView.findViewById(R.id.tvDistanceInKilo)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val colorLayout: ConstraintLayout = itemView.findViewById(R.id.colorLayout)
        private val imageViewShop: ImageView = itemView.findViewById(R.id.imageView)
        private val imgViewDelete: ImageView = itemView.findViewById(R.id.imgDelete)

        init {
            itemView.setOnClickListener {
                val shop = Shop(shop.id,shop.name,shop.rating,
                    shop.price,shop.numReviews,shop.distanceInMeters,shop.imageUrl
                    ,shop.categories,shop.location,shop.coordinates)
               val action = FavoriteListFragmentDirections.actionFavoriteListFragmentToShopFragment(shop)
               findNavController().navigate(action)
            }
            imgViewDelete.setOnClickListener {
                favoritesListViewModel.deleteFavoriteShop(shop.id)

            }
        }

        fun bindGalleryItem(shop: FavoriteShop) {
            this.shop = shop
            val distanceInKilo = shop.distanceInMeters / KILO
            val roundedDistanceInKilo = String.format(
                getString(R.string.kilo_format),
                distanceInKilo
            )
            tvTitle.text = shop.title
            tvShpoName.text = shop.name
            ratingBar.rating = shop.rating.toFloat()
            tvDistance.text = roundedDistanceInKilo + getString(R.string.km)
            tvCategory.text = getString(R.string.category) + shop.categories[FIRST_ITEM].title
            colorLayout.setBackgroundColor(Color.parseColor(shop.color))
            Picasso.get().load(shop.imageUrl).resize(SHOP_IMG_WIDTH, SHOP_IMG_HEIGHT)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageViewShop)
        }

        override fun onClick(v: View) {

        }
    }

    private inner class ShopAdapter(private val galleryItems: List<FavoriteShop>) :
        RecyclerView.Adapter<ShopHolder>() {

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ShopHolder {
            val view = layoutInflater.inflate(
                R.layout.list_item_favorite,
                parent,
                false
            )
            return ShopHolder(view)

        }

        override fun getItemCount(): Int = galleryItems.size

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onBindViewHolder(holder: ShopHolder, position: Int) {
            val galleryItem = galleryItems[position]
            holder.bindGalleryItem(galleryItem)
        }
    }


}
