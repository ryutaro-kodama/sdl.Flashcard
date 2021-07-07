package jp.ac.titech.itpro.sdl.frashcard.test;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import jp.ac.titech.itpro.sdl.frashcard.Card;
import jp.ac.titech.itpro.sdl.frashcard.R;


public class TestFrontActivity extends TestActivity {
    private final static String TAG = TestFrontActivity.class.getSimpleName();

    protected void initTesting() {
        Log.d(TAG, "initTesting");

        // Change contents based on  test mode.
        LinearLayout layout = findViewById(R.id.test_contents_card);  // Get old contents.
        layout.removeAllViews();  // Remove old contents.
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.test_contents_front, layout, true);
            // Change old contents to new contents and get binding.

        displayCard();
    }

    protected void displayCard() {
        Log.d(TAG, "displayCard");

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

    protected void finishTesting() {
        Log.d(TAG, "finish!!!");
    }
}
