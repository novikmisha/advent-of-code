data class Coordinate(
    val x: Int,
    val y: Int
) {
    operator fun plus(second: Coordinate) = Coordinate(x + second.x, y + second.y)

    operator fun minus(second: Coordinate) = Coordinate(x - second.x, y - second.y)
}
