package net.spartanb312.phantom.util

import org.apache.logging.log4j.LogManager

object Logger {

    private val logger = LogManager.getLogger("Phantom")

    fun debug(str: String) = logger.debug(str)

    fun info(str: String) = logger.info(str)

    fun warn(str: String) = logger.warn(str)

    fun error(str: String) = logger.error(str)

    fun fatal(str: String) = logger.fatal(str)

}