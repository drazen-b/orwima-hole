package com.drazenbertic.hole_v2.dataclasses.singleCoin

import com.drazenbertic.hole_v2.dataclasses.globalData.TotalVolume

data class MarketData(
    val current_price: CurrentPrice,
    val market_cap: MarketCap,
    val total_volume: TotalVolume,
    val high24_h: High24H,
    val price_change_percentage_24h: Float = -1F,
    val total_supply: Float = -1F

) {
    constructor() : this(CurrentPrice(), MarketCap(), TotalVolume(), High24H(), -1F, -1F)
}