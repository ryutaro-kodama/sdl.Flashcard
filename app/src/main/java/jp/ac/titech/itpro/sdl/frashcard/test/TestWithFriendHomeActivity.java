package jp.ac.titech.itpro.sdl.frashcard.test;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import jp.ac.titech.itpro.sdl.frashcard.R;
import jp.ac.titech.itpro.sdl.frashcard.test.connection.Agent;
import jp.ac.titech.itpro.sdl.frashcard.test.connection.BluetoothInitializer;
import jp.ac.titech.itpro.sdl.frashcard.test.connection.BluetoothSocketSingleton;
import jp.ac.titech.itpro.sdl.frashcard.test.connection.ClientAgent;
import jp.ac.titech.itpro.sdl.frashcard.test.connection.ServerAgent;

public class TestWithFriendHomeActivity  extends AppCompatActivity {
    private final static String TAG = TestWithFriendHomeActivity.class.getSimpleName();

    public final static UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter adapter;

    public enum State {
        Initializing,
        Disconnected,
        Connecting,
        Connected,
        Waiting
    }
    private State state = State.Initializing;

    private Agent agent;
    private BluetoothInitializer initializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_test_with_friend_home);

        setState(State.Initializing);

        // Initialize bluetooth
        initializer = new BluetoothInitializer(this) {
            @Override
            protected void onReady(BluetoothAdapter adapter) {
                TestWithFriendHomeActivity.this.adapter = adapter;
                setState(State.Disconnected);
            }
        };
        initializer.initialize();

        // Set connect button to connect other device.
        Button buttonConnect = findViewById(R.id.test_with_friend_home_connect);
        buttonConnect.setOnClickListener(v -> {
            agent = new ClientAgent(this);
            ((ClientAgent) agent).connect();  // Start "ScanActivity".
        });

        // Set accept button to accept connection from other device.
        Button buttonAccept = findViewById(R.id.test_with_friend_home_accept);
        buttonAccept.setOnClickListener(v -> {
            agent = new ServerAgent(this);
            ((ServerAgent) agent).start(adapter);
        });
    }

    public void finishConnecting(BluetoothSocket socket) {
        // Save the socket.
        BluetoothSocketSingleton.setSocket(socket);

        Intent intent = new Intent(this, TestWithFriendActivity.class);
//        agent.isSenderOrReceiver();
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (state == State.Connected && agent != null) {
            agent.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu");
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        return true;
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        Log.d(TAG, "onActivityResult: reqCode=" + reqCode + " resCode=" + resCode);
        initializer.onActivityResult(reqCode, resCode, data); // delegate
        if (agent != null) {
            agent.onActivityResult(reqCode, resCode, data); // delegate
        }
    }

    public void setState(State state) {
        setState(state, null);
    }

    public void setState(State state, String arg) {
        this.state = state;
        switch (state) {
            case Initializing:
            case Disconnected:
//                status.setText(R.string.main_status_disconnected);
                break;
            case Connecting:
//                status.setText(getString(R.string.main_status_connecting_to, arg));
                break;
            case Connected:
//                status.setText(getString(R.string.main_status_connected_to, arg));
                break;
            case Waiting:
//                status.setText(R.string.main_status_listening_for_incoming_connection);
                break;
        }
        invalidateOptionsMenu();
    }

//    private void disconnect() {
//        Log.d(TAG, "disconnect");
//        agent.close();
//        agent = null;
//        setState(State.Disconnected);
//    }
}
