package phantomloader

import net.minecraft.launchwrapper.Launch
import net.minecraft.launchwrapper.LaunchClassLoader
import java.io.InputStream
import java.util.zip.ZipInputStream

class PhantomClassLoader(inputF: InputStream) {

    init {
        @Suppress("UNCHECKED_CAST")
        val resourceCache = LaunchClassLoader::class.java.getDeclaredField("resourceCache").let {
            it.isAccessible = true
            it[Launch.classLoader] as MutableMap<String, ByteArray>
        }
        ZipInputStream(inputF).use { zipStream ->
            //Define classes and get mixin files
            while (true) {
                val zipEntry = zipStream.nextEntry ?: break
                if (zipEntry.name.endsWith(".class")) {
                    resourceCache[zipEntry.name.removeSuffix(".class").replace('/', '.')] = zipStream.readBytes()
                } else {
                    //Cache the mixin
                    if (zipEntry.name == "mixins.phantom.json") {
                        MixinCache.mixinBytes = zipStream.readBytes()
                    } else if (zipEntry.name == "mixins.phantom.refmap.json") {
                        MixinCache.refmapBytes = zipStream.readBytes()
                    }
                }
            }
        }
    }

}