package de.hannesstruss.unearthed

internal fun <T> List<T>.toArrayList(): ArrayList<T> = when (this) {
  is ArrayList<T> -> this
  else -> ArrayList(this)
}
