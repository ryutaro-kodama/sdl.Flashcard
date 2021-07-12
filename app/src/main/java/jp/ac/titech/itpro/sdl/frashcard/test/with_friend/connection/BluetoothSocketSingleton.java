package jp.ac.titech.itpro.sdl.frashcard.test.with_friend.connection;

import android.bluetooth.BluetoothSocket;

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
