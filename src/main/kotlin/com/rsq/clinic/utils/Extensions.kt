package com.rsq.clinic.utils

//fun String.isNumeric(): Boolean {
//    val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
//    return this.matches(regex)
//}

fun String.containsNumber(): Boolean = this.any { it.isDigit() }

fun String.containsForbiddenCharactersForData(): Boolean {
    val regex = ".*[~`!@#$%^&*()_=+\\[\\]{};:\"|,./<>?\\\\1234567890].*".toRegex()
    return this.matches(regex)
}

fun String.containsForbiddenCharactersForAddress(): Boolean {
    val regex = ".*[~`!@#$%^&*_=+\\[\\]{};:|<>?].*".toRegex()
    return this.matches(regex)
}

fun String.containsNoCharacters(): Boolean = this.trim().isBlank()

//[a-zA-Z]+