package com.lordsandlegends.crew

import android.app.Application
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader

class CrewApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PDFBoxResourceLoader.init(applicationContext)
    }
}