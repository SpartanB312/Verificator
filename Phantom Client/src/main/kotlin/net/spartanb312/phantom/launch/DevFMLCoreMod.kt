package net.spartanb312.phantom.launch

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets

/**
 * Use this to dev easily in IDE
 */
class DevFMLCoreMod : IFMLLoadingPlugin {

    var isObfuscatedEnvironment = false
    private val tempDir: String = System.getProperty("java.io.tmpdir")
    private val nextTempFile get() = File(tempDir, "+~JF$randomString.tmp")
    private val randomString: String
        get() {
            val allowedChars = ('0'..'9') + ('a'..'z') + ('A'..'Z')
            return (1..18)
                .map { allowedChars.random() }
                .joinToString("")
        }

    init {
        val refMapFile = nextTempFile.apply {
            try {
                FileOutputStream(this).let {
                    it.write(DevFMLCoreMod::class.java.getResourceAsStream("/mixins.phantom.refmap.json")!!.readBytes())
                    it.flush()
                    it.close()
                    this.deleteOnExit()
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }

        val mixinCache = mutableListOf<String>()

        Gson().fromJson(
            String(
                DevFMLCoreMod::class.java.getResourceAsStream("/mixins.phantom.json")!!.readBytes(),
                StandardCharsets.UTF_8
            ),
            JsonObject::class.java
        ).apply {
            getAsJsonArray("client")?.forEach {
                mixinCache.add(it.asString)
            }
            getAsJsonArray("mixins")?.forEach {
                mixinCache.add(it.asString)
            }
        }

        InitializationManager.init(refMapFile, mixinCache)
    }

    override fun getModContainerClass(): String? = null

    override fun getASMTransformerClass(): Array<String> = emptyArray()

    override fun getSetupClass(): String? = null

    override fun injectData(data: Map<String?, Any?>) {
        isObfuscatedEnvironment = (data["runtimeDeobfuscationEnabled"] as Boolean?)!!
    }

    override fun getAccessTransformerClass(): String? {
        return null
    }

}