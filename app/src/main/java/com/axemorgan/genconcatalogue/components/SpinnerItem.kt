package com.axemorgan.genconcatalogue.components

data class SpinnerItem<T>(val text: String, val value: T) {
    override fun toString(): String {
        return text
    }
}