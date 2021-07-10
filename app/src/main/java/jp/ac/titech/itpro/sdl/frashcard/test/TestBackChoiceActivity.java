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

//    @Override
//    protected Card getNextCard() {
//        // Get next card from card data file.
//        Card card = super.getNextCard();
//        while (card.hasNoChoice()) {
//            // Skip while the card has choice.
//            card = super.getNextCard();
//        }
//
//        return card;
//    }

    @Override
    protected void displayCard(Card card) {
        Log.d(TAG, "displayCard");
        if (imageView != null) {
            imageView.setVisibility(View.INVISIBLE);
        }

        // Set card data to layout by using "data binding".
        binding.setCard(card);

        // Next button and finish button are invisible at first.
        setNextAndFinishButton();

        // Shuffle choices.
        ArrayList<String> choiceList = card.getChoiceList();
        binding.setChoices(choiceList.toArray(new String[choiceList.size()]));

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
        Button buttonChoice3 = findViewById(R.id.test_choice3_button);
        buttonChoice3.setOnClickListener(buttonClick);

        // When choices are 2, don't set third choice.
        if (card.hasTwoChoice()) {
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
    protected boolean isRemainData() {
        // If There is card which has choices, return true.
        for (int index = cardIndex; index < cardData.size(); index++){
            if(!cardData.get(index).hasNoChoice()){
                cardIndex = index;
                return true;
            }
        }

        return false;
    }

    @Override
    protected void finishTesting() {
        Log.d(TAG, "finishTesting");
        finish();
    }
}

