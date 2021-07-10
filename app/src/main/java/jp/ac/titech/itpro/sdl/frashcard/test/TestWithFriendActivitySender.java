package jp.ac.titech.itpro.sdl.frashcard.test;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

import jp.ac.titech.itpro.sdl.frashcard.card.Card;
import jp.ac.titech.itpro.sdl.frashcard.R;
import jp.ac.titech.itpro.sdl.frashcard.card.CardReader;
import jp.ac.titech.itpro.sdl.frashcard.card.CardWriter;
import jp.ac.titech.itpro.sdl.frashcard.databinding.TestContentsBackChoiceBinding;
import jp.ac.titech.itpro.sdl.frashcard.test.connection.BluetoothSocketSingleton;
import jp.ac.titech.itpro.sdl.frashcard.test.connection.ScanActivity;
import jp.ac.titech.itpro.sdl.frashcard.thread.CommonThread;

public class TestWithFriendActivitySender extends TestWithFriendActivity {
    private final static String TAG = TestWithFriendActivitySender.class.getSimpleName();

    @Override
    protected Card getNextCard() {
        // Get next card from card data file.
        Card card = super.getNextCard();
        while (card.hasNoChoice()) {
            // Skip while the card has choice.
            card = super.getNextCard();
        }

        // Send card to receiver.
        thread.send(communicationDataFactory.make(card));
        return card;
    }

    @Override
    protected void displayCard() {
        Log.d(TAG, "displayCard");

        // Set card data to layout by using "data binding".
        Card card = getNextCard();
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
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clickedChoice = ((Button) view).getText().toString();

                if (clickedChoice.equals(card.getBackTrue())) {
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

    // Send your answer to your friend.
    private void sendYourAnswer(int answer) {
        thread.send(communicationDataFactory.makeAnswer(answer));
    }

    @Override
    protected void finishTesting() {
        Log.d(TAG, "finishTesting");
        finish();
    }
}

