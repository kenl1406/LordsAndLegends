package com.lordsandlegends.crew.data.repository

import com.lordsandlegends.crew.data.model.Staff
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

/**
 * Reads from the `staff` table.
 *
 * Server only for now — the Room cache and the offline queue described in Section 1
 * of the build guide are Kendal's work and slot in behind this same class, so the
 * screens above it do not change when they arrive.
 */
class StaffRepository(private val client: SupabaseClient) {

    /**
     * The staff row belonging to a signed-in Supabase Auth user.
     * `auth_user_id` is the column that joins auth.users to our own staff table.
     */
    suspend fun byAuthUserId(authUserId: String): Staff =
        client.from("staff")
            .select {
                filter { eq("auth_user_id", authUserId) }
                limit(1)
            }
            .decodeSingle()
}
