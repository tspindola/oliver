package com.example.thiagospindola.trmobiletef

class PPFuncs{
    companion object {
        val SYN:Byte = 22.toByte()
        val ETB:Byte = 23.toByte()
        val ACK:Byte = 6.toByte()
        val NAK:Byte = 15.toByte()
        val CAN:Byte = 24.toByte()
        val EOT:Byte = 4.toByte()

        fun prepareTxBuffer(data:String, size: Int, txBuffer:ByteArray){
            var dataByteArray = data.toByteArray(Charsets.US_ASCII)
            var toCalcCRC = ByteArray(dataByteArray.size+1)
            System.arraycopy(dataByteArray, 0, toCalcCRC, 0, dataByteArray.size)
            toCalcCRC[dataByteArray.size] = ETB
            var crcRet = crc16.calculate(toCalcCRC)

            txBuffer[0] = SYN
            for(i in 1..data.length)
                  txBuffer[i] = dataByteArray[i-1]
            txBuffer[size-3] = ETB
            txBuffer[size-2] = (crcRet/256).toByte()
            txBuffer[size-1] = (crcRet%256).toByte()
        }
    }
}