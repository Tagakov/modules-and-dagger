package com.tagakov.common

import android.app.Activity
import android.app.Dialog
import android.app.DialogFragment
import android.app.Fragment
import android.support.annotation.IdRes
import android.view.View
import android.view.ViewStub
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import android.support.v4.app.DialogFragment as SupportDialogFragment
import android.support.v4.app.Fragment as SupportFragment

fun <V: View> Activity.bindView(id: Int): ReadOnlyProperty<Activity, V> = required(id, viewFinder, null)
fun <V: View> Dialog.bindView(id: Int): ReadOnlyProperty<Dialog, V> = required(id, viewFinder, null)
fun <V: View> Activity.bindOptionalView(id: Int): ReadOnlyProperty<Activity, V?> = nullable(id, viewFinder, null)
fun <V: View> Dialog.bindOptionalView(id: Int): ReadOnlyProperty<Dialog, V?> = nullable(id, viewFinder, null)

fun <V: View> Activity.bindViews(ids: IntArray): ReadOnlyProperty<Activity, List<V>> = required(ids, viewFinder, null)
fun <V: View> Dialog.bindViews(ids: IntArray): ReadOnlyProperty<Dialog, List<V>> = required(ids, viewFinder, null)
fun <V: View> Activity.bindOptionalViews(ids: IntArray): ReadOnlyProperty<Activity, List<V>> = nullable(ids, viewFinder, null)
fun <V: View> Dialog.bindOptionalViews(ids: IntArray): ReadOnlyProperty<Dialog, List<V>> = nullable(ids, viewFinder, null)

fun <V: View> View.bindView(id: Int, viewInitializer: ViewInitializer<V>? = null) = requiredImmediate(id, viewFinder, viewInitializer)
fun <V: View> View.bindViews(ids: IntArray, viewInitializer: ViewInitializer<List<V>>? = null) = requiredImmediate(ids, viewFinder, viewInitializer)
fun <V: View> View.bindOptionalView(id: Int, viewInitializer: ViewInitializer<V>? = null) = nullableImmediate(id, viewFinder, viewInitializer)
fun <V: View> View.bindOptionalViews(ids: IntArray, viewInitializer: ViewInitializer<List<V>>? = null) = nullableImmediate(ids, viewFinder, viewInitializer)

fun Fragment.createBinder() = ViewBinder(viewFinder)
fun DialogFragment.createBinder() = ViewBinder(viewFinder)
fun SupportFragment.createBinder() = ViewBinder(viewFinder)
fun SupportDialogFragment.createBinder() = ViewBinder(viewFinder)

class ViewBinder(private val viewFinder: Finder) {
    private val lazyRegistry = mutableListOf<Lazy<*>>()

    operator fun <V : View> invoke(@IdRes id: Int, viewInitializer: ViewInitializer<V>? = null) =
            required(id, viewFinder, viewInitializer).register()
    operator fun <V : View> invoke(ids: IntArray, viewInitializer: ViewInitializer<List<V>>? = null) =
            required(ids, viewFinder, viewInitializer).register()

    fun <V : View> optional(@IdRes id: Int, viewInitializer: ViewInitializer<V?>? = null) =
            nullable(id, viewFinder, viewInitializer).register()
    fun <V : View> optional(ids: IntArray, viewInitializer: ViewInitializer<List<V?>>? = null) =
            nullable(ids, viewFinder, viewInitializer).register()

    fun <V : View> stub(@IdRes id: Int, viewInitializer: ViewInitializer<V>? = null): ReadOnlyProperty<Any, V> = stubRequired(id, viewFinder, viewInitializer)
    fun <V : View> optionalStub(@IdRes id: Int, viewInitializer: ViewInitializer<V?>? = null): ReadOnlyProperty<Any, V?> = stubNullable(id, viewFinder, viewInitializer)

    fun resetViews() = lazyRegistry.forEach { it.reset() }

    private fun <V> Lazy<V>.register(): ReadOnlyProperty<Any, V> = also {
        lazyRegistry += it
    }
}


private typealias Finder = (Int) -> View?
private typealias ViewInitializer<V> = V.() -> Unit

private inline val View.viewFinder: Finder get() = { findViewById(it) }
private inline val Activity.viewFinder: Finder get() = { findViewById(it) }
private inline val Dialog.viewFinder: Finder get() = { findViewById(it) }

private val DialogFragment.viewFinder: Finder
    get() = { if (dialog == null && view == null) tooEarlyViewAccess(this) else dialog?.findViewById(it) ?: view?.findViewById(it) }
