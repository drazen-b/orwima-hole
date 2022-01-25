package com.drazenbertic.hole_v2.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.drazenbertic.hole_v2.R
import com.drazenbertic.hole_v2.adapters.SearchCoinAdapter
import com.drazenbertic.hole_v2.dataclasses.Coin
import com.drazenbertic.hole_v2.interfaces.CoinGeckoEndPoints
import com.drazenbertic.hole_v2.objects.ServiceBuilder
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class   SearchFragment : Fragment() {

    lateinit var listCoins : ArrayList<Coin>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callApi()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        //It listens for changes and modifies the displayed list
        etSearchCoin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })



    }


//  With thus function we call API and set basic list of all 12k coins
    private fun callApi() {
        val request = ServiceBuilder.buildService(CoinGeckoEndPoints::class.java)
        val call = request.getAllCoins()


        call.enqueue(object : Callback<List<Coin>> {
            override fun onResponse(call: Call<List<Coin>>, response: Response<List<Coin>>) {
                if(response.isSuccessful) {
                    listCoins = (response.body() as ArrayList<Coin>?)!!
                    rvSearchCoinList.apply {
                        layoutManager = LinearLayoutManager(this.context)
                        adapter = SearchCoinAdapter(response.body()!!)
                    }
                }
            }
            override fun onFailure(call: Call<List<Coin>>, t: Throwable)
            {
                Log.d("FAIL", t.message.toString())
            }
        })
    }

//  With this we filter listCoins and display searched items
    fun filter(input: String){
        var coinsFiltered: List<Coin> = ArrayList()

        for (coin: Coin in listCoins){
            if(input in coin.id)
                coinsFiltered += coin

        }

        rvSearchCoinList.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = SearchCoinAdapter(coinsFiltered)
        }

    }

}
