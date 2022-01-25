package com.drazenbertic.hole_v2.dataclasses.portfolio

import com.drazenbertic.hole_v2.dataclasses.singleCoin.SingleCoin

data class CoinAndAmount(
    var coinID: String = "",
//    val coinPrice: Float = -1F,
    var amount: Float = -1F,
    var coinData: SingleCoin
) {
    constructor() : this("", -1F, SingleCoin())
}

