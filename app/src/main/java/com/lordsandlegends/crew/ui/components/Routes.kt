package com.lordsandlegends.crew.ui.components

/**
 * Every destination in the app, as a navigation route.
 *
 * These replace the old `Screen` enum. An enum cannot carry arguments, which is
 * why signing a contract used to need a `selectedContract` variable kept in step
 * with the screen by hand — now the contract's id travels in the route itself.
 */
object Routes {
    const val POLICIES = "policies"
    const val ONBOARDING_DETAILS = "onboarding_details"
    const val OVERVIEW = "overview"
    const val ACADEMY = "academy"
    const val PERFORMANCE = "performance"
    const val PROFILE = "profile"
    const val PASSED = "passed"
    const val TIME_ATTENDANCE = "time_attendance"
    const val SELF_SERVICE = "self_service"

    // manager and owner only — these are left out of an employee's graph entirely
    const val CONTRACTS = "contracts"
    const val WARNINGS = "warnings"
    const val LEAVE = "leave"
    const val ONBOARDING_OFFBOARDING = "onboarding_offboarding"

    const val SIGN_CONTRACT = "sign_contract/{contractId}"
    fun signContract(contractId: String) = "sign_contract/$contractId"

    /** The four destinations the bottom tab bar switches between. */
    val tabs = listOf(OVERVIEW, ACADEMY, PERFORMANCE, PROFILE)

    /** Routes that fill the screen on their own, with no tab bar underneath. */
    val fullScreen = listOf(POLICIES, ONBOARDING_DETAILS, SIGN_CONTRACT)
}
