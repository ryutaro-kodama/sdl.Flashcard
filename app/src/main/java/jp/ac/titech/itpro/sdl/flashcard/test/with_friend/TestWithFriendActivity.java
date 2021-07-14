package jp.ac.titech.itpro.sdl.flashcard.test.with_friend;

import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import jp.ac.titech.itpro.sdl.flashcard.R;
import jp.ac.titech.itpro.sdl.flashcard.card.Card;
import jp.ac.titech.itpro.sdl.flashcard.databinding.TestContentsWithFriendBinding;
import jp.ac.titech.itpro.sdl.flashcard.test.TestActivity;
import jp.ac.titech.itpro.sdl.flashcard.test.with_friend.communication.CommunicationData;
import jp.ac.titech.itpro.sdl.flashcard.test.with_friend.communication.CommunicationDataFactory;
import jp.ac.titech.itpro.sdl.flashcard.test.with_friend.connection.BluetoothSocketSingleton;
import jp.ac.titech.itpro.sdl.flashcard.test.with_friend.thread.CommonThread;

// This is the activity which is common between sender and receiver.
public abstract class TestWithFriendActivity extends TestActivity {
    private final static String TAG = TestWithFriendActivity.class.getSimpleName();

    public enum State {
        Connected,
        CardReceived,
        CanDisplayCard,
        Answering,
        YouAnswered,
        FriendAnswered,
        BothAnswered,
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
    private int[] allChoiceButtonIds = new int[]{R.id.test_choice1_button,
                                              R.id.test_choice2_button,
                                              R.id.test_choice3_button};

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

    protected void initDisplaying() {
        // Delete correct or incorrect image.
        if (imageView != null) {
            imageView.setVisibility(View.INVISIBLE);
        }
        if (friendImageView != null) {
            friendImageView.setVisibility(View.INVISIBLE);
        }

        // Make all buttons clickable.
        for(int index = 0; index < allChoiceButtonIds.length; index++){
            Button notClickedButton = findViewById(allChoiceButtonIds[index]);
            notClickedButton.setEnabled(true);
        }
    }

    @Override
    protected void displayCard(Card card) {
        Log.d(TAG, "displayCard");
        if (state != State.CanDisplayCard) return;

        initDisplaying();

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
                displayCorrectOrIncorrectImage(
                        clickedChoice.equals(card.getBackTrue()), imageView);


                int answerIndex = -1;
                for(int index = 0; index < allChoiceButtonIds.length; index++){
                    if (view.getId() == allChoiceButtonIds[index]) {
                        // If clicked button, get answered index.
                        answerIndex = index;
                    } else {
                        // If not, make the button not clickable.
                        Button notClickedButton = findViewById(allChoiceButtonIds[index]);
                        notClickedButton.setEnabled(false);
                    }
                }

                // Send your answer to friend to display your answer.
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

    // Return the "ImageViewId" based on clicked button to display correct or incorrect image.
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

    // Based on "isCorrect", display image.
    public void displayCorrectOrIncorrectImage(boolean isCorrect, ImageView target) {
        if (isCorrect) {
            target.setImageResource(R.drawable.correct);
        } else {
            target.setImageResource(R.drawable.incorrect);
        }
    }

    public void setFriendImageView(ImageView friendImageView) {
        this.friendImageView = friendImageView;
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

    protected void friendFinish() {
        // Set next button not to be pressed, because friend has finished test.
        Button nextButton = findViewById(R.id.test_next_button);
        nextButton.setEnabled(false);
        visibleNextAndFinishButton();

        // Notice that friend has finished test.
        TextView friendFinishText = findViewById(R.id.test_choice_friend_finish_text);
        friendFinishText.setText(R.string.test_choice_friend_finish_text);
        friendFinishText.setVisibility(View.VISIBLE);

        // Delete thread.
        if (thread != null) {
            thread = null;
        }

        setState(State.Disconnected);
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
                        case CommunicationData.CARD:  // Receive card data.
                            // Receive card from sender.
                            activity.setCard(data.getCard());
                            activity.setState(State.CardReceived);
                            break;
                        case CommunicationData.CHOICE_ORDER:  // Receive choice's order.
                            if (activity.state != State.CardReceived) assert false;

                            // Receive choice order from sender.
                            card = activity.getCard();
                            card.setChoiceOrder(data.getContents());
                            activity.setState(State.CanDisplayCard);
                            activity.displayCard(card);
                            break;
                        case CommunicationData.ANSWER:  // Receive friend's answer.
                            int answerIndex = data.getContents();

                            // Get friend's image view.
                            int friendImageViewId = getFriendImageViewId(answerIndex);
                            ImageView friendImageView = activity.findViewById(friendImageViewId);
                            friendImageView.setVisibility(View.INVISIBLE);

                            // Set image to the friend's image view.
                            card = activity.getCard();
                            activity.displayCorrectOrIncorrectImage(
                                    card.getChoiceList().get(answerIndex).equals(card.getBackTrue()),
                                    friendImageView
                            );
                            activity.setFriendImageView(friendImageView);

                            activity.setState(State.FriendAnswered);
                            break;
                        case CommunicationData.FINISH:
                            activity.friendFinish();
                            break;
                    }
                    break;
                case CommonThread.MSG_FINISHED:
                    activity.setState(State.Disconnected);
                    break;
            }
        }

        // Return the "ImageViewId" based on clicked button index to display correct or incorrect image.
        private int getFriendImageViewId(int answerIndex) {
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
    }

    protected void setCard(Card card) {
        this.card = card;
    }

    protected Card getCard() {
        return this.card;
    }

    protected void setState(State state){
        if ((this.state == State.YouAnswered && state == State.FriendAnswered) || (this.state == State.FriendAnswered && state == State.YouAnswered)) {
            this.state = State.BothAnswered;
            // When state become 'BothWaiting', display friend's answer.
            friendImageView.setVisibility(View.VISIBLE);
            // Make next button and finish button visible.
            visibleNextAndFinishButton();
        } else {
            this.state = state;
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (thread != null) {
            thread.send(CommunicationDataFactory.makeFinish());
            thread.close();
            thread = null;
        }
        setState(State.Disconnected);
    }
}

