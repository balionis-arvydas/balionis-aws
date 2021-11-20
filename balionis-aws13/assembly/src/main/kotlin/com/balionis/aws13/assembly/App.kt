package com.balionis.aws13.assembly

import mu.KotlinLogging
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    logger.info { "main: args=${args.joinToString()}" }

    runCatching {
        App(Configuration.load()).let {
            Runtime.getRuntime().addShutdownHook(
                Thread {
                    logger.info { "main: closing" }
                    it.close()
                    logger.info { "main: closed" }
                }
            )
        }
    }.onFailure {
        logger.error(it) { "main: unhandled exception ${it.message}" }
        exitProcess(1)
    }
}

class App(private val config: Configuration) : AutoCloseable {

    init {
        logger.info { "init: config=$config" }
    }

    override fun close() {
        runCatching {
            logger.info { "close: +" }
        }.onFailure {
            logger.error(it) { "close: failed to close. ${it.message}" }
        }
    }
}
