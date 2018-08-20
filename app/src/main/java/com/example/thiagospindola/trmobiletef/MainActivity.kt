package com.example.thiagospindola.trmobiletef

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.bluetooth.BluetoothAdapter
import android.widget.Toast
import android.content.Intent

var REQUEST_ENABLE_BT:Int = 999


class MainActivity : AppCompatActivity() {

    private lateinit var mBluetoothAdapter:BluetoothAdapter

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if(checkIfBtIsPresent())
        {
            turnOnBt()
        }
    }

    private fun turnOnBt():Boolean {
        if (!mBluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }

    private fun checkIfBtIsPresent():Boolean {
        var ret:Boolean
        if (mBluetoothAdapter == null) {
            Toast.makeText(this,"Bluetooth is not available in your phone!")
            ret = false
        }
        else
            ret =  true

        return ret
    }
}
