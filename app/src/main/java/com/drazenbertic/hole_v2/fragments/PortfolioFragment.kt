package com.drazenbertic.hole_v2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.drazenbertic.hole_v2.R
import com.drazenbertic.hole_v2.adapters.PortfolioCoinsList
import com.drazenbertic.hole_v2.dataclasses.TotalBalance
import com.drazenbertic.hole_v2.dataclasses.portfolio.CoinAndAmount
import com.drazenbertic.hole_v2.dataclasses.singleCoin.SingleCoin
import com.drazenbertic.hole_v2.interfaces.CoinGeckoEndPoints
import com.drazenbertic.hole_v2.objects.ServiceBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_portfolio.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.collections.ArrayList

class PortfolioFragment : Fragment() {

    private val coinCollRef = Firebase.firestore.collection("coins")
    private val balCollRef = Firebase.firestore.collection("balance")
    private var totalAmount : List<CoinAndAmount> = ArrayList<CoinAndAmount>()
    private var totalBalance = 0F

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_portfolio, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        subscribeToRealtimeUpdates()


    }

    private fun subscribeToRealtimeUpdates() {
        coinCollRef.addSnapshotListener {value, error ->
            if(error == null) {
                val values: List<CoinAndAmount> = value!!.toObjects<CoinAndAmount>()



                rvPortfolioCoinsList.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = PortfolioCoinsList(values, coinCollRef)
                }

                checkBalanceChange()

                for(coin in values){

                    val request = ServiceBuilder.buildService(CoinGeckoEndPoints::class.java)
                    val call = request.getCoinInformation(coin.coinID)

                    call.enqueue(object : Callback<SingleCoin> {
                        override fun onResponse(call: Call<SingleCoin>, response: Response<SingleCoin>) {
                            if(response.isSuccessful) {

                                val price = response.body()!!.market_data.current_price.usd

                                totalBalance += price * coin.amount
                                twBalanceValue.text = totalBalance.toString() + "$"

                            }
                        }
                        override fun onFailure(call: Call<SingleCoin>, t: Throwable)
                        {
                            Log.d("FAIL", t.message.toString())
                        }
                    })

                }



            } else {
                Log.e("FIRESTORE ERROR", error.message.toString())
            }
        }


    }


    private fun saveBalance(price: Float) = CoroutineScope(Dispatchers.IO).launch {
        var balance = TotalBalance(price)

        try {
            val balQuery = balCollRef.get().await()
            for(document in balQuery) {
                balCollRef.document(document.id).delete().await()
            }

            balCollRef.add(balance).await()

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun callTotalBalance(id: String, amount: Float) {


        val request = ServiceBuilder.buildService(CoinGeckoEndPoints::class.java)
        val call = request.getCoinInformation(id)

        call.enqueue(object : Callback<SingleCoin> {
            override fun onResponse(call: Call<SingleCoin>, response: Response<SingleCoin>) {
                if(response.isSuccessful) {

                    val price = response.body()!!.market_data.current_price.usd

                    saveBalance(price* amount)

                }
            }
            override fun onFailure(call: Call<SingleCoin>, t: Throwable)
            {
                Log.d("FAIL", t.message.toString())
            }
        })
    }

    private fun checkBalanceChange() {
        coinCollRef.addSnapshotListener {value, error ->
            if(error == null) {
                totalBalance = 0F
                val values: List<CoinAndAmount> = value!!.toObjects<CoinAndAmount>()
                if (values.isEmpty()){
                    twBalanceValue.text = totalBalance.toString()
                }

            } else {
                Log.e("FIRESTORE ERROR", error.message.toString())
            }
        }
    }

}