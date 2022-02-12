package net.spartanb312.phantom

import net.spartanb312.phantom.util.Logger

/**
 * The main mod
 */
object Phantom {

    fun onPreInit() {
        Logger.fatal("Pre init")
    }

    fun onInit() {
        Logger.fatal("Init")
    }

    fun onPostInit() {
        Logger.fatal("Post Init")
    }

    fun onReady() {
        Logger.fatal("Ready")
    }

}