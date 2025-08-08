package ziyao.huang.savedstate

fun <A> Savable<A>.of(): SaveDelegate<A?> = SaveDelegate.of(this)

fun <A> Savable<A>.of(defaultValue: A): SaveDelegate<A> = of().convert(nonNull(defaultValue))

fun <A> Savable<A>.listOf(): SaveDelegate<List<A>?> = SaveDelegate.listOf(this)

fun <A> Savable<A>.listOf(defaultValue: List<A>): SaveDelegate<List<A>> =
    listOf().convert(nonNull(defaultValue))

fun <A, B> Converter(convert: (A) -> B, reverse: (B) -> A): Converter<A, B> =
    object : Converter<A, B> {
        override fun convert(value: A): B = convert(value)
        override fun reverse(value: B): A = reverse(value)
    }

fun <T> nonNull(defaultValue: T): Converter<T?, T> =
    Converter(convert = { it ?: defaultValue }, reverse = { it })

fun <A, B> onNonNull(converter: Converter<A, B>): Converter<A?, B?> = Converter(
    convert = { it?.let(converter::convert) },
    reverse = { it?.let(converter::reverse) },
)

fun <A, B> mapList(converter: Converter<A, B>): Converter<List<A>, List<B>> = Converter(
    convert = { it.map(converter::convert) },
    reverse = { it.map(converter::reverse) },
)

fun <T> list2set(): Converter<List<T>, Set<T>> = Converter(
    convert = { it.toSet() },
    reverse = { it.toList() },
)

fun <T> set2list(): Converter<Set<T>, List<T>> = Converter(
    convert = { it.toList() },
    reverse = { it.toSet() },
)

fun <A, B> mapSet(converter: Converter<A, B>): Converter<Set<A>, Set<B>> = Converter(
    convert = { it.map(converter::convert).toSet() },
    reverse = { it.map(converter::reverse).toSet() },
)
