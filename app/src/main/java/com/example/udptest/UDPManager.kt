package com.example.udptest
// Author: LC
//Time: 2020/2/12/15:15

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

object UDPManager{
    lateinit var listenSocket:DatagramSocket

    lateinit var listenPacket : DatagramPacket  //Waiting to listen to the package
    lateinit var sendPacket : DatagramPacket    //Package sent to destination
    val RecvBuf = ByteArray(1024)
    val ReplyBuf = ByteArray(1024)
    var dstIP = "127.0.0.1" //Default destination IP
    var dstPort = 12306     //Default destination port

    fun init(){
        listenSocket  = DatagramSocket(12306)//Socket for monitoring
    }

    fun init(port:Int)
    {
        listenSocket  = DatagramSocket(port)//Socket for monitoring
    }

    fun setIPandPort(dstIP:String,dstPort:Int)
    {
        this.dstIP =dstIP
        this.dstPort = dstPort
    }

    fun close(){
        listenSocket.close()
        listenSocket.close()
    }

    //Use port 12307 to set and send information to the target
    fun sendMsg(str:String){
            val bytes = str.toByteArray()
            sendPacket = DatagramPacket(bytes, bytes.size, InetAddress.getByName(dstIP), dstPort)
            listenSocket.send(sendPacket)
    }

    fun sendMsg(bytes: ByteArray){
        sendPacket = DatagramPacket(bytes,bytes.size, InetAddress.getByName(dstIP), dstPort)
        listenSocket.send(sendPacket)
    }

    //Use port 12306 to reply to the target information (provided that the information has been received)
    fun sendReplyMsg(str:String){
        val bytes = str.toByteArray()
        listenPacket = DatagramPacket(bytes,bytes.size, listenPacket.address, listenPacket.port)
        listenSocket.send(listenPacket)
    }
    fun sendReplyMsg(bytes: ByteArray){
        listenPacket = DatagramPacket(bytes,bytes.size, listenPacket.address, listenPacket.port)
        listenSocket.send(listenPacket)
    }

    //Receive the information from the server reply
    fun recvReplyMsg()
    {
        recv(listenSocket,ReplyBuf)
    }

    //Information from the client
    fun recvMsg()
    {
        listenPacket=recv(listenSocket,RecvBuf)
    }

    //Get the other party's IP
    fun getHisIP():InetAddress{
        return listenPacket.address
    }

    private fun recv(socket: DatagramSocket, receiveBuf: ByteArray): DatagramPacket {
        val receiverPacket = DatagramPacket(receiveBuf, receiveBuf.size)
        socket.receive(receiverPacket)
        return receiverPacket
    }
}
