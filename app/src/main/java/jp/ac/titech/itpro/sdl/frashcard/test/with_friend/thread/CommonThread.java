package jp.ac.titech.itpro.sdl.frashcard.test.with_friend.thread;

import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Handler;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import jp.ac.titech.itpro.sdl.frashcard.test.with_friend.communication.CommunicationData;
import jp.ac.titech.itpro.sdl.frashcard.test.with_friend.communication.CommunicationReader;
import jp.ac.titech.itpro.sdl.frashcard.test.with_friend.communication.CommunicationWriter;

public class CommonThread extends Thread {
    private final static String TAG = CommonThread.class.getSimpleName();

    public final static int MSG_STARTED = 1111;
    public final static int MSG_RECEIVED = 2222;
    public final static int MSG_FINISHED = 3333;

    private BluetoothSocket socket;
    private CommunicationReader reader;
    private CommunicationWriter writer;
    private Handler handler;
    private boolean writerClosed = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CommonThread(BluetoothSocket socket, Handler handler) throws IOException {
        // 画面更新用のhandlerも一緒に受け取る
        if (!socket.isConnected()) {
            throw new IOException("Socket is not connected");
        }
        this.socket = socket;
        this.handler = handler;

        Reader rawReader = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
        this.reader = new CommunicationReader(new JsonReader(rawReader));
        Writer rawWriter = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
        this.writer = new CommunicationWriter(new JsonWriter(rawWriter));
    }

    @Override
    public void run() {
        Log.d(TAG, "run");
        try (BluetoothSocket s = socket) {
            handler.sendMessage(handler.obtainMessage(MSG_STARTED, s.getRemoteDevice()));
            writer.beginArray();
            reader.beginArray();
            while (reader.hasNext()) {  // 読み口から、次のメッセージがないか確認
                // メッセージがあったら取り出して、handlerに送る
                handler.sendMessage(handler.obtainMessage(MSG_RECEIVED, reader.read()));
            }
            reader.endArray();
            if (!writerClosed) {
                writer.endArray();
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.sendMessage(handler.obtainMessage(MSG_FINISHED));
    }

    public void send(CommunicationData data) {
        Log.d(TAG, "send");
        try {
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        Log.d(TAG, "close");
        try {
            writer.endArray();
            writer.flush();
            writerClosed = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}