package jp.ac.titech.itpro.sdl.frashcard.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;

import jp.ac.titech.itpro.sdl.frashcard.Card;
import jp.ac.titech.itpro.sdl.frashcard.CardDataFile;
import jp.ac.titech.itpro.sdl.frashcard.CreateActivity;
import jp.ac.titech.itpro.sdl.frashcard.MainActivity;
import jp.ac.titech.itpro.sdl.frashcard.R;
import jp.ac.titech.itpro.sdl.frashcard.databinding.ActivityTestBinding;
import jp.ac.titech.itpro.sdl.frashcard.databinding.TestContentsFrontBindingImpl;

public class TestFrontActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private TestContentsFrontBindingImpl binding;

    private ArrayList<Card> cardData;
    private int cardIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_test);

        // Load card data.
        CardDataFile cardDataFile = new CardDataFile(getApplicationContext());
        cardData = cardDataFile.getCardData();

        initTesting();
    }

    private void initTesting() {
        // Change contents based on  test mode.
        LinearLayout layout = findViewById(R.id.test_contents_card);  // Get old contents.
        layout.removeAllViews();  // Remove old contents.
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.test_contents_front, layout, true);
            // Change old contents to new contents and get binding.

        displayCard();
    }

    private void displayCard() {
        if (cardData.size() > cardIndex) {
            // Set card data to layout by using "data binding".
            Card card = cardData.get(cardIndex);
            binding.setCard(card);
            cardIndex++;

            // Answer text, next button and finish button are invisible at first.
            TextView answerText = findViewById(R.id.test_front_front_text);
            answerText.setVisibility(View.INVISIBLE);

            Button buttonNext = findViewById(R.id.test_next_button);
            buttonNext.setVisibility(View.INVISIBLE);
            buttonNext.setOnClickListener(v -> {
                displayCard();
            });

            Button buttonFinish = findViewById(R.id.test_finish_button);
            buttonFinish.setVisibility(View.INVISIBLE);
            buttonFinish.setOnClickListener(v -> {
                finishTesting();
            });

            Button buttonAnswer = findViewById(R.id.test_answer_button);
            buttonAnswer.setOnClickListener(v -> {
                // If "Answer" button clicked, make answer text and finish button visible.
                answerText.setVisibility(View.VISIBLE);
                buttonFinish.setVisibility(View.VISIBLE);

                // If there are remaining data, make next button visible.
                if (cardData.size() > cardIndex) {
                    buttonNext.setVisibility(View.VISIBLE);
                }
            });
        } else {
            finishTesting();
        }
    }

    private void finishTesting() {
        Log.d(TAG, "finish!!!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
