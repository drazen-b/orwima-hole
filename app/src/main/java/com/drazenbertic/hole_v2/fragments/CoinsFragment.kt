package com.drazenbertic.hole_v2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.drazenbertic.hole_v2.R
import com.drazenbertic.hole_v2.adapters.CoinsListAdapter
import com.drazenbertic.hole_v2.dataclasses.Coin
import com.drazenbertic.hole_v2.dataclasses.globalData.globalData
import com.drazenbertic.hole_v2.interfaces.CoinGeckoEndPoints
import com.drazenbertic.hole_v2.objects.ServiceBuilder
import kotlinx.android.synthetic.main.fragment_coins.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CoinsFragment : Fragment() {

    lateinit var globalData: globalData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val View = inflater.inflate(R.layout.fragment_coins, container, false)

        return View
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callApi()
    }



    private fun callApi(){
        val listRequest = ServiceBuilder.buildService(CoinGeckoEndPoints::class.java)
        val listCall = listRequest.getCoins("usd")


        listCall.enqueue(object : Callback<List<Coin>> {
            override fun onResponse(call: Call<List<Coin>>, response: Response<List<Coin>>) {
                if(response.isSuccessful) {
                    rvMainCoinsList.apply {
                        layoutManager = LinearLayoutManager(this.context)
                        adapter = CoinsListAdapter(response.body()!!)
                    }
                }
            }
            override fun onFailure(call: Call<List<Coin>>, t: Throwable)
            {
                Log.d("FAIL", t.message.toString())
            }
        })

        val globalRequest = ServiceBuilder.buildService(CoinGeckoEndPoints::class.java)
        val globalCall = listRequest.getGlobalInformation()


        globalCall.enqueue(object : Callback<globalData> {
            override fun onResponse(call: Call<globalData>, response: Response<globalData>) {
                if(response.isSuccessful) {
                    globalData = response.body()!!


                    twGlobalMarketCapMainValue.text= "$" + globalData.data.total_market_cap.usd.toString()
                    twTotalVolumeMainValue.text ="$" + globalData.data.total_volume.usd.toString()


                }
            }
            override fun onFailure(call: Call<globalData>, t: Throwable)
            {
                Log.d("FAILLLL", t.message.toString())
            }
        })




    }
}