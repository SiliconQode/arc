/*
 * MIT License
 *
 * Copyright (c) 2018-2019, Idaho State University, Empirical Software Engineering
 * Laboratory and Isaac Griffith
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package edu.isu.arcmlp.utils

import kotlin.reflect.KClass

fun <T, K> Iterable<Pair<T, K>>.reversePairs() = map { it.reversed() }
fun <A, B> Pair<A, B>.reversed() = second to first
fun <A> Pair<A, A>.toSet() = setOf(first, second)
fun <K, V> Iterable<Pair<K, V>>.toReversedMap() = associate { (k, v) -> v to k }
infix fun <A, B, C> Pair<A, B>.via(third: C) = Triple(first, second, third)

fun <T> Iterable<T>.split(splitSelector: (T) -> Boolean): List<List<T>> {
    val splitItems = mutableListOf<List<T>>()
    var currentList = mutableListOf<T>()

    for (item in this) {
        if (splitSelector(item)) {
            splitItems += currentList
            currentList = mutableListOf()
        } else {
            currentList.add(item)
        }
    }
    splitItems += currentList
    return splitItems
}

fun <T> Iterable<T>.interleave(other: Iterable<T>): List<T> {
    val items = mutableListOf<T>()

    val iter1 = this.iterator()
    val iter2 = other.iterator()

    while (iter1.hasNext() && iter2.hasNext()) {
        items.add(iter1.next())
        items.add(iter2.next())
    }
    items.addAll(iter1.asSequence())
    items.addAll(iter2.asSequence())

    return items
}

fun <K, V> Map<K, V>.partition(predicate: (K, V) -> Boolean): Pair<Map<K, V>, Map<K, V>> {
    val m1 = mutableMapOf<K, V>()
    val m2 = mutableMapOf<K, V>()
    for ((key, value) in this) {
        if (predicate(key, value)) {
            m1[key] = value
        } else {
            m2[key] = value
        }
    }

    return m1 to m2
}

fun <K, V> Map<K, V>.partitionKeys(predicate: (K) -> Boolean) = partition { k, _ -> predicate(k) }
fun <K, V> Map<K, V>.partitionValues(predicate: (V) -> Boolean) = partition { _, v -> predicate(v) }
inline fun <K, V, reified T : Any> Map<K, V>.filterValuesInstanceOf(@Suppress("UNUSED_PARAMETER") clazz: KClass<T>): Map<K, T> {
    val result = mutableMapOf<K, T>()
    for ((key, value) in entries) {
        if (value is T) {
            result[key] = value
        }
    }
    return result
}

fun <K, V> Iterable<Map.Entry<K, V>>.toMap() = map { it.toPair() }.toMap()

/**
 * Casts each element of the collection to the specified type. If its type is different, an error is thrown.
 */
inline fun <reified T> Iterable<*>.castElements(): Iterable<T> = Iterable {
    iterator {
        for (item in this@castElements) {
            yield(item as T)
        }
    }
}