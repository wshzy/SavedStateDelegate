package ziyao.huang.savedstatedelegate

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.SerialName
import ziyao.huang.savedstate.Savable
import ziyao.huang.savedstate.SavableScope
import ziyao.huang.savedstate.gson.GsonSavableScope
import ziyao.huang.savedstate.gson.byGson
import ziyao.huang.savedstate.list2set
import ziyao.huang.savedstate.listOf
import ziyao.huang.savedstate.moshi.MoshiSavableScope
import ziyao.huang.savedstate.moshi.byMoshi
import ziyao.huang.savedstate.nonNull
import ziyao.huang.savedstate.of
import ziyao.huang.savedstate.serialization.SerializationSavableScope
import ziyao.huang.savedstate.serialization.bySerialization
import java.io.Serializable

class MainViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    SavableScope by SavableScope(savedStateHandle),
    GsonSavableScope by GsonSavableScope(),
    MoshiSavableScope by MoshiSavableScope(),
    SerializationSavableScope by SerializationSavableScope() {
    var name: String by Savable.ForString.of(defaultValue = "name").bind()
        private set

    var age: Int by Savable.ForInt.of(defaultValue = 0).bind()
        private set

    var contents: List<String> by Savable.ForString.listOf()
        .convert(converter = nonNull(defaultValue = listOf("content 1", "content 2")))
        .bind()
        private set

    var ids: Set<Int> by Savable.ForInt.listOf(listOf(1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024))
        .convert(converter = list2set())
        .bind()
        private set

    private val _testSerializable by Savable.serialization<TestSerializable>()
        .of(defaultValue = TestSerializable("testSerializable"))
        .bindFlow()

    val testSerializable = _testSerializable.asStateFlow()

    private val _testParcelable by Savable.parcelable<TestParcelable>()
        .of(defaultValue = TestParcelable("testParcelable"))
        .bindFlow()

    val testParcelable = _testParcelable.asStateFlow()

    var testKSerialization by Savable.ForString.of()
        .convert(converter = bySerialization<TestKSerialization>())
        .convert(converter = nonNull(defaultValue = TestKSerialization(name = "testKSerialization")))
        .bind()
        private set

    var testMoshi by Savable.ForString.of()
        .convert(converter = byMoshi<TestMoshi>())
        .convert(converter = nonNull(defaultValue = TestMoshi(name = "testMoshi")))
        .bind()
        private set

    var testGson by Savable.ForString.of()
        .convert(converter = byGson<TestGson>())
        .convert(converter = nonNull(defaultValue = TestGson(name = "testGson")))
        .bind()
        private set
}

data class TestSerializable(val name: String) : Serializable

data class TestParcelable(val name: String) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString() ?: "")

    override fun describeContents(): Int = 0

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<TestParcelable> {
            override fun createFromParcel(p0: Parcel): TestParcelable = TestParcelable(p0)
            override fun newArray(p0: Int): Array<out TestParcelable?>? {
                return arrayOfNulls(p0)
            }
        }
    }
}

@kotlinx.serialization.Serializable
data class TestKSerialization(@SerialName("name") val name: String)

@JsonClass(generateAdapter = true)
data class TestMoshi(@Json(name = "name") val name: String)

data class TestGson(@SerializedName("name") val name: String)
