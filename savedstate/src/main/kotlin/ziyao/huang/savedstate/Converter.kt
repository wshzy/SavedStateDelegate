package ziyao.huang.savedstate

interface Converter<T, R> {
    fun convert(value: T): R
    fun reverse(value: R): T
}
