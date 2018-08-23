package com.example.thiagospindola.trmobiletef

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.widget.Toast
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import java.util.*
import java.io.*


var REQUEST_ENABLE_BT:Int = 999

lateinit var btDevice: BluetoothDevice
lateinit var btSocket: BluetoothSocket
lateinit var btInputStream: InputStream
lateinit var btOutputStream: OutputStream
lateinit var workerThread: Thread

//TODO: Organizar Vars
lateinit var btServerSocket: BluetoothServerSocket

var readBuffer: ByteArray = ByteArray(1024)
var bufferPosition: Int = 0
var stopWorker: Boolean = false

var senddatatest: Boolean = false

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBluetoothAdapter:BluetoothAdapter

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                //message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                //message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                //message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btOpen:Button = findViewById(R.id.btOpen)
        val btClose:Button = findViewById(R.id.btClose)
        val btSend:Button = findViewById(R.id.btSend)

        btOpen.setOnClickListener(this)
        btClose.setOnClickListener(this)
        btSend.setOnClickListener(this)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btOpen -> {
                //findBT()
                //openBT()
                var txBuffer = ByteArray(1)
                var rxBuffer = ByteArray(100)
                var iRet = serialOpen()
                if (iRet === 0) {
                    txBuffer[0] = 24.toByte()
                    iRet = serialTx(txBuffer, txBuffer.size)
                    if (iRet === 0) {
                        iRet = serialRx(80000, rxBuffer.size, rxBuffer)
                        if (iRet >= 0) {
                            if (rxBuffer[0] !== 4.toByte()) {
                                iRet = -83
                            } else {
                                iRet = 0
                            }
                        }
                    }
                }
            }
            R.id.btClose -> {
                //closeBT()
                serialClose()
            }

            R.id.btSend -> {
                //sendData()
                var txBuffer = ByteArray(6)
                var rxBuffer = ByteArray(100)
                var s:String = "OPN000"

                txBuffer = s.toByteArray(charset("ISO-8859-1"))
                var iRet = serialTx(txBuffer, txBuffer.size)
                if (iRet === 0) {
                    iRet = serialRx(5000, rxBuffer.size, rxBuffer)
                    if (iRet >= 0) {
                        if (rxBuffer[0] !== 0.toByte()) {
                            iRet = -83
                        } else {
                            iRet = 0
                        }
                    }
                }
            }
            else -> {

            }
        }
    }

    private fun findBT() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (!mBluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        val pairedDevices: Set<BluetoothDevice> = mBluetoothAdapter.getBondedDevices()
        if(pairedDevices.isNotEmpty())
        {
            for (device in pairedDevices) {
                if (device.name == "PAX-55555556") {
                    btDevice = device
                    break
                }
            }
        }
    }

    private fun openBT() {
        val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(uuid)
        btSocket.connect()
        btOutputStream = btSocket.getOutputStream()
        btInputStream = btSocket.getInputStream()

        listenForData()
    }

    private fun listenForData() {
        val handler = Handler()
        val delimiter: Byte = 10 //This is the ASCII code for a newline character

        stopWorker = false
        bufferPosition = 0

        readBuffer.fill(0,0, readBuffer.size)

        workerThread = Thread(Runnable
        {
            while(!Thread.currentThread().isInterrupted && !stopWorker)
            {
                try
                {
                    var bytesAvailable: Int = btInputStream.available()
                    if(bytesAvailable > 0)
                    {
                        var packetBytes = ByteArray(bytesAvailable)
                        btInputStream.read(packetBytes)
                        for(b in packetBytes)
                        {
                            if(b == delimiter)
                            {
                                var encodedBytes = ByteArray(bufferPosition)
                                System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.size)
                                var data = String(encodedBytes, charset("US_ASCII"))
                                bufferPosition = 0

                                Toast.makeText(this,data,Toast.LENGTH_LONG)

                                handler.post(Runnable
                                {
                                    Log.d("Message",data)
                                })
                            }
                            else
                            {
                                readBuffer[bufferPosition++] = b
                            }
                        }
                    }
                }
                catch (e: IOException)
                {
                    stopWorker = true
                }
            }
        })
        workerThread.start()
    }

    private fun sendData() {
        var msg:String
        if(!senddatatest)
            msg = "OPN000"
        else
            msg = "CLO032      PEAK          PAYMENTS    "
        btOutputStream.write(msg.toByteArray())

    }

    private fun closeBT() {
        stopWorker = true
        btOutputStream.close()
        btInputStream.close()
        btSocket.close()
    }




    private fun realizaConexao(): Int {
        if (btSocket.isConnected()) {
            Log.d("DBG - SerialBT","Dispositivo ja conectado!")
            return 0
        }
        mBluetoothAdapter.cancelDiscovery()
        try {
            btSocket.connect()
            Log.d("DBG - SerialBT","Conectado ao bluetooth")
//            val dados = ByteArray(1000)
//            var retorno: Int
//            do {
//                retorno = serialRx(30, dados.size, dados)
//                try {
//                    Thread.sleep(30)
//                    continue
//                } catch (e: InterruptedException) {
//                    continue
//                }
//
//            } while (retorno > 0)
            return 0
        } catch (e2: Exception) {
            Log.d("DBG - SerialBT","Erro ao conectar pinpad: " + e2.message)
            return PPRetCodes.PP_COMMERR
        }

    }

    private fun receiveConnections(): Int {
        try {
            btSocket = btServerSocket.accept()
            btServerSocket.close()
        } catch (e: IOException) {
        }

        return if (btSocket == null) {
            PPRetCodes.PP_COMMERR
        } else 0
    }

    fun serialOpen(): Int {
        val mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        try {
            var pairedDevices: Set<BluetoothDevice> = mBluetoothAdapter.getBondedDevices()
            if(pairedDevices.isNotEmpty())
            {
                for (device in pairedDevices) {
                    if (device.name == "PAX-55555556") {
                        btDevice = device
                        break
                    }
                }
            }
            for (i in 0..2) {
                Log.d("DBG - SerialBT", "Criando socket bluetooth, tentativa: " + i + 1)
                    btSocket = btDevice.createRfcommSocketToServiceRecord(mUUID)
                }
                try {
                    Thread.sleep(100)
                } catch (e2: InterruptedException) {
                    e2.printStackTrace()
                }
                if (realizaConexao() === 0) {
                    return 0
                }
            return PPRetCodes.PP_COMMERR
        } catch (e3: IllegalArgumentException) {
            Log.d("DBG - SerialBT","Erro de configuracao bluetooth: " + e3.message)
            return -5
        }
    }

    fun serialTx(dados: ByteArray, tamanho: Int): Int {
        if (btSocket == null || !btSocket.isConnected()) {
            Log.d("DBG - SerialBT","Dispositivo desconectado ao enviar")
            return PPRetCodes.PP_COMMERR
        }
        try {
            DataOutputStream(btSocket.getOutputStream()).write(dados, 0, tamanho)
            return 0
        } catch (e: IOException) {
            Log.d("DBG - SerialBT","Erro ao enviar dados pro pinpad: " + e.message)
            return PPRetCodes.PP_COMMERR
        }

    }

    fun serialRx(timeout: Long, tamanho: Int, dados: ByteArray): Int {
        if (timeout < 0 || tamanho <= 0) {
            return -8
        }
        if (btSocket == null || !btSocket.isConnected()) {
            Log.d("DBG - SerialBT","Dispositivo desconectado ao receber")
            return PPRetCodes.PP_COMMERR
        }
        val finalTime = System.currentTimeMillis() + timeout
        try {
            val input = DataInputStream(btSocket.inputStream)
            do {
                try {
                    Thread.sleep(30)
                } catch (e: InterruptedException) {
                }

                try {
                    if (input.available() > 0) {
                        var first = true
                        var bytesRecebidos = 0
                        while (true) {
                            if (!first) {
                                try {
                                    if (input.available() <= 0) {
                                        break
                                    }
                                } catch (e1: IOException) {
                                    Log.d("DBG - SerialBT","ERRO em HW_iBlueToothRxBlk #2:" + e1.message)
                                    return PPRetCodes.PP_COMMERR
                                }

                            }
                            try {
                                bytesRecebidos += input.read(dados, bytesRecebidos, tamanho - bytesRecebidos)
                                if (tamanho - bytesRecebidos <= 0) {
                                    return bytesRecebidos
                                }
                                try {
                                    Thread.sleep(50)
                                } catch (e2: InterruptedException) {
                                }

                                first = false
                            } catch (e3: IOException) {
                                Log.d("DBG - SerialBT","Dispositivo desconectado ao receber: " + e3.message)
                                return PPRetCodes.PP_COMMERR
                            }

                        }
                        return if (bytesRecebidos <= 0) {
                            PPRetCodes.PP_TIMEOUT
                        } else bytesRecebidos
                    }
                } catch (e4: IOException) {
                }

            } while (System.currentTimeMillis() <= finalTime)
            return PPRetCodes.PP_TIMEOUT
        } catch (e12: IOException) {
            Log.d("DBG - SerialBT","ERRO em HW_iBlueToothRxBlk:" + e12.message)
            return PPRetCodes.PP_COMMERR
        }

    }

    fun serialClose(): Int {
        try {
            btSocket.close()
            if (!mBluetoothAdapter.isDiscovering) {
                return 0
            }
            mBluetoothAdapter.cancelDiscovery()
            return 0
        } catch (e: IOException) {
            Log.d("DBG - SerialBT","Erro ao fechar socket bluetooth: " + e.message)
            return PPRetCodes.PP_COMMERR
        }

    }

    fun serialListen(): Int {
        if (btSocket.isConnected()) {
            return 0
        }
        val mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        mBluetoothAdapter.cancelDiscovery()
        Thread(Runnable {
            while (true) {
                try {
                    btServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("RFCOMM", mUUID)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                if (this.receiveConnections() === 0) {
                    while (btSocket.isConnected()) {
                        try {
                            Thread.sleep(1000)
                        } catch (e2: InterruptedException) {
                        }

                    }
                }
            }
        }).start()
        return 0
    }
}
