package com.example.grocery.helpers

import java.net.InetAddress

class Functions {

    companion object {
        fun isInternetAvailable(): Boolean {
            return try {
                val ipAddr = InetAddress.getByName("www.google.com")
                //You can replace it with your name
                !ipAddr.equals("")

            } catch (e: Exception) {
                false
            }
        }
    }


}