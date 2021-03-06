package com.nina.example.signature

import java.security.Key
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.Signature
import java.security.interfaces.DSAPrivateKey
import java.security.interfaces.DSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

object DSACoder {
    private const val KEY_ALGORITHM = "DSA"
    //default 1024, is multiple of 64, 512 ~ 1024
    private const val KEY_SIZE = 1024
    private const val SIGNATURE_ALGORITHM = "SHA1withDSA"

    private const val PUBLIC_KEY = "DSAPublicKey"
    private const val PRIVATE_KEY = "DSAPrivateKey"

    fun sign(data: ByteArray, privateKey: ByteArray): ByteArray {
        val pkcS8EncodedKeySpec = PKCS8EncodedKeySpec(privateKey)
        val keyFactory = KeyFactory.getInstance(KEY_ALGORITHM)
        val priKey = keyFactory.generatePrivate(pkcS8EncodedKeySpec)
        val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
        signature.initSign(priKey)
        signature.update(data)
        return signature.sign()
    }

    fun verify(data: ByteArray, publicKey: ByteArray, sign: ByteArray): Boolean {
        val x509EncodedKeySpec = X509EncodedKeySpec(publicKey)
        val keyFactory = KeyFactory.getInstance(KEY_ALGORITHM)
        val pubKey = keyFactory.generatePublic(x509EncodedKeySpec)
        val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
        signature.initVerify(pubKey)
        signature.update(data)
        return signature.verify(sign)
    }

    fun getPublicKey(map: Map<String, Key>): ByteArray {
        val key = map[PUBLIC_KEY]
        return key?.encoded ?: byteArrayOf()
    }

    fun getPrivateKey(map: Map<String, Key>): ByteArray {
        val key = map[PRIVATE_KEY]
        return key?.encoded ?: byteArrayOf()
    }

    fun initKey(): Map<String, Key> {
        val keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM)
        keyPairGenerator.initialize(KEY_SIZE)
        val keyPair = keyPairGenerator.generateKeyPair()
        val publicKey = keyPair.public as DSAPublicKey
        val privateKey = keyPair.private as DSAPrivateKey
        val map = HashMap<String, Key>(2)
        map[PUBLIC_KEY] = publicKey
        map[PRIVATE_KEY] = privateKey
        return map
    }
}