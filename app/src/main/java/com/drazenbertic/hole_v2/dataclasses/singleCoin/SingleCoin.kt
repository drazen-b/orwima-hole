package com.drazenbertic.hole_v2.dataclasses.singleCoin

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable


data class SingleCoin(
    val id: String? = "",
    val symbol: String? = "",
    val name: String? = "",
    val blockchain_time_i_minutes: Int = -1,
    val hashing_algorithm: String? = "",
    val genesis_data: String? = "",
    val market_cap_rank: Int = -1,
    val last_updated: String? = "",
    val market_data: MarketData,
    val description: Description,
    val image: Image

        ) {
    constructor() : this("", "", "", -1, "",
        "", -1, "", MarketData(), Description(), Image())
}