package me.benguiman.spainstats.ui

sealed class ScreenStatus
object ScreenError : ScreenStatus()
object ScreenLoading : ScreenStatus()
object ScreenSuccess : ScreenStatus()