package net.spartanb312.phantom.server.utils

import net.spartanb312.phantom.server.SERVER_FILE_PATH
import java.io.*
import java.lang.Exception
import java.util.concurrent.CopyOnWriteArrayList

object HWIDManager {

    var HWID = CopyOnWriteArrayList<String>()
    private const val CONFIG_PATH = SERVER_FILE_PATH
    private const val HWID_CONFIG = CONFIG_PATH + "HWID.json"

    private fun toFiles(): File {
        return File(HWID_CONFIG)
    }

    fun addHWID(name: String) {
        if (!HWID.contains(name)) {
            HWID.add(name)
        }
    }

    fun removeHWID(name: String) {
        HWID.remove(name)
    }

    fun checkHWID(name: String): Boolean {
        return HWID.contains(name) || HWID.contains("[ALL]")
    }

    fun saveHWID() {
        if (!toFiles().exists()) {
            toFiles().parentFile.mkdirs()
            try {
                toFiles().createNewFile()
            } catch (e4: IOException) {
                e4.printStackTrace()
            }
        }
        try {
            val writer = BufferedWriter(FileWriter(HWID_CONFIG, false))
            for (hwid in HWID) {
                writer.write(hwid)
                writer.flush()
                writer.newLine()
            }
            writer.close()
        } catch (e: Exception) {
        }
    }

    fun loadHWID() {
        if (toFiles().exists()) {
            try {
                val reader = BufferedReader(FileReader(HWID_CONFIG))
                var line: String
                while (reader.readLine().also { line = it } != null) {
                    addHWID(line)
                }
                reader.close()
            } catch (ex: Exception) {
            }
        } else {
            saveHWID()
        }
    }

}