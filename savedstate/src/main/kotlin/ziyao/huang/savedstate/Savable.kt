package ziyao.huang.savedstate

import android.os.Parcelable
import java.io.Serializable

sealed interface Savable<A> {
    data object ForString : Savable<String>
    data object ForInt : Savable<Int>
    data object ForLong : Savable<Long>
    data object ForFloat : Savable<Float>
    data object ForDouble : Savable<Double>
    data object ForBoolean : Savable<Boolean>
    data object ForChar : Savable<Char>
    data object ForCharSequence : Savable<CharSequence>
    class ForParcelable<T : Parcelable> : Savable<T>
    class ForSerializable<T : Serializable> : Savable<T>

    companion object {
        fun <T : Serializable> serialization() = ForSerializable<T>()

        fun <T : Parcelable> parcelable() = ForParcelable<T>()
    }
}
