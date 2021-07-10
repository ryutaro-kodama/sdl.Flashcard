package jp.ac.titech.itpro.sdl.frashcard.test;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

import jp.ac.titech.itpro.sdl.frashcard.R;
import jp.ac.titech.itpro.sdl.frashcard.card.Card;
import jp.ac.titech.itpro.sdl.frashcard.databinding.TestContentsBackChoiceBinding;
import jp.ac.titech.itpro.sdl.frashcard.databinding.TestContentsWithFriendBinding;
import jp.ac.titech.itpro.sdl.frashcard.test.communication.CommunicationData;
import jp.ac.titech.itpro.sdl.frashcard.test.communication.CommunicationDataFactory;
import jp.ac.titech.itpro.sdl.frashcard.test.communication.CommunicationReader;
import jp.ac.titech.itpro.sdl.frashcard.test.connection.BluetoothSocketSingleton;
import jp.ac.titech.itpro.sdl.frashcard.thread.CommonThread;

public abstract class TestWithFriendActivity extends TestActivity {
    private final static String TAG = TestWithFriendActivity.class.getSimpleName();

    public static enum State {
        Connected,
        CardReceived,
        CanDisplayCard,
        Answering,
        YouAnswered,
        FriendAnswered,
        BothWaiting,
        YouWaiting,
        FriendWaiting,
        Disconnected,
    }

    protected TestContentsWithFriendBinding binding;
    private BluetoothSocket socket;
    private CommonHandler handler;
    protected CommunicationDataFactory communicationDataFactory;
    protected CommonThread thread;

    private Card card;
    private State state;
    private ImageView imageView = null;
    private ImageView friendImageView = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initTesting() {
        Log.d(TAG, "initTesting");
        binding = (TestContentsWithFriendBinding) setContent(R.layout.test_contents_with_friend);

        socket = BluetoothSocketSingleton.getSocketAndSetNull();
        communicationDataFactory = new CommunicationDataFactory();
        handler = new CommonHandler(this);
        try {
            // Thread for communicating data.
            thread = new CommonThread(socket, handler);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void displayCard(Card card) {
        Log.d(TAG, "displayCard");
        if (state != State.CanDisplayCard) return;

        if (imageView != null) {
            imageView.setVisibility(View.INVISIBLE);
        }
        if (friendImageView != null) {
            friendImageView.setVisibility(View.INVISIBLE);
        }

        // Set card data to layout by using "data binding".
        binding.setCard(card);

        // Next button and finish button are invisible at first.
        setNextAndFinishButton();

        // Shuffle choices.
        ArrayList<String> choiceList = card.getChoiceList();
        binding.setChoices(choiceList.toArray(new String[choiceList.size()]));

        // Create listener object which is called when choices are clicked.
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clickedChoice = ((Button) view).getText().toString();

                // Get "ImageView" to display correct or incorrect.
                int imageViewId = getImageViewId(view);
                imageView = findViewById(imageViewId);
                imageView.setVisibility(View.VISIBLE);

                // Set image to display correct or incorrect..
                if (clickedChoice.equals(card.getBackTrue())) {
                    imageView.setImageResource(R.drawable.correct);
                } else {
                    imageView.setImageResource(R.drawable.incorrect);
                }

                // Get answered index.
                int answerIndex = -1;
                if (view.getId() == R.id.test_choice1_button) {
                    answerIndex = 0;
                } else if (view.getId() == R.id.test_choice2_button) {
                    answerIndex = 1;
                } else if (view.getId() == R.id.test_choice3_button) {
                    answerIndex = 2;
                } else {
                    assert false;
                }
                thread.send(communicationDataFactory.makeAnswer(answerIndex));

                setState(State.YouAnswered);
            }
        };

        // Set event lister.
        Button buttonChoice1 = findViewById(R.id.test_choice1_button);
        buttonChoice1.setOnClickListener(buttonClick);
        Button buttonChoice2 = findViewById(R.id.test_choice2_button);
        buttonChoice2.setOnClickListener(buttonClick);
        Button buttonChoice3 = findViewById(R.id.test_choice3_button);
        buttonChoice3.setOnClickListener(buttonClick);

        // When choices are 2, don't set third choice.
        if (card.hasTwoChoice()) {
            buttonChoice3.setVisibility(View.GONE);
        }

        setState(State.Answering);
    }

