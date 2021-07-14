package jp.ac.titech.itpro.sdl.flashcard;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.flashcard.card.Card;
import jp.ac.titech.itpro.sdl.flashcard.card.CardDataFile;

public class CreateActivity extends AppCompatActivity {
    private final static String TAG = CreateActivity.class.getSimpleName();

    private ArrayList<Card> tmpCardData;

    private EditText frontInput, backInputTrue, backInputFalse1, backInputFalse2;
    private final String OCR_PACKAGE = "io.github.subhamtyagi.ocr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_create);

        // Register text.
        final TextView createFrontTextView = (TextView) findViewById(R.id.create_front_text);
        createFrontTextView.setText(R.string.create_front_text);
        final TextView createBackTextView = (TextView) findViewById(R.id.create_back_text);
        createBackTextView.setText(R.string.create_back_text);

        // An array for temporally saving of card data.
        tmpCardData = new ArrayList<Card>();

        // Register input field.
        frontInput = findViewById(R.id.create_front_input);
        backInputTrue = findViewById(R.id.create_back_input_true);
        backInputFalse1 = findViewById(R.id.create_back_input_false1);
        backInputFalse2 = findViewById(R.id.create_back_input_false2);

        // Register button and call event function.
        Button saveButton = (Button)findViewById(R.id.create_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewCard(v);
            }
        });


        Button ocrButton = findViewById(R.id.create_ocr_button);
        ocrButton.setEnabled(false);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setAction("android.intent.category.LAUNCHER");
        intent.setClassName(OCR_PACKAGE, OCR_PACKAGE + ".MainActivity");
        List<ResolveInfo> ris = getPackageManager().queryIntentActivities(intent, 0);
        if (ris != null && ris.size() > 0) {
            // If device has target icr app, make ocr button enable.
            ocrButton.setEnabled(true);
        }
        ocrButton.setOnClickListener(v -> {
            try {
                // Start ocr app.
                startActivity(intent);
            } catch (ActivityNotFoundException e){

            }
        });
    }

    private void saveNewCard(View v) {
        Log.d(TAG, "saveNewCard");

        String frontInputText = frontInput.getText().toString().trim();
        String backInputTrueText = backInputTrue.getText().toString().trim();

        // If "front" or "back true" is empty, not add.
        if (frontInputText.isEmpty() || backInputTrueText.isEmpty()) return;

        String backInputFalse1Text = backInputFalse1.getText().toString().trim();
        String backInputFalse2Text = backInputFalse2.getText().toString().trim();

        // Add data to temporally array.
        Card card = new Card(
                frontInputText, backInputTrueText, backInputFalse1Text, backInputFalse2Text
        );
        tmpCardData.add(card);
        Log.d(TAG, "saveNewCard - add new data: " + card.toString());

        // Clear input data.
        frontInput.getEditableText().clear();
        backInputTrue.getEditableText().clear();
        backInputFalse1.getEditableText().clear();
        backInputFalse2.getEditableText().clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

        // Load card data.
        CardDataFile cardDataFile = new CardDataFile(getApplicationContext());

        // Save new data to json file.
        cardDataFile.addAllTmpData(tmpCardData);
        cardDataFile.save();
        Log.d(TAG, cardDataFile.toString());

    }
}