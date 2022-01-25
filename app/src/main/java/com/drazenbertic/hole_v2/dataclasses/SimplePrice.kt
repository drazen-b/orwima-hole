package com.drazenbertic.hole_v2.dataclasses

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonNames

data class SimplePrice(
    var solana: CoinIdPrice = CoinIdPrice()

)

data class CoinIdPrice(
    var usd: Double = (-1).toDouble()
)


//    var coinID: String = "",