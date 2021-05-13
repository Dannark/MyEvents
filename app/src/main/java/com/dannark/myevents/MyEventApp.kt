package com.dannark.myevents

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.dannark.myevents.repository.event.EventRepository
import timber.log.Timber

class MyEventApp: MultiDexApplication() {

    val eventRepository: EventRepository
        get() = ServiceLocator.proviteEventRepository(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}