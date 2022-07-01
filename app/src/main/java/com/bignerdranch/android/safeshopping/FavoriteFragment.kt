package com.bignerdranch.android.safeshopping

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_favorite.*


private const val ARG_SHOP = "shop"

class FavoriteFragment : BottomSheetDialogFragment() {
    private lateinit var etCategory: EditText
    private lateinit var btnGreen: Button
    private lateinit var btnBlue: Button
    private lateinit var btnRed: Button
    private lateinit var btnYellow: Button
    private lateinit var btnSave: Button
    private lateinit var layoutColor: LinearLayout
    private lateinit var shop: Shop
    private lateinit var favoriteShop: FavoriteShop
    private var color = "#14ba09"
    private var greenColor = "#14ba09"
    private var blueColor = "#043be1"
    private var redColor = "#d10505"
    private var yellowColor = "#f2eb0b"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var parcelableShop: Shop? = arguments?.getParcelable(ARG_SHOP)
        if (parcelableShop != null) {
            shop = parcelableShop
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_favorite,
            container,
            false
        )
        etCategory = view.findViewById(R.id.etCategory)
        btnGreen = view.findViewById(R.id.btnGreen)
        btnBlue = view.findViewById(R.id.btnBlue)
        btnRed = view.findViewById(R.id.btnRed)
        btnYellow = view.findViewById(R.id.btnyellow)
        layoutColor = view.findViewById(R.id.layoutColor)
        btnSave = view.findViewById(R.id.btnSave)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shopRepository = ShopRepository.get()
        var title = "Resturant"

        btnGreen.setOnClickListener {
            layoutColor.setBackgroundColor(Color.GREEN)
            color =  greenColor

        }
        btnBlue.setOnClickListener {
            layoutColor.setBackgroundColor(Color.BLUE)
            color = blueColor
        }
        btnRed.setOnClickListener {
            layoutColor.setBackgroundColor(Color.RED)
            color = redColor
        }
        btnyellow.setOnClickListener {
            layoutColor.setBackgroundColor(Color.YELLOW)
            color = yellowColor
        }
        btnSave.setOnClickListener {
            title = etCategory.text.toString()

            favoriteShop = FavoriteShop(
                shop.id, shop.name, color, title, shop.rating
                , shop.numReviews, shop.distanceInMeters, shop.imageUrl,
                shop.categories, shop.coordinates
            )
            shopRepository.addFavoriteShop(favoriteShop)
            dismiss()
        }
    }

    companion object {
        fun newInstance(shop: Shop): FavoriteFragment {
            val args = Bundle().apply {
                putParcelable(ARG_SHOP, shop)
            }
            return FavoriteFragment().apply {
                arguments = args
            }
        }
    }
}