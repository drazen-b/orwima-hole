package com.drazenbertic.hole_v2.interfaces

import com.drazenbertic.hole_v2.dataclasses.Coin
import com.drazenbertic.hole_v2.dataclasses.SimplePrice
import com.drazenbertic.hole_v2.dataclasses.globalData.globalData
import com.drazenbertic.hole_v2.dataclasses.singleCoin.SingleCoin
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoEndPoints {
    @GET("coins/markets")
    fun getCoins(@Query("vs_currency") currency: String) : Call<List<Coin>>

    @GET("coins/list")
    fun getAllCoins() : Call<List<Coin>>

    @GET("simple/price")
    fun searchSimplePrice(@Query("ids") name: String, @Query("vs_currencies") currency: String): Call<SimplePrice>

    @GET("coins/{id}")
    fun getCoinInformation(@Path("id") id: String): Call<SingleCoin>

    @GET("global")
    fun getGlobalInformation(): Call<globalData>

}
//https://api.coingecko.com/api/v3/coins/bitcoin
// ?localization=false
// &tickers=false
// &market_data=true
// &community_data=false
// &developer_data=false
// &sparkline=true