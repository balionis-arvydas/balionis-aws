package com.balionis.aws13.assembly

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract

data class Configuration(
    val application: AppConfiguration
) {
    companion object {
        fun load() = ConfigFactory.load().extract<Configuration>()
    }
}

data class AppConfiguration(
    val namespace: String
)
