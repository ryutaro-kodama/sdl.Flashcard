package jp.ac.titech.itpro.sdl.frashcard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CreateActivity extends AppCompatActivity {
    private final static String TAG = CreateActivity.class.getSimpleName();

    public final static String NAME_EXTRA = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_create);

        final TextView createFrontTextView = (TextView) findViewById(R.id.create_front_text);
        createFrontTextView.setText(R.string.create_front_text);
        final TextView createBackTextView = (TextView) findViewById(R.id.create_back_text);
        createBackTextView.setText(R.string.create_back_text);

        Button saveButton = (Button)findViewById(R.id.create_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewCard(v);
            }
        });
    }

    private void saveNewCard(View v) {
        Log.d(TAG, "saveNewCard");
    }
}