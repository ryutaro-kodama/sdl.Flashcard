package jp.ac.titech.itpro.sdl.frashcard.test;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import jp.ac.titech.itpro.sdl.frashcard.Card;
import jp.ac.titech.itpro.sdl.frashcard.R;
import jp.ac.titech.itpro.sdl.frashcard.databinding.TestContentsBackBindingImpl;


public class TestBackActivity extends TestActivity {
    private final static String TAG = TestFrontActivity.class.getSimpleName();

    private TestContentsBackBindingImpl binding;

    @Override
    protected void initTesting() {
        Log.d(TAG, "initTesting");

        binding = (TestContentsBackBindingImpl) setContent(R.layout.test_contents_back);

        displayCard();
    }

    @Override
    protected void displayCard() {
        Log.d(TAG, "displayCard");

        if (cardData.size() > cardIndex) {
            // Set card data to layout by using "data binding".
            Card card = cardData.get(cardIndex);
            binding.setCard(card);
            cardIndex++;

            // Answer text, next button and finish button are invisible at first.
            TextView answerText = findViewById(R.id.test_back_back_text);
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

    @Override
    protected void finishTesting() {
        Log.d(TAG, "finish!!!");
    }
}
