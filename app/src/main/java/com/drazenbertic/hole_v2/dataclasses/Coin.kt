package com.drazenbertic.hole_v2.dataclasses

data class Coin(

    var id: String,
    var symbol: String,
    var name: String,
    var image: String,
    var current_price: Float,
    var market_cap: Long,
    var market_cap_rank: Int,
    var price_change_percentage_24h: Float
)