    private int getImageViewId(View view) {
        Log.d(TAG, "getImageViewId");

        int buttonId = view.getId();

        if (buttonId == R.id.test_choice1_button) {
            return R.id.test_choice1_image;
        } else if (buttonId == R.id.test_choice2_button) {
            return R.id.test_choice2_image;
        } else if (buttonId == R.id.test_choice3_button) {
            return R.id.test_choice3_image;
        } else {
            assert false;
            return -1;
        }
    }

    public ImageView getFriendImageView() {
        return friendImageView;
    }

    public void setFriendImageView(ImageView friendImageView) {
        this.friendImageView = friendImageView;
    }

    public int getFriendImageViewId(int answerIndex) {
        Log.d(TAG, "getFriendImageViewId");

        if (answerIndex == 0) {
            return R.id.test_choice1_friend_image;
        } else if (answerIndex == 1) {
            return R.id.test_choice2_friend_image;
        } else if (answerIndex == 2) {
            return R.id.test_choice3_friend_image;
        } else {
            assert false;
            return -1;
        }
    }

    @Override
    protected boolean isRemainData() {
        // If There is card which has choices, return true.
        for (int index = cardIndex; index < cardData.size(); index++){
            if(!cardData.get(index).hasNoChoice()){
                cardIndex = index;
                return true;
            }
        }

        return false;
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
            Card card;
            switch (msg.what) {
                case CommonThread.MSG_STARTED:
                    activity.setState(State.Connected);
                    break;
                case CommonThread.MSG_RECEIVED:
                    CommunicationData data = (CommunicationData) msg.obj;
                    switch (data.getDataType()) {
                        case CommunicationData.CARD:
                            // Receive card from sender.
                            activity.setCard(data.getCard());
                            activity.setState(State.CardReceived);
                            break;
                        case CommunicationData.CHOICE_ORDER:
                            if (activity.state != State.CardReceived) assert false;

                            // Receive choice order from sender.
                            card = activity.getCard();
                            card.setChoiceOrder(data.getContents());
                            activity.setState(State.CanDisplayCard);
                            activity.displayCard(card);
                            break;
                        case CommunicationData.ANSWER:
                            int answerIndex = data.getContents();

                            // Get friend's image view.
                            int friendImageViewId = activity.getFriendImageViewId(answerIndex);
                            ImageView friendImageView = activity.findViewById(friendImageViewId);
                            friendImageView.setVisibility(View.INVISIBLE);

                            // Set image to the friend's image view.
                            card = activity.getCard();
                            if (card.getChoiceList().get(answerIndex).equals(card.getBackTrue())) {
                                friendImageView.setImageResource(R.drawable.correct);
                            } else {
                                friendImageView.setImageResource(R.drawable.incorrect);
                            }
                            activity.setFriendImageView(friendImageView);

                            activity.setState(State.FriendAnswered);
                    }
                    break;
                case CommonThread.MSG_FINISHED:
//                    Toast.makeText(activity, R.string.toast_connection_closed, Toast.LENGTH_SHORT).show();
                    activity.setState(State.Disconnected);
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
        if ((this.state == State.YouAnswered && state == State.FriendAnswered) || (this.state == State.FriendAnswered && state == State.YouAnswered)) {
            this.state = State.BothWaiting;
            // When state become 'BothWaiting', display friend's answer.
            friendImageView.setVisibility(View.VISIBLE);
            // Make next button and finish button visible.
            visibleNextAndFinishButton();
        }
        this.state = state;
    }

    @Override
    protected void finishTesting() {
        Log.d(TAG, "finishTesting");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (thread != null) {
            thread.close();
        }
    }
}

