package pe.pcs.maestrodetallesqlserver.core

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object UtilsSecurity {

    // respetar la longitud de la clave a 16 digitos
    private val valor_clave = "Z&K8xD#T@WC=KLQI".toByteArray()

    fun cifrarDato(datoSinEncriptar: String): String {
        try {
            val cipher = Cipher.getInstance("AES")
            cipher.init(
                Cipher.ENCRYPT_MODE,
                SecretKeySpec(valor_clave, "AES")
            )

            val encriptar = cipher.doFinal(datoSinEncriptar.toByteArray(charset("UTF-8")))

            return Base64.encodeToString(encriptar, Base64.DEFAULT)
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    fun descifrarDato(datoEncriptado: String): String {
        try {
            val cipher = Cipher.getInstance("AES")
            cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(valor_clave, "AES")
            )

            val desencriptado = cipher.doFinal(
                Base64.decode(datoEncriptado.toByteArray(charset("UTF-8")), Base64.DEFAULT)
            )

            return String(desencriptado, charset("UTF-8"))
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

}