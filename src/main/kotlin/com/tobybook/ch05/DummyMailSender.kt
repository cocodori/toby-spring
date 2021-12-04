package com.tobybook.ch05

import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage

class DummyMailSender: MailSender {
    override fun send(simpleMessage: SimpleMailMessage) {
    }

    override fun send(vararg simpleMessages: SimpleMailMessage?) {
    }
}