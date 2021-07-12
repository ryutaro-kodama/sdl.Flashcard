package jp.ac.titech.itpro.sdl.frashcard.test;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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
        Log.d(TAG, "finishConnecting");
        // Save the socket.
        BluetoothSocketSingleton.setSocket(socket);

        // According to agent, start testing activity.
        Intent intent = null;
        if (agent instanceof ClientAgent) {
            intent = new Intent(this, TestWithFriendActivitySender.class);
        } else if (agent instanceof ServerAgent) {
            intent = new Intent(this, TestWithFriendActivityReceiver.class);
        } else {
            assert false;
        }
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
        Log.d(TAG, "setState");
        this.state = state;
    }
}
