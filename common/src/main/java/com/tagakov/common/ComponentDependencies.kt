@file:Suppress("unused")

package com.tagakov.common

import android.support.v4.app.Fragment
import dagger.MapKey
import dagger.Module
import dagger.multibindings.Multibinds
import kotlin.reflect.KClass

interface ComponentDependencies

inline fun <reified T : ComponentDependencies> Fragment.findComponentDependencies(): T {
    return findComponentDependenciesProvider()[T::class.java] as T
}

typealias ComponentDependenciesProvider = Map<Class<out ComponentDependencies>, @JvmSuppressWildcards ComponentDependencies>

interface HasComponentDependencies {
    val dependencies: ComponentDependenciesProvider
}

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class ComponentDependenciesKey(val value: KClass<out ComponentDependencies>)

@Module
abstract class DummyComponentDependenciesModule private constructor() {
    @Multibinds
    abstract fun componentDependencies(): ComponentDependenciesProvider
}

fun Fragment.findComponentDependenciesProvider(): ComponentDependenciesProvider {
    var current: Fragment? = parentFragment
    while (current !is HasComponentDependencies?) {
        current = current?.parentFragment
    }

    val hasDaggerProviders = current ?: when {
        activity is HasComponentDependencies -> activity as HasComponentDependencies
        activity?.application is HasComponentDependencies -> activity?.application as HasComponentDependencies
        else -> throw IllegalStateException("Can not find suitable dagger provider for $this")
    }
    return hasDaggerProviders.dependencies
}