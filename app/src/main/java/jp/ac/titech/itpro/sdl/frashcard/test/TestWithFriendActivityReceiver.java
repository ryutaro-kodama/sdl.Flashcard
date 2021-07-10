package jp.ac.titech.itpro.sdl.frashcard.test;

import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;

import jp.ac.titech.itpro.sdl.frashcard.card.Card;
import jp.ac.titech.itpro.sdl.frashcard.R;
import jp.ac.titech.itpro.sdl.frashcard.databinding.TestContentsBackChoiceBinding;
import jp.ac.titech.itpro.sdl.frashcard.test.connection.BluetoothSocketSingleton;

public class TestWithFriendActivityReceiver extends TestWithFriendActivity {
    private final static String TAG = TestWithFriendActivity.class.getSimpleName();

    @Override
    protected void loadCard() {
        // The receiver doesn't load card data file.
        return;
    }

    @Override
    protected Card getNextCard() {
        // Return received card.
        return getCard();
    }

    @Override
    protected boolean isRemainData() {
        return true;
    }

    private void clickedCorrectChoice() {
        Log.d(TAG, "correct!!!");
    }

    private void clickedIncorrectChoice() {
        Log.d(TAG, "incorrect!!!");
    }

    @Override
    protected void finishTesting() {
        Log.d(TAG, "finishTesting");
        finish();
    }
}

