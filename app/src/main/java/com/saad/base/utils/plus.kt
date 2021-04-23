package com.saad.base.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

operator fun CompositeDisposable?.plus(disposable: Disposable?): CompositeDisposable? {
    disposable?.let {
        this?.add(it)
    }
    return this
}