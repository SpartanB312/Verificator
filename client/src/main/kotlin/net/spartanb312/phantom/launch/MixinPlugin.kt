package net.spartanb312.phantom.launch

import net.minecraft.launchwrapper.Launch
import net.minecraftforge.fml.relauncher.FMLLaunchHandler
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo
import java.net.MalformedURLException

/**
 * Author B_312
 * Use this to update mixin remotely
 */
class MixinPlugin : IMixinConfigPlugin {

    private var mixins: MutableList<String> = ArrayList()

    override fun onLoad(mixinPackage: String) {
        try {
            Launch.classLoader.addURL(InitializationManager.mixinRefmapFile.toURI().toURL())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        InitializationManager.mixinRefmapFile.deleteOnExit()

        InitializationManager.mixinCache.forEach {
            mixins.add(it)
            if (FMLLaunchHandler.isDeobfuscatedEnvironment()) {
                runCatching {
                    Launch.classLoader.loadClass(it)
                }
            }
        }
    }

    override fun getRefMapperConfig(): String {
        return try {
            InitializationManager.mixinRefmapFile.toURI().toURL().toString()
        } catch (e: MalformedURLException) {
            "mixins.phantom.refmap.json"
        }
    }

    override fun shouldApplyMixin(targetClassName: String?, mixinClassName: String?): Boolean {
        return true
    }

    override fun acceptTargets(myTargets: MutableSet<String>?, otherTargets: MutableSet<String>?) {
    }

    override fun getMixins(): MutableList<String> {
        return mixins
    }

    override fun preApply(
        targetClassName: String?,
        targetClass: org.objectweb.asm.tree.ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
    }

    override fun postApply(
        targetClassName: String?,
        targetClass: org.objectweb.asm.tree.ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
    }

}