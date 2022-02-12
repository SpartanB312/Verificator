package phantomloader

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Use this to encrypt
 */
object AES {

    private const val STANDARD_NAME = "AES"
    private const val SPARTAN_KEY = "spartanb312isgod"

    fun String.encode(key: String): String = Cipher.getInstance(STANDARD_NAME).let {
        val secretKeySpec = SecretKeySpec(key.processKey().toByteArray(), STANDARD_NAME)
        it.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        val result = it.doFinal(this.toByteArray())
        String(Base64.getEncoder().encode(result))
    }

    fun String.decode(key: String): String = Cipher.getInstance(STANDARD_NAME).let {
        val secretKeySpec = SecretKeySpec(key.processKey().toByteArray(), STANDARD_NAME)
        it.init(Cipher.DECRYPT_MODE, secretKeySpec)
        val result = it.doFinal(Base64.getDecoder().decode(this))
        String(result)
    }

    private fun String.processKey(): String = when {
        this.length < 16 -> {
            this + SPARTAN_KEY.substring(this.length)
        }
        this.length > 16 -> {
            this.substring(0, 8) + this.substring(this.length - 8, this.length)
        }
        else -> this
    }

}