package com.drazenbertic.hole_v2.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drazenbertic.hole_v2.R
import com.drazenbertic.hole_v2.dataclasses.SimplePrice
import com.drazenbertic.hole_v2.dataclasses.portfolio.CoinAndAmount
import com.drazenbertic.hole_v2.dataclasses.singleCoin.SingleCoin
import com.drazenbertic.hole_v2.dialogs.UpdateCoinDialogFragment
import com.drazenbertic.hole_v2.interfaces.CoinGeckoEndPoints
import com.drazenbertic.hole_v2.objects.ServiceBuilder
import com.google.firebase.firestore.CollectionReference
import kotlinx.android.synthetic.main.dialog_update_coin.*
import kotlinx.android.synthetic.main.fragment_portfolio.view.*
import kotlinx.android.synthetic.main.rvelement_coin_info.view.iwCoinPicture
import kotlinx.android.synthetic.main.rvelement_coin_info.view.tvCoinId
import kotlinx.android.synthetic.main.rvelement_coin_info.view.tvCoinName
import kotlinx.android.synthetic.main.rvelement_coin_info.view.tvCoinPrice
import kotlinx.android.synthetic.main.rvelement_portfoliocoin.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PortfolioCoinsList(private val coins: List<CoinAndAmount>, val fb: CollectionReference) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var totalBalance: Float = 0F

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var total = 0F
        for(coin in coins) {
            total += coin.coinData.market_data.current_price.usd
        }


        return CoinViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rvelement_portfoliocoin, parent,
                false
        ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        when(holder) {
            is CoinViewHolder -> {
                holder.bind(coins[position])


                holder.itemView.setOnClickListener(object  :View.OnClickListener{
                    override fun onClick(v: View?) {

                        val activity = v!!.context as AppCompatActivity
                        val dialog = UpdateCoinDialogFragment(fb, coins[position])
                        val fragmentManager = activity.supportFragmentManager
                        dialog.show(fragmentManager, "updateCoinDialog")
                        notifyDataSetChanged()
                    }
                })

            }
        }


    }

    override fun getItemCount(): Int {
        return coins.size
    }



    class CoinViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var coinPrice : Float = 0f

        fun bind(coin: CoinAndAmount) {

            val request = ServiceBuilder.buildService(CoinGeckoEndPoints::class.java)
            val call = request.getCoinInformation(coin.coinID)

            call.enqueue(object : Callback<SingleCoin> {
                override fun onResponse(call: Call<SingleCoin>, response: Response<SingleCoin>) {
                    if(response.isSuccessful) {

                        coinPrice = response.body()!!.market_data.current_price.usd.toFloat()

                        Glide
                        .with(itemView.context)
                        .load(coin.coinData.image.large)
                        .into(itemView.iwCoinPicture)

                        itemView.tvCoinId.text = coin.coinData.id
                        itemView.tvCoinName.text = coin.coinData.name
                        itemView.tvCoinPrice.text = (coin.amount * coinPrice).toString() + "$"
                        itemView.tvCoinAmount.text = coin.amount.toString()

                    }
                }
                override fun onFailure(call: Call<SingleCoin>, t: Throwable)
                {
                    Log.d("FAILLLL", t.message.toString())
                }
            })


        }

    }


}