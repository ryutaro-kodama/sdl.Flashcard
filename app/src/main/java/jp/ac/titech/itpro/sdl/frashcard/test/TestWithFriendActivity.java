package jp.ac.titech.itpro.sdl.frashcard.test;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

import jp.ac.titech.itpro.sdl.frashcard.R;
import jp.ac.titech.itpro.sdl.frashcard.card.Card;
import jp.ac.titech.itpro.sdl.frashcard.databinding.TestContentsBackChoiceBinding;
import jp.ac.titech.itpro.sdl.frashcard.test.communication.CommunicationData;
import jp.ac.titech.itpro.sdl.frashcard.test.communication.CommunicationDataFactory;
import jp.ac.titech.itpro.sdl.frashcard.test.communication.CommunicationReader;
import jp.ac.titech.itpro.sdl.frashcard.test.connection.BluetoothSocketSingleton;
import jp.ac.titech.itpro.sdl.frashcard.thread.CommonThread;

public abstract class TestWithFriendActivity extends TestBackChoiceActivity {
    private final static String TAG = TestWithFriendActivity.class.getSimpleName();

    public static enum State {
        Connected,
        Answering,
        YouAnswered,
        FriendAnswered,
        YouWaiting,
        FriendWaiting,
        Disconnected,
    }

    protected TestContentsBackChoiceBinding binding;
    private BluetoothSocket socket;
    private CommonHandler handler;
    protected CommonThread thread;
    private Card card;
    protected CommunicationDataFactory communicationDataFactory;
    private State state;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initTesting() {
        Log.d(TAG, "initTesting");
        binding = (TestContentsBackChoiceBinding) setContent(R.layout.test_contents_back_choice);

        socket = BluetoothSocketSingleton.getSocketAndSetNull();
        handler = new CommonHandler(this);
        try {
            // Thread for communicating data.
            thread = new CommonThread(socket, handler);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static class CommonHandler extends Handler {
        WeakReference<TestWithFriendActivity> ref;

        CommonHandler(TestWithFriendActivity activity) {
            super(activity.getMainLooper());
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "handleMessage");
            TestWithFriendActivity activity = ref.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {
                case CommonThread.MSG_STARTED:
                    activity.setState(State.Connected);
                    break;
                case CommonThread.MSG_FINISHED:
//                    Toast.makeText(activity, R.string.toast_connection_closed, Toast.LENGTH_SHORT).show();
                    activity.setState(TestWithFriendActivitySender.State.Disconnected);
                    break;
                case CommonThread.MSG_RECEIVED:
                    CommunicationData data = (CommunicationData) msg.obj;
                    switch (data.getDataType()) {
                        case CommunicationData.CARD:
                            activity.setCard(data.getCard());
                            activity.displayCard();
                    }
                    break;
            }
        }
    }

    protected void setCard(Card card) {
        this.card = card;
    }

    protected Card getCard() {
        return this.card;
    }


    private void clickedCorrectChoice() {
        Log.d(TAG, "correct!!!");
    }

    private void clickedIncorrectChoice() {
        Log.d(TAG, "incorrect!!!");
    }

    protected void setState(State state){
        this.state = state;
    }

    @Override
    protected void finishTesting() {
        Log.d(TAG, "finishTesting");
        finish();
    }
}

