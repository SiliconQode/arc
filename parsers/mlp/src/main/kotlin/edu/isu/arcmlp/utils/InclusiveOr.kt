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

import edu.isu.arcmlp.utils.InclusiveOr.*

sealed class InclusiveOr<out L, out R> {
    companion object {
        fun <L, R> both(left: L, right: R) = Shared(left, right)
        fun <T> both(both: T) = Shared(both, both)
        fun <L> left(left: L) = Left(left)
        fun <R> right(right: R) = Right(right)
        fun fromBools(left: Boolean, right: Boolean) = when {
            left && right -> Shared(Unit, Unit)
            left -> Left(Unit)
            right -> Right(Unit)
            else -> error("Must be left or right or both. Neither is not an option")
        }
    }
    
    data class Left<L>(override val left: L) : InclusiveOr<L, Nothing>() {
        override fun removeLeft() = error("Inclusive or must have at least one side")
        override fun removeRight() = this
        
        override val hasLeft: Boolean
            get() = true
        override val right: Nothing?
            get() = null
        override val hasRight: Boolean
            get() = false
    
        @Suppress("OVERRIDE_BY_INLINE") // Allow these simple calls to be inlined if the exact type is known
        override inline fun <T> mapLeft(mapper: (L) -> T) = Left(mapper(left))
    
        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T> mapRight(mapper: (Nothing) -> T) = this
    }
    
    data class Shared<L, R>(override val left: L, override val right: R) : InclusiveOr<L, R>() {
        override fun removeLeft() = Right(right)
        override fun removeRight() = Left(left)
        
        override val hasLeft: Boolean
            get() = true
        override val hasRight: Boolean
            get() = true
    
        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T> mapLeft(mapper: (L) -> T) = Shared(mapper(left), right)
    
        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T> mapRight(mapper: (R) -> T) = Shared(left, mapper(right))
    }
    
    data class Right<R>(override val right: R) : InclusiveOr<Nothing, R>() {
        override fun removeLeft() = this
        
        override fun removeRight() = error("Inclusive or must have at least one side")
        
        override val left: Nothing?
            get() = null
        override val hasLeft: Boolean
            get() = false
        override val hasRight: Boolean
            get() = true
    
        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T> mapLeft(mapper: (Nothing) -> T) = this
    
        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T> mapRight(mapper: (R) -> T) = Right(mapper(right))
    }
    
    abstract fun <T> mapLeft(mapper: (L) -> T): InclusiveOr<T, R>
    abstract fun <T> mapRight(mapper: (R) -> T): InclusiveOr<L, T>
    
    abstract val left: L?
    abstract val hasLeft: Boolean
    abstract val right: R?
    abstract val hasRight: Boolean
    abstract fun removeLeft(): InclusiveOr<Nothing, R>
    abstract fun removeRight(): InclusiveOr<L, Nothing>
}

val <L, R> InclusiveOr<L, R>.hasBoth: Boolean
    get() = hasLeft && hasRight

fun <L, R> InclusiveOr<L, R>.forLeft(func: (L) -> Unit) = mapLeft(func)
fun <L, R> InclusiveOr<L, R>.forRight(func: (R) -> Unit) = mapRight(func)

fun <L, R> InclusiveOr<L, R>.defaultLeft(left: L): InclusiveOr<L, R> = when (this) {
    is Right -> Shared(left, right)
    else -> this
}

fun <L, R> InclusiveOr<L, R>.defaultRight(right: R): InclusiveOr<L, R> = when (this) {
    is Left -> Shared(left, right)
    else -> this
}

fun <L, R> InclusiveOr<L, R>.defaults(left: L, right: R): Shared<L, R> = when (this) {
    is Left -> Shared(this.left, right)
    is Right -> Shared(left, this.right)
    is Shared -> this
}

fun <L, R> Shared<L, R>.toPair(): Pair<L, R> = left to right

/**
 * Gets the first item in this inclusive or. The left item if it exists, else the right item.
 */
val <T> InclusiveOr<T, T>.first: T
    get() = if (hasLeft) left!! else right!!

/**
 * Concatenates the left and right sides of this object.
 */
fun <T> InclusiveOr<Iterable<T>, Iterable<T>>.merge(): Iterable<T> = when (this) {
    is Left -> left
    is Right -> right
    is Shared -> left + right
}

