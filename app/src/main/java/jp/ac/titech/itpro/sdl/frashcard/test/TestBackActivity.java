package jp.ac.titech.itpro.sdl.frashcard.test;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jp.ac.titech.itpro.sdl.frashcard.R;
import jp.ac.titech.itpro.sdl.frashcard.databinding.TestContentsBackBindingImpl;


public class TestBackActivity extends TestActivity {
    private final static String TAG = TestBackActivity.class.getSimpleName();

    private TestContentsBackBindingImpl binding;

    @Override
    protected void initTesting() {
        Log.d(TAG, "initTesting");
        binding = (TestContentsBackBindingImpl) setContent(R.layout.test_contents_back);
    }

    @Override
    protected void displayCard() {
        Log.d(TAG, "displayCard");

        // Set card data to layout by using "data binding".
        binding.setCard(getNextCard());

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
