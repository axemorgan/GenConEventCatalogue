package com.axemorgan.genconcatalogue.components;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class AbstractPresenter<T> {

    private T view;

    public final void attachView(T view) {
        this.view = view;
        this.onViewAttached();
    }

    public final void detachView() {
        this.view = null;
        this.onViewDetached();
    }

    protected abstract void onViewAttached();

    protected abstract void onViewDetached();

    @NonNull
    protected T getViewOrThrow() {
        if (view != null) {
            return view;
        } else {
            throw new IllegalArgumentException("View should not have been null");
        }
    }

    @Nullable
    protected T getView() {
        return view;
    }
}
