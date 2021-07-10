package jp.ac.titech.itpro.sdl.frashcard.test;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

import jp.ac.titech.itpro.sdl.frashcard.card.Card;
import jp.ac.titech.itpro.sdl.frashcard.R;
import jp.ac.titech.itpro.sdl.frashcard.databinding.TestContentsBackChoiceBinding;


public class TestBackChoiceActivity extends TestActivity {
    private final static String TAG = TestBackChoiceActivity.class.getSimpleName();

    private TestContentsBackChoiceBinding binding;
    private ImageView imageView = null;

    @Override
    protected void initTesting() {
        Log.d(TAG, "initTesting");
        binding = (TestContentsBackChoiceBinding) setContent(R.layout.test_contents_back_choice);
    }

    @Override
    protected Card getNextCard() {
        // Get next card from card data file.
        Card card = super.getNextCard();
        while (card.hasNoChoice()) {
            // Skip while the card has choice.
            card = super.getNextCard();
        }

        return card;
    }

    @Override
    protected void displayCard() {
        Log.d(TAG, "displayCard");
        if (imageView != null) {
            imageView.setVisibility(View.INVISIBLE);
        }

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

                // Get "ImageView" to display correct or incorrect.
                int imageViewId = getImageViewId(view);
                imageView = findViewById(imageViewId);
                imageView.setVisibility(View.VISIBLE);

                // Set image to display correct or incorrect..
                if (clickedChoice.equals(card.getBackTrue())) {
                    imageView.setImageResource(R.drawable.correct);
                } else {
                    imageView.setImageResource(R.drawable.incorrect);
                }

                // Make next button and finish button visible.
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

    private int getImageViewId(View view) {
        Log.d(TAG, "clickedCorrectChoice");

        int buttonId = view.getId();

        if (buttonId == R.id.test_choice1_button) {
            return R.id.test_choice1_image;
        } else if (buttonId == R.id.test_choice2_button) {
            return R.id.test_choice2_image;
        } else if (buttonId == R.id.test_choice3_button) {
            return R.id.test_choice3_image;
        } else {
            assert false;
            return -1;
        }
    }

    @Override
    protected void finishTesting() {
        Log.d(TAG, "finishTesting");
        finish();
    }
}

