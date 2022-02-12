package phantomloader

import net.spartanb312.phantom.launch.InitializationManager
import phantomloader.AES.encode
import phantomloader.MixinCache.getMixins
import phantomloader.MixinCache.getRefMapFile
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

fun launch() {
    val socket = Socket("127.0.0.1", 31212)
    val inputF = DataInputStream(socket.getInputStream())
    val outputF = DataOutputStream(socket.getOutputStream())
    outputF.writeUTF("[HWID]$hardwareId")
    PhantomClassLoader(inputF)
    InitializationManager.init(MixinCache.refmapBytes.getRefMapFile(), MixinCache.mixinBytes.getMixins())
}

val hardwareId
    get() = (System.getenv("PROCESS_IDENTIFIER")
            + System.getenv("PROCESSOR_LEVEL")
            + System.getenv("PROCESSOR_REVISION")
            + System.getenv("PROCESSOR_ARCHITECTURE")
            + System.getenv("PROCESSOR_ARCHITEW6432")
            + System.getenv("NUMBER_OF_PROCESSORS")
            + System.getenv("COMPUTERNAME")).encode("MissingInAction")