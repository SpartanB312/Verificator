package net.spartanb312.authserver.utils

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object LogUtil {

    private lateinit var bufferedWriter: BufferedWriter

    private var file:File = File("AuthServer/logs/" + getDate() + "log.txt")

    init {
        try {
            if (!file.exists()) {
                try {
                    file.parentFile.mkdirs()
                    file.createNewFile()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            bufferedWriter = BufferedWriter(FileWriter(file))
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    fun log(msg: String) {
        println("[AuthServer][" + getDate() + "]" + msg)
        saveLog("[AuthServer][" + getDate() + "]" + msg)
    }

    private fun saveLog(string: String) {
        try {
            bufferedWriter.write(string)
            bufferedWriter.flush()
            bufferedWriter.newLine()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getDate(): String {
        val date = Date()
        val format = SimpleDateFormat("MM-dd hh-mm-ss")
        return format.format(date)
    }
}