package com.kttstask.util

import android.icu.text.StringSearch
import android.util.Patterns
import com.kttstask.App
import com.kttstask.R
import com.kttstask.data.models.User
import io.realm.kotlin.ext.query

fun validateName(name: String): ValidationResult {
    if (name.isBlank()) {
        return ValidationResult(isSuccess = false, errorMsgId = R.string.err_empty_name)
    }

    if (!name.all { it.isLetter() }) {
        return ValidationResult(isSuccess = false, errorMsgId = R.string.err_invalid_name)
    }

    return ValidationResult(isSuccess = true)
}

fun validateEmailAddress(email: String, excludeEmailSearch: Boolean = false): ValidationResult {
    if (email.isBlank()) {
        return ValidationResult(isSuccess = false, errorMsgId = R.string.err_empty_email)
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return ValidationResult(isSuccess = false, errorMsgId = R.string.err_invalid_email)
    }

    if (!excludeEmailSearch) {
        val emailSearchResult = App.realm.query<User>(query = "emailAddress == $0", args = arrayOf(email)).find().size
        if (emailSearchResult > 0) {
            return ValidationResult(isSuccess = false, errorMsgId = R.string.err_duplicate_email)
        }
    }

    return ValidationResult(isSuccess = true)
}

fun validatePassword(password: String): ValidationResult {
    if (password.isBlank()) {
        return ValidationResult(isSuccess = false, errorMsgId = R.string.err_empty_password)
    }

    if (password.length < 8) {
        return ValidationResult(isSuccess = false, errorMsgId = R.string.err_password_length)
    }

    return ValidationResult(isSuccess = true)
}

fun validateRepeatedPassword(password: String, repeatedPassword: String): ValidationResult {
    if (repeatedPassword.isBlank()) {
        return ValidationResult(isSuccess = false, errorMsgId = R.string.err_empty_password)
    }

    if (password != repeatedPassword) {
        return ValidationResult(isSuccess = false, errorMsgId = R.string.err_password_not_matched)
    }

    return ValidationResult(isSuccess = true)
}

data class ValidationResult(
    val isSuccess: Boolean,
    val errorMsgId: Int? = null
)