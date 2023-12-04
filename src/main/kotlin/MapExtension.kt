fun <K> MutableMap<K, Int>.inc(key: K): Int = inc(key, 1)

fun <K> MutableMap<K, Int>.inc(key: K, value: Int): Int = merge(key, value, Math::addExact)!!
