@file:JvmMultifileClass
@file:JvmName("SparseArraysKt")
@file:Suppress("NOTHING_TO_INLINE", "UNUSED", "EXTENSION_SHADOWED_BY_MEMBER")

package kota

import android.util.SparseIntArray
import java.util.*

/** Transform current map to sparse array. */
inline fun Map<Int, Int>.toSparseIntArray(): SparseIntArray = SparseIntArray().apply {
    for (key in keys) append(key, get(key))
}

/** Returns an empty sparse array. */
inline fun sparseIntArrayOf(): SparseIntArray = SparseIntArray()

/** Returns a sparse array with matching position of array input. */
inline fun sparseIntArrayOf(vararg elements: Int): SparseIntArray = SparseIntArray().apply {
    var i = 0
    for (element in elements) append(i++, element)
}

/** Returns a sparse array with defined position and element from Kotlin pair. */
inline fun sparseIntArrayOf(vararg pairs: Pair<Int, Int>): SparseIntArray = SparseIntArray().apply {
    for ((key, value) in pairs) append(key, value)
}

inline operator fun SparseIntArray.get(key: Int): Int = get(key)
inline operator fun SparseIntArray.set(key: Int, value: Int) = put(key, value)

inline fun SparseIntArray.containsKey(key: Int): Boolean = indexOfKey(key) > -1
inline fun SparseIntArray.containsValue(value: Int): Boolean = indexOfValue(value) > -1

inline fun SparseIntArray.forEach(action: (Int) -> Unit) {
    val size = size()
    for (i in 0 until size) {
        if (size != size()) throw ConcurrentModificationException()
        action(valueAt(i))
    }
}

inline fun SparseIntArray.forEachIndexed(action: (Int, Int) -> Unit) {
    val size = size()
    for (i in 0 until size) {
        if (size != size()) throw ConcurrentModificationException()
        action(keyAt(i), valueAt(i))
    }
}