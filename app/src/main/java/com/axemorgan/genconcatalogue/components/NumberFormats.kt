package com.axemorgan.genconcatalogue.components

import java.text.NumberFormat

object NumberFormats {

    private val numberFormat = lazy { NumberFormat.getInstance() }

    fun format(number: Int): String {
        return numberFormat.value.format(number)
    }
}