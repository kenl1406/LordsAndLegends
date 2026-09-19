package com.lordsandlegends.crew

import android.app.Application
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

class CrewApp : Application() {

    /**
     * One client for the whole app. Built here because Application is created once
     * per process, so every screen shares the same auth session and connection pool.
     *
     * The Room database from Section 1 of the build guide is built alongside this.
     */
    lateinit var supabase: SupabaseClient
        private set

    override fun onCreate() {
        super.onCreate()
        PDFBoxResourceLoader.init(applicationContext)

        supabase = createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY,
        ) {
            install(Postgrest)
            install(Auth)
            install(Storage)
            install(Realtime)
        }
    }
}
