@file:JvmName("RxUtils")

package com.axemorgan.genconcatalogue.components

import io.reactivex.disposables.Disposable

/**
 * A null-safe way to dispose of subscriptions that may not have been initialized
 */


fun disposeOf(vararg disposables: Disposable) {
    disposables.forEach { it.disposeOf() }
}

fun Disposable?.disposeOf() {
    this?.dispose()
}
