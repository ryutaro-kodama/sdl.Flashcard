package jp.ac.titech.itpro.sdl.frashcard.test;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;

import jp.ac.titech.itpro.sdl.frashcard.card.Card;
import jp.ac.titech.itpro.sdl.frashcard.CardDataFile;
import jp.ac.titech.itpro.sdl.frashcard.R;

public abstract class TestActivity extends AppCompatActivity {
    private final static String TAG = TestActivity.class.getSimpleName();

    protected Button buttonNext;
    protected Button buttonFinish;

    protected ArrayList<Card> cardData;
    protected int cardIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_test);

        initTesting();

        loadCard();

        displayFirstCard();
    }

    protected void displayFirstCard() {
        if (isRemainData()) {
            // Display first Card
            displayCard();
        } else {
            finishTesting();
        }
    }

    protected void loadCard() {
        // Load card data.
        CardDataFile cardDataFile = new CardDataFile(getApplicationContext());
        cardData = cardDataFile.getCardData();
    }

    protected abstract void initTesting();

    // Change contents based on test mode and return the layout's data binding object.
    protected ViewDataBinding setContent(int newLayoutId) {
        Log.d(TAG, "setContent");

        // Get old contents.
        LinearLayout oldLayout = findViewById(R.id.test_contents_card);

        // Remove old contents.
        oldLayout.removeAllViews();

        // Change old contents to new contents and get binding.
        ViewDataBinding binding = DataBindingUtil.inflate(getLayoutInflater(), newLayoutId, oldLayout, true);

        return binding;
    }

    protected abstract void displayCard();

    protected Card getNextCard() {
        Card card = cardData.get(cardIndex);
        cardIndex++;
        return card;
    }

    protected void setNextAndFinishButton() {
        buttonNext = findViewById(R.id.test_next_button);
        buttonNext.setVisibility(View.INVISIBLE);
        buttonNext.setOnClickListener(v -> {
            onClickNextButton();
        });

        buttonFinish = findViewById(R.id.test_finish_button);
        buttonFinish.setVisibility(View.INVISIBLE);
        buttonFinish.setOnClickListener(v -> {
            onClickFinishButton();
        });
    }

    protected void onClickNextButton() {
        displayCard();
    }

    protected void onClickFinishButton() {
        finishTesting();
    }

    // Make the next and finish button visible.
    protected void visibleNextAndFinishButton() {
        buttonFinish.setVisibility(View.VISIBLE);

        // If there are remaining data, make next button visible.
        if (isRemainData()) {
            buttonNext.setVisibility(View.VISIBLE);
        }
    }

    protected boolean isRemainData() {
        return cardData.size() > cardIndex;
    }

    protected abstract void finishTesting();

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
