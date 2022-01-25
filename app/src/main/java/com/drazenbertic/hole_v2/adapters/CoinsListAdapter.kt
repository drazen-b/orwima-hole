package com.drazenbertic.hole_v2.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drazenbertic.hole_v2.CoinInformationActivity
import com.drazenbertic.hole_v2.R
import com.drazenbertic.hole_v2.dataclasses.Coin
import kotlinx.android.synthetic.main.activity_coin_information.*
import kotlinx.android.synthetic.main.rvelement_coin_info.view.*

class CoinsListAdapter ( private var coins: List<Coin> ): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    var coinsFiltered: List<Coin> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CoinViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rvelement_coin_info, parent,
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
            Glide
                .with(itemView.context)
                .load(coins.image)
                .into(itemView.iwCoinPicture)

            itemView.tvCoinRank.text = coins.market_cap_rank.toString() + '.'
            itemView.tvCoinId.text = coins.symbol
            itemView.tvCoinName.text = coins.name
            itemView.tvCoinPrice.text = coins.current_price.toString() + "$"
            itemView.tvCoinPriceChange.text = coins.price_change_percentage_24h.toString() + '%'


            itemView.setOnClickListener {

                val intent = Intent(itemView.context, CoinInformationActivity::class.java)
                intent.putExtra("coinID", coins.id)
                itemView.context.startActivity(intent)
            }

            if(coins.price_change_percentage_24h > 0) {
                itemView.tvCoinPriceChange.setTextColor(itemView.resources.getColor(R.color.green))
                itemView.tvCoinPrice.setTextColor(itemView.resources.getColor(R.color.green))
            } else if (coins.price_change_percentage_24h < 0) {
                itemView.tvCoinPriceChange.setTextColor(itemView.resources.getColor(R.color.red_rose_madder))
                itemView.tvCoinPrice.setTextColor(itemView.resources.getColor(R.color.red_rose_madder))
            }


        }



    }



}