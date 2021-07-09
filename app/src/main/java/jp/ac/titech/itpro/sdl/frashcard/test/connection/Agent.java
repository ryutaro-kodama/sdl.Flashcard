package jp.ac.titech.itpro.sdl.frashcard.test.connection;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
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

import jp.ac.titech.itpro.sdl.frashcard.test.TestWithFriendHomeActivity;
import jp.ac.titech.itpro.sdl.frashcard.test.TestWithFriendHomeActivity.State;

public abstract class Agent {
    private final static String TAG = Agent.class.getSimpleName();

    final TestWithFriendHomeActivity activity;

    Agent(TestWithFriendHomeActivity activity) {
        this.activity = activity;
    }

    public void close() {
//        if (thread != null) {
//            thread.close();
//            thread = null;
//        }
    }

    public abstract void onActivityResult(int reqCode, int resCode, Intent data);
}
