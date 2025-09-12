package org.example.mason.todolist.utils

import javax.crypto.KeyGenerator
import java.util.Base64

//fun main() {
//    try {
//        // 使用 HMACSHA512 演算法產生金鑰
//        val keyGen = KeyGenerator.getInstance("HmacSHA512")
//
//        // 產生密鑰
//        val secretKey = keyGen.generateKey()
//
//        // 將密鑰編碼為 Base64 字串，以便儲存
//        val encodedKey = Base64.getEncoder().encodeToString(secretKey.encoded)
//
//        println("Generated HS512 Key (Base64 encoded):")
//        println(encodedKey)
//
//    } catch (e: Exception) {
//        println("An error occurred: ${e.message}")
//    }
//}