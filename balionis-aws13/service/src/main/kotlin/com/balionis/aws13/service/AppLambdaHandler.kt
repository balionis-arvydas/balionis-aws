package com.balionis.aws13.service

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class AppLambdaHandler {
    fun handle(msg: String): String {
        logger.info { "handle: msg=$msg" }

        return msg.toUpperCase()
    }
}
