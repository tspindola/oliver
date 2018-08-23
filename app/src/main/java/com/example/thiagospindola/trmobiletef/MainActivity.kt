package com.example.thiagospindola.trmobiletef

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.widget.Toast
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*


var REQUEST_ENABLE_BT:Int = 999

lateinit var btDevice: BluetoothDevice
lateinit var btSocket: BluetoothSocket
lateinit var btInputStream: InputStream
lateinit var btOutputStream: OutputStream
lateinit var workerThread: Thread

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
                findBT()
                openBT()
            }
            R.id.btClose -> {
                closeBT()
            }

            R.id.btSend -> {
                sendData()
            }
            else -> {

            }
        }
    }

    private fun findBT() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (mBluetoothAdapter == null) {
            Toast.makeText(this,"Bluetooth is not available in your phone!",Toast.LENGTH_LONG)
        }
        else if (!mBluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

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
}
