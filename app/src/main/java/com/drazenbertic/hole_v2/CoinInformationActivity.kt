package com.drazenbertic.hole_v2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.drazenbertic.hole_v2.dataclasses.singleCoin.SingleCoin
import com.drazenbertic.hole_v2.dialogs.AddCoinDialogFragment
import com.drazenbertic.hole_v2.interfaces.CoinGeckoEndPoints
import com.drazenbertic.hole_v2.objects.ServiceBuilder
import kotlinx.android.synthetic.main.activity_coin_information.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CoinInformationActivity : AppCompatActivity() {


    lateinit var data: SingleCoin
    lateinit var coinID: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_information)

        coinID = intent.getStringExtra("coinID").toString()
        callCoinApi(coinID)

        btnAddCoinBack.setOnClickListener{
            finish()
        }

        btnAddCoinToPortfolio.setOnClickListener{
            val dialog = AddCoinDialogFragment(coinID)
            val fragmentManager = supportFragmentManager
            dialog.show(fragmentManager, "addCoinDialog")
        }
    }




//      With thus function we call API for specific coin information and set basic list of all 12k coins
    private fun callCoinApi(id: String) {


            val request = ServiceBuilder.buildService(CoinGeckoEndPoints::class.java)
            val call = request.getCoinInformation(id)

            call.enqueue(object : Callback<SingleCoin> {
                override fun onResponse(call: Call<SingleCoin>, response: Response<SingleCoin>) {
                    if(response.isSuccessful) {

                        data = (response.body() as SingleCoin)

                        setupData(data)

                    }
                }
                override fun onFailure(call: Call<SingleCoin>, t: Throwable)
                {
                    Log.d("FAILLLL", t.message.toString())
                }
            })
        }

    private fun setupData(coin: SingleCoin){

        Glide
            .with(this)
            .load(coin.image.small)
            .into(iwCoinInfoPicture)


        twCoinInfo.text = coin.id
        twCoinInfoValue.text = "$" + coin.market_data.current_price.usd.toString()
        tvCointInfoMarketValueAmount.text = coin.market_data.current_price.usd.toString()
        tvMarketCapRankValue.text = coin.market_cap_rank.toString()
        tvMarketCapValue.text ="$" + coin.market_data.market_cap.usd.toString()
        tvTotalSupplyValue.text = coin.market_data.total_supply.toString()
        tvTradingVolumeValue.text = "$" + coin.market_data.total_volume.usd.toString()
        tvCoinInformationDescription.text = coin.description.en
        tvCointInfoMarkeGainAmount.text = coin.market_data.price_change_percentage_24h.toString() + "%"

        if (coin.market_data.price_change_percentage_24h < 0) {
            tvCointInfoMarkeGainAmount.setTextColor(resources.getColor(R.color.red_rose_madder))
            twCoinInfoValue.setTextColor(resources.getColor(R.color.red_rose_madder))
            tvCointInfoMarketValueAmount.setTextColor(resources.getColor(R.color.red_rose_madder))
        } else if (coin.market_data.price_change_percentage_24h > 0) {
            tvCointInfoMarkeGainAmount.setTextColor(resources.getColor(R.color.green))
            twCoinInfoValue.setTextColor(resources.getColor(R.color.green))
            tvCointInfoMarketValueAmount.setTextColor(resources.getColor(R.color.green))
        }
    }

}