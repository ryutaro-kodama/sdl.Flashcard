package jp.ac.titech.itpro.sdl.frashcard;

import android.os.Bundle;
import android.util.Log;
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

//        TextView title = findViewById(R.id.create_title);
//        title.setText(TAG);
//
//        String name = getIntent().getStringExtra(NAME_EXTRA);
//        // 引数情報を取り出し
//        TextView answer = findViewById(R.id.answer_answer);
//        answer.setText(getString(R.string.answer_format, name));
    }
}