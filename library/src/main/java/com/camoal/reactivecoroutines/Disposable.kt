package com.camoal.reactivecoroutines

/**
 * Represents a disposable resource.
 */
interface Disposable {

    /**
     * Dispose the resource, the operation is idempotent.
     */
    fun dispose()

    /**
     * Returns if this resource has been disposed.
     * @return true if this resource has been disposed.
     */
    fun isDisposed(): Boolean
}