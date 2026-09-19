package com.lordsandlegends.crew.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `staff` table, named exactly as the ERD draws it.
 *
 * Column names are taken from the live Supabase schema, which lowercases a few
 * names the ERD capitalised (venue_id, not Venue_id). Only the columns sign-in
 * needs are required; the rest default to null.
 */
@Serializable
data class Staff(
    @SerialName("staff_id")       val staffId: Long = 0,
    @SerialName("auth_user_id")   val authUserId: String? = null,
    @SerialName("role")           val role: StaffRole,
    @SerialName("first_name")     val firstName: String,
    @SerialName("last_name")      val lastName: String,
    @SerialName("email")          val email: String,
    @SerialName("venue_id")       val venueId: Long? = null,
    @SerialName("phone_num")      val phoneNum: Long? = null,
    @SerialName("tax_num")        val taxNum: Long? = null,
    @SerialName("id_num")         val idNum: Long? = null,
    @SerialName("pilot_ref")      val pilotRef: String? = null,
    @SerialName("tutorial_state") val tutorialState: TutorialState? = null,
    @SerialName("is_active")      val isActive: Boolean = true,
    @SerialName("created_at")     val createdAt: String? = null,
) {
    val fullName: String get() = "$firstName $lastName"
}

@Serializable
enum class StaffRole {
    @SerialName("employee") EMPLOYEE,
    @SerialName("manager")  MANAGER,
    @SerialName("owner")    OWNER,
}

/** Confirm these values against the `tutorial_state` enum in Supabase before relying on them. */
@Serializable
enum class TutorialState {
    @SerialName("not_started") NOT_STARTED,
    @SerialName("in_progress") IN_PROGRESS,
    @SerialName("completed")   COMPLETED,
}
