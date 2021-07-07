package jp.ac.titech.itpro.sdl.frashcard.test;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jp.ac.titech.itpro.sdl.frashcard.R;
import jp.ac.titech.itpro.sdl.frashcard.databinding.TestContentsFrontBindingImpl;


public class TestFrontActivity extends TestActivity {
    private final static String TAG = TestFrontActivity.class.getSimpleName();

    private TestContentsFrontBindingImpl binding;

    @Override
    protected void initTesting() {
        Log.d(TAG, "initTesting");

        binding = (TestContentsFrontBindingImpl) setContent(R.layout.test_contents_front);
    }

    @Override
    protected void displayCard() {
        Log.d(TAG, "displayCard");

        // Set card data to layout by using "data binding".
        binding.setCard(getNextCard());

        TextView textAnswer = findViewById(R.id.test_answer_text);
        Button buttonNext = findViewById(R.id.test_next_button);
        Button buttonFinish = findViewById(R.id.test_finish_button);

        // Answer text, next button and finish button are invisible at first.
        setInvisible(textAnswer, buttonNext, buttonFinish);

        buttonNext.setOnClickListener(v -> {
            displayCard();
        });

        buttonFinish.setOnClickListener(v -> {
            finishTesting();
        });

        Button buttonAnswer = findViewById(R.id.test_answer_button);
        buttonAnswer.setOnClickListener(v -> {
            // If "Answer" button clicked, make answer text and finish button visible.
            textAnswer.setVisibility(View.VISIBLE);
            buttonFinish.setVisibility(View.VISIBLE);

            // If there are remaining data, make next button visible.
            if (cardData.size() > cardIndex) {
                buttonNext.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void finishTesting() {
        Log.d(TAG, "finish!!!");
    }
}
