package com.drazenbertic.hole_v2.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.drazenbertic.hole_v2.R
import com.drazenbertic.hole_v2.dataclasses.portfolio.CoinAndAmount
import com.drazenbertic.hole_v2.dataclasses.singleCoin.SingleCoin
import com.drazenbertic.hole_v2.interfaces.CoinGeckoEndPoints
import com.drazenbertic.hole_v2.objects.ServiceBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.dialog_add_coin_to_portfolio.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class AddCoinDialogFragment(coinName: String) : DialogFragment() {

    private val coinCollRef = Firebase.firestore.collection("coins")
    private val coinID = coinName
    lateinit var coinData : SingleCoin

    val request = ServiceBuilder.buildService(CoinGeckoEndPoints::class.java)
    val call = request.getCoinInformation(coinID)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.dialog_add_coin_to_portfolio, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btnDialogAddPriceToPortfolio.setOnClickListener{

            if (etDialogAddAmountToPortfolio.text.isNullOrEmpty()){

            } else {
                var amount : Float = etDialogAddAmountToPortfolio.text.toString().toFloat()


                call.enqueue(object : Callback<SingleCoin> {
                    override fun onResponse(call: Call<SingleCoin>, response: Response<SingleCoin>) {
                        if(response.isSuccessful) {

                            coinData = (response.body() as SingleCoin)

                            saveCoin(CoinAndAmount(coinID, amount, coinData))
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


    fun saveCoin(coin: CoinAndAmount) = CoroutineScope(Dispatchers.IO).launch {

        try {
            coinCollRef.add(coin).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
            }
            dismiss()

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
            dismiss()
        }
    }


}