/**
 * Pairs the left side of [this] with [left] if it exists.
 * Pairs the right side of [this] with [right] if it exists.
 */
fun <L1, R1, L2, R2> InclusiveOr<L1, R1>.zip(
    left: L2, right: R2
): InclusiveOr<Pair<L1, L2>, Pair<R1, R2>> = when (this) {
    is Left -> Left(this.left to left)
    is Right -> Right(this.right to right)
    is Shared -> Shared(this.left to left, this.right to right)
}

/**
 * Combines the two inclusive ors so that only sides that are in both are shared.
 */
fun <L1, R1, L2, R2> InclusiveOr<L1, R1>.zip(
    other: InclusiveOr<L2, R2>
): InclusiveOr<Pair<L1, L2>, Pair<R1, R2>> = when (this) {
    is Left -> when (other) {
        is Left -> Left(left to other.left)
        is Shared -> Left(left to other.left)
        is Right -> error("No sides are shared by both sides")
    }
    is Shared -> when (other) {
        is Left -> Left(left to other.left)
        is Shared -> Shared(left to other.left, right to other.right)
        is Right -> Right(right to other.right)
    }
    is Right -> when (other) {
        is Left -> error("Nothing is shared by both sides")
        is Shared -> Right(right to other.right)
        is Right -> Right(right to other.right)
    }
}

/**
 * Uses [mapper] to transform both the left and right side of this if they exist.
 */
fun <T, V> InclusiveOr<T, T>.mapBoth(mapper: (T) -> V) = mapLeft(mapper).mapRight(mapper)

fun <T, V> InclusiveOr<Iterable<T>, Iterable<T>>.mapDeep(mapper: (T) -> V) = mapBoth { it.map(mapper) }

fun <L, R, T> InclusiveOr<Iterable<L>, R>.mapLeftDeep(mapper: (L) -> T) = mapLeft { it.map(mapper) }
fun <L, R, T> InclusiveOr<L, Iterable<R>>.mapRightDeep(mapper: (R) -> T) = mapRight { it.map(mapper) }

private class RemoveLeftException : Exception("Remove the left branch")
private class RemoveRightException : Exception("Remove the right branch")

sealed class Splitter {
    /**
     * Returns the left item if code is split left ways. Returns the right item
     * if code is split right ways.
     */
    abstract fun <T> split(left: T, right: T): T
    
    /**
     * Removes the current branch of execution from the inclusive or object.
     * Note that at least one branch must still be there
     */
    abstract fun prune(): Nothing
    
    object Left : Splitter() {
        override fun <T> InclusiveOr<T, T>.filterSplit(): T = if (!hasLeft) prune() else left!!
        override fun prune() = throw RemoveLeftException()
        override fun <T> split(left: T, right: T) = left
    }
    
    object Right : Splitter() {
        override fun <T> InclusiveOr<T, T>.filterSplit(): T = if (!hasRight) prune() else right!!
        override fun prune() = throw RemoveRightException()
        override fun <T> split(left: T, right: T) = right
    }
    
    /**
     * Returns the left item if code is split left ways. Returns the right item
     * if code is split right ways. Returns null if the item doesn't exist.
     */
    fun <T> InclusiveOr<T, T>.split(): T? = split(left, right)
    
    /**
     * Returns the left item if code is split left ways. Returns the right item
     * if code is split right ways. If the desired item doesn't exist, the current branch
     * of execution is trimmed.
     */
    abstract fun <T> InclusiveOr<T, T>.filterSplit(): T
}

/**
 * Executes [mapper] for left if it exists and for the right branch if it exists. Passes a [Splitter]
 * context that allows you to use objects depending on if they are in the left or right branches.
 */
inline fun <T, U> InclusiveOr<T, T>.split(crossinline mapper: Splitter.(T) -> U): InclusiveOr<U, U> {
    val leftMapped = try {
        mapLeft { Splitter.Left.mapper(it) }
    } catch (e: RemoveLeftException) {
        removeLeft()
    }
    
    return try {
        leftMapped.mapRight { Splitter.Right.mapper(it) }
    } catch (e: RemoveRightException) {
        leftMapped.removeRight()
    }
}