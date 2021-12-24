package com.tobybook.ch06

class Message private constructor(
    var text: String
) {
    companion object { fun newMessage(text: String) = Message(text) }
}