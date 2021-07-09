package jp.ac.titech.itpro.sdl.frashcard.test.connection;

import android.bluetooth.BluetoothSocket;

import java.net.Socket;

public class BluetoothSocketSingleton {
    private static BluetoothSocket socket;

    public static void setSocket(BluetoothSocket socketpass){
        BluetoothSocketSingleton.socket = socketpass;
    }

    public static BluetoothSocket getSocketAndSetNull(){
        BluetoothSocket tmp = BluetoothSocketSingleton.socket;
        BluetoothSocketSingleton.socket = null;
        return tmp;
    }
}
