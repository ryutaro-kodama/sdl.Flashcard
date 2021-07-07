package jp.ac.titech.itpro.sdl.frashcard.test;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;

import jp.ac.titech.itpro.sdl.frashcard.Card;
import jp.ac.titech.itpro.sdl.frashcard.R;
import jp.ac.titech.itpro.sdl.frashcard.databinding.TestContentsBackChoiceBinding;

public class TestBackChoiceActivity extends TestActivity {
    private final static String TAG = TestFrontActivity.class.getSimpleName();

    private TestContentsBackChoiceBinding binding;

    @Override
    protected void initTesting() {
        Log.d(TAG, "initTesting");

        binding = (TestContentsBackChoiceBinding) setContent(R.layout.test_contents_back_choice);
    }

    @Override
    protected void displayCard() {
        Log.d(TAG, "displayCard");

        // Set card data to layout by using "data binding".
        Card card = getNextCard();
        while (card.hasNoChoice()) {
            // Skip while the card has choice.
            card = getNextCard();
        }
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
        Card finalCard = card;
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clickedChoice = ((Button) view).getText().toString();

                if (clickedChoice.equals(finalCard.getBack_true())) {
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

    @Override
    protected void finishTesting() {
        Log.d(TAG, "finish!!!");
    }
}

