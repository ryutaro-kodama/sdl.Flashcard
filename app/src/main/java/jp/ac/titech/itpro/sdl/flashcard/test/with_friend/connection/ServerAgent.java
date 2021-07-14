package jp.ac.titech.itpro.sdl.flashcard.test.with_friend.connection;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.lang.ref.WeakReference;

import jp.ac.titech.itpro.sdl.flashcard.R;
import jp.ac.titech.itpro.sdl.flashcard.test.with_friend.TestWithFriendHomeActivity;
import jp.ac.titech.itpro.sdl.flashcard.test.with_friend.TestWithFriendHomeActivity.State;

public class ServerAgent extends Agent {
    private final static String TAG = ServerAgent.class.getSimpleName();

    private final static int SERVER_TIMEOUT_SEC = 90;

    private final static int REQ_DISCOVERABLE = 3333;

    private ServerTask task;

    private BluetoothAdapter adapter;

    public ServerAgent(TestWithFriendHomeActivity activity) {
        super(activity);
    }

    public void start(BluetoothAdapter adapter) {
        Log.d(TAG, "start");

        // Start bluetooth adapter.
        this.adapter = adapter;
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, SERVER_TIMEOUT_SEC);
        activity.startActivityForResult(intent, REQ_DISCOVERABLE);
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, @SuppressWarnings("unused") Intent data) {
        if (reqCode == REQ_DISCOVERABLE) {
            Log.d(TAG, "onActivityResult: reqCode=" + reqCode + " resCode=" + resCode);
            if (resCode == Activity.RESULT_CANCELED) {
                activity.setState(State.Disconnected);
                return;
            }
            start1();
        }
    }

    private void start1() {
        Log.d(TAG, "start1");

        // Let 'ClientTask' socket connection.
        task = new ServerTask(this);
        task.execute(adapter);
        activity.setState(State.Waiting);
    }

    // This is the thread which connects socket communication.
    private static class ServerTask extends AsyncTask<BluetoothAdapter, Void, BluetoothSocket> {
        private BluetoothServerSocket serverSocket;
        private final WeakReference<ServerAgent> ref;
        ServerTask(ServerAgent server) {
            super();

            // Hold the weak reference to client agent (outer class).
            ref = new WeakReference<>(server);
        }

        @Override
        protected BluetoothSocket doInBackground(BluetoothAdapter... params) {
            Log.d(TAG, "doInBackground");
            BluetoothAdapter adapter = params[0];
            String name = adapter.getName();
            try (BluetoothServerSocket sock = adapter.listenUsingRfcommWithServiceRecord(name, TestWithFriendHomeActivity.SPP_UUID)) {
                // Create server socket.
                serverSocket = sock;
                return sock.accept(SERVER_TIMEOUT_SEC * 1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(BluetoothSocket socket) {
            Log.d(TAG, "onPostExecute");
            ServerAgent server = ref.get();
            if (server != null) {
                server.start2(socket);
            }
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "onCancelled");
            ServerAgent server = ref.get();
            if (server != null) {
                server.cancel();
            }
        }

        void stop() {
            Log.d(TAG, "stop");
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cancel(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void start2(BluetoothSocket socket) {
        Log.d(TAG, "start2");
        if (socket == null) {
            Toast.makeText(activity, R.string.toast_connection_failed, Toast.LENGTH_SHORT).show();
            activity.setState(State.Disconnected);
            return;
        }

        // Back process to "TestWithFriendHomeActivity".
        activity.finishConnecting(socket);
    }

    private void cancel() {
        Log.d(TAG, "cancel");
        activity.setState(State.Disconnected);
        task = null;
    }

    void stop() {
        Log.d(TAG, "stop");
        if (task != null) {
            task.stop();
        }
    }
}
