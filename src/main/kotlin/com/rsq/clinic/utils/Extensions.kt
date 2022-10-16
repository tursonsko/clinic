package com.rsq.clinic.utils

fun String.containsNumber(): Boolean = this.any { it.isDigit() }

/**
 * Function checking if user provides characters
 * which don't participate in personal data creation
 */
fun String.containsForbiddenCharactersForData(): Boolean {
    val regex = ".*[~`!@#$%^&*()_=+\\[\\]{};:\"|,./<>?\\\\1234567890].*".toRegex()
    return this.matches(regex)
}

/**
 * Function checking if user provides characters
 * which don't participate in address' creation
 */
fun String.containsForbiddenCharactersForAddress(): Boolean {
    val regex = ".*[~`!@#$%^&*_=+\\[\\]{};:|<>?].*".toRegex()
    return this.matches(regex)
}

/**
 * Function to check during creation, if user dont provide blank data
 */
fun String.containsNoCharacters(): Boolean = this.trim().isBlank()
