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
        setCard(card);
        setState(State.CardReceived);

        // Send card to receiver.
        thread.send(communicationDataFactory.make(card));
        card.setChoiceOrder();
        thread.send(communicationDataFactory.makeChoiceOrder(card.getChoiceOrder()));

        setState(State.CanDisplayCard);
        return card;
    }
}

