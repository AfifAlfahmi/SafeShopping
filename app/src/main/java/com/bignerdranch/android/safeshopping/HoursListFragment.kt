package com.bignerdranch.android.safeshopping

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.safeshopping.weatherapi.Hour
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

private const val ARG_HOURS = "hours"
class HoursListFragment:BottomSheetDialogFragment() {
    lateinit var hoursList:ArrayList<Hour>
    lateinit var tvDay:TextView
    private lateinit var hoursRecyclerView: RecyclerView
    val parser =  SimpleDateFormat("yyyy-MM-dd HH:mm")
    val timeFormatter = SimpleDateFormat("HH")
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         hoursList = arguments?.getParcelableArrayList<Hour>(ARG_HOURS) as ArrayList<Hour>

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hours_list, container, false)
        hoursRecyclerView = view.findViewById(R.id.hoursRecyclerView)
        tvDay = view.findViewById(R.id.tvDay)

        hoursRecyclerView.layoutManager = GridLayoutManager(context, 1)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       // tvDay.text = formattedDate
        val formattedDate = dateFormatter.format(parser.parse(hoursList[0].time))
        tvDay.text = formattedDate
        hoursRecyclerView.adapter = HourAdapter(hoursList)

    }

    override fun onStart() {
        super.onStart()


    }
    private inner class HourHolder(view: View)
        : RecyclerView.ViewHolder(view),View.OnClickListener {

        private lateinit var hour: Hour
        private val tvHourTemp: TextView = itemView.findViewById(R.id.tvHourTemp)
        private val tvHourTime: TextView = itemView.findViewById(R.id.tvHourTime)
        private val tvHourCondition: TextView = itemView.findViewById(R.id.tvHourCondition)
        private val imgViewHour: ImageView = itemView.findViewById(R.id.imgViewHour)
        private val imgViewShow: ImageView = itemView.findViewById(R.id.imgViewShow)

        private val expandedLayout: ConstraintLayout = itemView.findViewById(R.id.expandedLayout)




        init {
            itemView.setOnClickListener {

            }


        }

        fun bindHourItem(hour: Hour,position: Int) {
            this.hour=hour
          var isExpanded = hour.isExpanded
            if(isExpanded){
                expandedLayout.visibility = View.VISIBLE
                imgViewShow.setImageResource(R.mipmap.up_icon)

            }
            else{
                expandedLayout.visibility = View.GONE
                imgViewShow.setImageResource(R.mipmap.down)


            }

            val formattedHour = timeFormatter.format(parser.parse(hour.time))
            tvHourTime.text = formattedHour
            tvHourTemp.text =hour.temp

            tvHourCondition.text =hour.condition.text


            Picasso.get().load("https:"+hour.condition.icon).resize(100, 100 ).placeholder(R.drawable.ic_launcher_foreground)
                    .into(imgViewHour)
            imgViewShow.setOnClickListener {
                hour.isExpanded = !hour.isExpanded
                hoursRecyclerView.adapter?.notifyItemChanged(position)
               // imgViewShow.setImageResource(R.mipmap.up_icon)


            }

        }

        override fun onClick(v: View) {
        }


    }
    private inner class HourAdapter(private val hours: List<Hour>)
        : RecyclerView.Adapter<HoursListFragment.HourHolder>() {

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int
        ): HoursListFragment.HourHolder {
            val view = layoutInflater.inflate(R.layout.list_item_hour, parent, false)
            return HourHolder(view)

        }

        override fun getItemCount(): Int = hours.size

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onBindViewHolder(holder: HoursListFragment.HourHolder, position: Int) {
            val hour = hours[position]



            holder.bindHourItem(hour,position)
        }
    }

    companion object {
        fun newInstance(hoursList:ArrayList<Hour>): HoursListFragment {
            val args = Bundle().apply {
                putParcelableArrayList(ARG_HOURS, hoursList)

            }
            return HoursListFragment().apply {
                arguments = args
            }
        }
    }

}