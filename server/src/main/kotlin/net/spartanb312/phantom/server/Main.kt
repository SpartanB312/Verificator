package net.spartanb312.phantom.server

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.spartanb312.phantom.server.utils.HWIDManager
import net.spartanb312.phantom.server.utils.LogUtil.log
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.FileInputStream
import java.net.ServerSocket
import java.net.Socket

const val SERVER_PORT = 31212
const val SERVER_FILE_PATH = "server/"
const val CLIENT_FILE_PATH = "client.jar"

fun main() = runBlocking {

    log("Loading Server...")
    HWIDManager.loadHWID()
    val tcpServer = ServerSocket(SERVER_PORT)

    launch(Dispatchers.Default) {
        while (!tcpServer.isClosed) {
            val inputCommand = readLine()
            if (inputCommand != null) onCommand(inputCommand)
        }
    }

    log("Server loaded!")

    while (!tcpServer.isClosed) {
        log("Waiting for connections.")
        val clientSocket: Socket = tcpServer.accept()

        launch(Dispatchers.IO) {

            val input = DataInputStream(clientSocket.getInputStream())
            val output = DataOutputStream(clientSocket.getOutputStream())

            while (!clientSocket.isClosed) {

                val received: String = input.readUTF()
                log("IP(" + clientSocket.inetAddress.hostAddress.toString() + ")Send ：" + received)

                if (!received.startsWith("[")) {
                    clientSocket.close()
                    log("Socket Closed")
                }

                var prefix: String
                var content: String

                try {
                    prefix = received.substring(0, received.indexOf("]") + 1)
                    content = received.substring(received.indexOf("]") + 1)
                } catch (e: Exception) {
                    continue
                }

                if (prefix == "[HWID]") {
                    if (HWIDManager.checkHWID(content)) {
                        log("Sending file...")
                        output.sendFile(CLIENT_FILE_PATH)
                        clientSocket.close()
                    } else {
                        output.writeUTF("[FUCK]")
                        log("Not passed verification!")
                        clientSocket.close()
                    }
                    continue
                }
            }
        }
    }
}

fun onCommand(input: String) {
    val args = input.split(" ").toTypedArray()

    if (args[0].equals("/help", ignoreCase = true)) {
        log("Phantom Server Console V0.1")
        log("Author B_312")
        log("Commands: ")
        log("/list (Get all hwids)")
        log("/add <HWID> (Add HWID)")
        log("/remove <HWID> (Remove HWID)")
    }

    when {
        args[0].equals("/add", ignoreCase = true) -> {
            if (args.size <= 1) {
                log("Usage /add <HWID>")
                return
            }
            log("Added HWID " + args[1])
            HWIDManager.addHWID(args[1])
            HWIDManager.saveHWID()
        }
        args[0].equals("/list", ignoreCase = true) -> {
            log(HWIDManager.HWID.toString())
        }
        args[0].equals("/remove", ignoreCase = true) -> {
            if (args.size <= 1) {
                log("Usage /remove <HWID>")
                return
            }
            log("Removed HWID " + args[1])
            HWIDManager.removeHWID(args[1])
            HWIDManager.saveHWID()
        }
    }
}


fun DataOutputStream.sendFile(filePath: String) {
    val fileStream = DataInputStream(FileInputStream(filePath))
    val startTime = System.currentTimeMillis()
    try {
        val bufferSize = 8192
        val buf = ByteArray(bufferSize)
        while (true) {
            val read: Int = fileStream.read(buf)
            if (read == -1) {
                break
            }
            write(buf, 0, read)
        }
        flush()
    } catch (ignore: Exception) {
        log("Sending file failed")
    }
    fileStream.close()
    log("Finished sending file in ${System.currentTimeMillis() - startTime} ms")
}

