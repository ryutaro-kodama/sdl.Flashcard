package jp.ac.titech.itpro.sdl.frashcard.test.with_friend;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import jp.ac.titech.itpro.sdl.frashcard.card.Card;
import jp.ac.titech.itpro.sdl.frashcard.R;

public class TestWithFriendActivityReceiver extends TestWithFriendActivity {
    private final static String TAG = TestWithFriendActivityReceiver.class.getSimpleName();

    private TextView receiverWait;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initTesting() {
        super.initTesting();
        Log.d(TAG, "initTesting");

        // Get text of notice that wait for friend's next action.
        receiverWait = findViewById(R.id.test_choice_friend_receiver_wait);
        receiverWait.setText(R.string.test_choice_friend_receiver_wait);
    }

    @Override
    protected void loadCard() {
        Log.d(TAG, "loadCard");
        // The receiver doesn't load card data file.
        return;
    }

    @Override
    protected void initDisplaying() {
        super.initDisplaying();
        Log.d(TAG, "initDisplaying");

        // Delete the notice.
        receiverWait.setVisibility(View.GONE);
    }

    @Override
    protected Card getNextCard() {
        // Return received card.
        return getCard();
    }

    protected void visibleNextAndFinishButton() {
        buttonFinish.setVisibility(View.VISIBLE);

        // The receiver can't see next button (do at 'initTesting').
        // findViewById(R.id.test_next_button).setVisibility(View.INVISIBLE);

        // Notice that wait for friend's action.
        receiverWait.setVisibility(View.VISIBLE);
    }

    @Override
    protected boolean isRemainData() {
        return true;
    }

    @Override
    protected void friendFinish() {
        super.friendFinish();

        receiverWait.setVisibility(View.GONE);
    }
}