private val SupportDialogFragment.viewFinder: Finder
    get() = { if (dialog == null && view == null) tooEarlyViewAccess(this) else dialog?.findViewById(it) ?: view?.findViewById(it) }
private val Fragment.viewFinder: Finder
    get() = { (view ?: tooEarlyViewAccess(this)).findViewById(it) }
private val SupportFragment.viewFinder: Finder
    get() = { (view ?: tooEarlyViewAccess(this)).findViewById(it) }

@Suppress("UNCHECKED_CAST")
private fun <V : View> required(id: Int, finder: Finder, viewInitializer: ViewInitializer<V>?) = Lazy { desc -> requiredImmediate(id, finder, viewInitializer, desc) }

@Suppress("UNCHECKED_CAST")
private fun <V : View> nullable(id: Int, finder: Finder, viewInitializer: ViewInitializer<V>?) = Lazy { nullableImmediate(id, finder, viewInitializer) }

@Suppress("UNCHECKED_CAST")
private fun <V : View> required(ids: IntArray, finder: Finder, viewInitializer: ViewInitializer<List<V>>?) = Lazy { desc -> requiredImmediate(ids, finder, viewInitializer, desc) }

@Suppress("UNCHECKED_CAST")
private fun <V : View> nullable(ids: IntArray, finder: Finder, viewInitializer: ViewInitializer<List<V>>?) = Lazy { nullableImmediate(ids, finder, viewInitializer) }

@Suppress("UNCHECKED_CAST")
private fun <V : View> stubRequired(id: Int, finder: Finder, viewInitializer: ViewInitializer<V>?) = Lazy { desc ->
    val stub = requiredImmediate<View>(id, finder, null, desc)
    if (stub !is ViewStub) incorrectTypeOfViewStub(stub, desc)
    (stub.inflate() as V).also { viewInitializer?.invoke(it) }
}

@Suppress("UNCHECKED_CAST")
private fun <V : View> stubNullable(id: Int, finder: Finder, viewInitializer: ViewInitializer<V>?) = Lazy { desc ->
    val stub = nullableImmediate<View>(id, finder, null)
    if (stub !is ViewStub?) incorrectTypeOfViewStub(stub!!, desc)
    (stub?.inflate() as V?)?.also { viewInitializer?.invoke(it) }
}

@Suppress("UNCHECKED_CAST")
private fun <V : View> requiredImmediate(id: Int, finder: Finder, viewInitializer: ViewInitializer<V>?, name: String? = null) = (finder(id) as V? ?: viewNotFound(id, name))
        .also { viewInitializer?.invoke(it) }

@Suppress("UNCHECKED_CAST")
private fun <V : View> requiredImmediate(ids: IntArray, finder: Finder, viewInitializer: ViewInitializer<List<V>>?, name: String? = null) = ids.map { finder(it) as V? ?: viewNotFound(it, name) }
        .also { viewInitializer?.invoke(it) }

@Suppress("UNCHECKED_CAST")
private fun <V : View> nullableImmediate(id: Int, finder: Finder, viewInitializer: ViewInitializer<V>?) = (finder(id) as V?)
        ?.also { viewInitializer?.invoke(it) }

@Suppress("UNCHECKED_CAST")
private fun <V : View> nullableImmediate(ids: IntArray, finder: Finder, viewInitializer: ViewInitializer<List<V>>?) = ids.map { finder(it) as V? }.filterNotNull()
        .also { viewInitializer?.invoke(it) }


@Suppress("UNCHECKED_CAST")
private class Lazy<out V>(val initializer: (String?) -> V) : ReadOnlyProperty<Any, V> {
    private object EMPTY_VALUE

    private var value: Any? = EMPTY_VALUE

    override fun getValue(thisRef: Any, property: KProperty<*>): V {
        init(property.name)
        return value as V
    }

    fun init(name: String? = null) {
        if (value == EMPTY_VALUE) {
            value = initializer(name)
        }
    }

    fun reset() {
        value = EMPTY_VALUE
    }
}

private fun viewNotFound(id: Int, name: String? = null): Nothing = throw IllegalStateException("View with ID $id '${name?.let { "for $it" } ?: ""}' not found.")
private fun tooEarlyViewAccess(ref: Any): Nothing = throw IllegalStateException("You accessing to views of ${ref.javaClass.name} to early")
private fun incorrectTypeOfViewStub(view: View, name: String? = null): Nothing = throw IllegalStateException("View stub with ID ${view.id} '${name?.let { "for $it" } ?: ""}' actually is ${view.javaClass.name}")
