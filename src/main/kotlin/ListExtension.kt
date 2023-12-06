inline fun <T> Collection<T>.indexOfFirstOrNull(predicate: (T) -> Boolean): Int? {
    for ((index, item) in this.withIndex()) {
        if (predicate(item))
            return index
    }
    return null
}

fun Collection<Int>.times() = reduce(Int::times)
fun Collection<Long>.times() = reduce(Long::times)

fun List<String>.atCoordinates(coordinate: Coordinate): Char? {
    if (!indices.contains(coordinate.x)
        || !(0 until first().length).contains(coordinate.y)) {
        return null
    }

    return get(coordinate.x)[coordinate.y]
}