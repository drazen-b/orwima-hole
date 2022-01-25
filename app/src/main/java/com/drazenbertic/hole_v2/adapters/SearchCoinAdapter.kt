package com.drazenbertic.hole_v2.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.drazenbertic.hole_v2.CoinInformationActivity
import com.drazenbertic.hole_v2.R
import com.drazenbertic.hole_v2.dataclasses.Coin
import com.drazenbertic.hole_v2.dataclasses.singleCoin.SingleCoin
import com.drazenbertic.hole_v2.interfaces.CoinGeckoEndPoints
import com.drazenbertic.hole_v2.objects.ServiceBuilder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_coin_information.*
import kotlinx.android.synthetic.main.activity_coin_information.view.*
import kotlinx.android.synthetic.main.rvelement_simplecoin.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchCoinAdapter ( private var coins: List<Coin> ): RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {
    var coinsFiltered: List<Coin> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CoinViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rvelement_simplecoin, parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is CoinViewHolder -> {
                holder.bind(coins[position])


            }
        }
    }

    override fun getItemCount(): Int {
        return coins.size
    }


    class CoinViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(coins: Coin) {

            itemView.tvCoinSymbol.text = coins.symbol
            itemView.tvCoinName.text = coins.name

                itemView.setOnClickListener {

                    val intent = Intent(itemView.context, CoinInformationActivity::class.java)
                    intent.putExtra("coinID", coins.id)
                    itemView.context.startActivity(intent)
                }
        }
    }

//
//
//
//
//
//                    var data: String = String()
//                    val intent = Intent(itemView.context, CoinInformationActivity::class.java)
//                    callCoinApi(coins.symbol)
//                    intent.putExtra("DATA", data)
//                    itemView.context.startActivity(intent)
//
//
//            }
//        }
//
//        private fun callCoinApi(id: String) {
//
//
//            val request = ServiceBuilder.buildService(CoinGeckoEndPoints::class.java)
//            val call = request.getCoinInformation(id)
//
//            call.enqueue(object : Callback<String> {
//                override fun onResponse(call: Call<String>, response: Response<String>) {
//                    if(response.isSuccessful) {
//
//                        data = (response.body() as String)
//
//                    }
//                }
//                override fun onFailure(call: Call<String>, t: Throwable)
//                {
//                    Log.d("FAILLLL", t.message.toString())
//                }
//            })


//        }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) coinsFiltered = coins else {
                    val filteredList = ArrayList<Coin>()
                    coins
                        .filter {
                            (it.id.contains(constraint!!)) or
                                    (it.name.contains(constraint))

                        }
                        .forEach { filteredList.add(it) }
                    coinsFiltered = filteredList

                }
                return FilterResults().apply { values = coinsFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                coinsFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Coin>
                notifyDataSetChanged()
            }
        }


    }


}

