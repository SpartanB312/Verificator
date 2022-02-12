package net.spartanb312.phantom.launch

import org.apache.logging.log4j.LogManager
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.Mixins
import java.io.File

/**
 * The hook of the mod
 */
object InitializationManager {

    private val logger = LogManager.getLogger("Phantom")

    lateinit var mixinRefmapFile: File
    lateinit var mixinCache: List<String>

    fun init(mixinRefmapFile: File, mixinCache: List<String>) {
        this.mixinCache = mixinCache
        this.mixinRefmapFile = mixinRefmapFile
        loadMixin()
    }

    private fun loadMixin() {
        MixinBootstrap.init()
        logger.info("Initializing Phantom mixins")
        Mixins.addConfiguration("mixins.phantom.loader.json")
        MixinEnvironment.getDefaultEnvironment().obfuscationContext = "searge"
        MixinEnvironment.getDefaultEnvironment().side = MixinEnvironment.Side.CLIENT
        logger.info("Phantom mixins initialized")
        logger.info(MixinEnvironment.getDefaultEnvironment().obfuscationContext)
    }

}