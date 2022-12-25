package com.example.udptest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.InetAddress

class MainViewModel : ViewModel() {
    private val msg = ""

    init {
        viewModelScope.launch {
            sendMsg(msg)
        }
    }

    fun sendMsg(str : String) {
        viewModelScope.launch {
            val bytes = str.toByteArray()
            UDPManager.sendPacket = DatagramPacket(
                bytes,
                bytes.size,
                withContext(Dispatchers.IO) {
                    InetAddress.getByName(UDPManager.dstIP)
                },
                UDPManager.dstPort
            )
            withContext(Dispatchers.IO) {
                UDPManager.listenSocket.send(UDPManager.sendPacket)
            }
        }
    }
}