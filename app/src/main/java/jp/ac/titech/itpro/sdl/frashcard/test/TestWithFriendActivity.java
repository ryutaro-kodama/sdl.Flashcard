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

public abstract class TestWithFriendActivity extends TestActivity {
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

    @Override
    protected void displayCard() {
        Log.d(TAG, "displayCard");

        // Set card data to layout by using "data binding".
        Card card = getNextCard();
        while (card.hasNoChoice()) {
            // Skip while the card has choice.
            card = getNextCard();
        }
        binding.setCard(card);

        // Next button and finish button are invisible at first.
        setNextAndFinishButton();

        // Shuffle choices.
        ArrayList<String> choiceList = card.getChoiceList();
        Collections.shuffle(choiceList);

        // Bind data of choices.
        binding.setChoice1(choiceList.get(0));
        binding.setChoice2(choiceList.get(1));

        // Create listener object which is called when choices are clicked.
        Card finalCard = card;
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clickedChoice = ((Button) view).getText().toString();

                if (clickedChoice.equals(finalCard.getBackTrue())) {
                    clickedCorrectChoice();
                } else {
                    clickedIncorrectChoice();
                }

                // If "Answer" button clicked, make finish button and answer text visible.
                visibleNextAndFinishButton();
            }
        };

        // Set event lister.
        Button buttonChoice1 = findViewById(R.id.test_choice1_button);
        buttonChoice1.setOnClickListener(buttonClick);
        Button buttonChoice2 = findViewById(R.id.test_choice2_button);
        buttonChoice2.setOnClickListener(buttonClick);

        // When choices are three, set third choice.
        Button buttonChoice3 = findViewById(R.id.test_choice3_button);
        if (choiceList.size() == 3) {
            binding.setChoice3(choiceList.get(2));
            buttonChoice3.setOnClickListener(buttonClick);
        } else {
            buttonChoice3.setVisibility(View.GONE);
        }
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

