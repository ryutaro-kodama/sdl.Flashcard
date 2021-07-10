package jp.ac.titech.itpro.sdl.frashcard.test;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jp.ac.titech.itpro.sdl.frashcard.R;
import jp.ac.titech.itpro.sdl.frashcard.card.Card;
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
    protected void displayCard(Card card) {
        Log.d(TAG, "displayCard");

        // Set card data to layout by using "data binding".
        binding.setCard(card);

        // Next button and finish button are invisible at first.
        setNextAndFinishButton();

        // Answer text is invisible at first.
        TextView textAnswer = findViewById(R.id.test_answer_text);
        textAnswer.setVisibility(View.INVISIBLE);

        Button buttonAnswer = findViewById(R.id.test_answer_button);
        buttonAnswer.setOnClickListener(v -> {
            // If "Answer" button clicked, make next button, finish button and answer text visible.
            visibleNextAndFinishButton();
            textAnswer.setVisibility(View.VISIBLE);
        });
    }

    @Override
    protected void finishTesting() {
        Log.d(TAG, "finishTesting");
        finish();
    }
}
