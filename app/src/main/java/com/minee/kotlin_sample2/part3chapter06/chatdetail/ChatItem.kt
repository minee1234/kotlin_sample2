package com.minee.kotlin_sample2.part3chapter06.chatdetail

data class ChatItem(
    val senderId: String,
    val message: String
) {
    constructor(): this("", "")
}
