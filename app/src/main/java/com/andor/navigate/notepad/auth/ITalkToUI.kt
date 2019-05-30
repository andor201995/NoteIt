package com.andor.navigate.notepad.auth

interface ITalkToUI {
    fun signingInSuccess(userModel: UserModel)
    fun signingInFailed()

}
