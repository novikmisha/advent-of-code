
infix fun LongRange.rangeIntersect(other: LongRange): LongRange? {
    if ((other.last < first) || last < other.first) return null
    return LongRange(maxOf(first, other.first), minOf(last, other.last))
}
