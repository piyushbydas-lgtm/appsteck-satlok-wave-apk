package com.example.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppLanguage {
    HINDI,
    ENGLISH
}

object LanguageManager {
    private val _currentLanguage = MutableStateFlow(AppLanguage.HINDI)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == AppLanguage.HINDI) AppLanguage.ENGLISH else AppLanguage.HINDI
    }

    // Dynamic strings provider
    fun get(hindi: String, english: String): String {
        return if (_currentLanguage.value == AppLanguage.HINDI) hindi else english
    }
}
