package com.drazenbertic.hole_v2.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.drazenbertic.hole_v2.R
import com.drazenbertic.hole_v2.dataclasses.Coin
import com.drazenbertic.hole_v2.dataclasses.portfolio.CoinAndAmount
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.dialog_update_coin.*
import kotlinx.android.synthetic.main.fragment_portfolio.*
import kotlinx.android.synthetic.main.rvelement_portfoliocoin.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class UpdateCoinDialogFragment(val fb: CollectionReference, val coin: CoinAndAmount) : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.dialog_update_coin, container, false)

        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnDialogUpdatePrice.setOnClickListener{
            val newCoinMap = getNewCoinMap(coin)
            updateCoin(coin, newCoinMap)
            dismiss()
        }

        btnDialogDeleteCoin.setOnClickListener{
            val newCoinMap = getNewCoinMap(coin)
            deleteCoin(coin)
            dismiss()
        }


    }


    private fun getNewCoinMap( coin: CoinAndAmount) : Map<String, Any> {
        val coinID = coin.coinID
        val coinAmount = etDialogAddAmountToPortfolio.text.toString()
        val map = mutableMapOf<String, Any>()
        if(coinID.isNotEmpty()) {
            map["coinID"] = coinID
        }
        if(coinAmount.isNotEmpty()) {
            map["amount"] = coinAmount.toFloat()
        }
        return map
    }

    private fun updateCoin(coin: CoinAndAmount, newCoinMap: Map<String, Any>) = CoroutineScope(Dispatchers.IO).launch {
        val coinQuery = fb.whereEqualTo("coinID", coin.coinID)
                            .whereEqualTo("amount", coin.amount)
                            .get()
                            .await()

        if(coinQuery.documents.isNotEmpty()) {
            for(document in coinQuery) {
                try{
                    fb.document(document.id).update("amount", coin.amount)
                    fb.document(document.id).set(
                        newCoinMap, SetOptions.merge()
                    ).await()
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "No coins matched the query", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deleteCoin(coin: CoinAndAmount) = CoroutineScope(Dispatchers.IO).launch {
        val coinQuery = fb.whereEqualTo("coinID", coin.coinID)
            .whereEqualTo("amount", coin.amount)
            .get()
            .await()

        if(coinQuery.documents.isNotEmpty()) {
            for(document in coinQuery) {
                try{
                    fb.document(document.id).delete().await()
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "No coins matched the query", Toast.LENGTH_LONG).show()
            }
        }
    }


}