package com.raisac.medmobpatients

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for MedMobPatients
 * Enables Hilt dependency injection across the app
 */
@HiltAndroidApp
class MedMobPatientsApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize any app-wide components here
    }
}
