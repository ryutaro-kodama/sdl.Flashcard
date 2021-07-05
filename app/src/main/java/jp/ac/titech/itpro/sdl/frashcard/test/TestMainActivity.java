package jp.ac.titech.itpro.sdl.frashcard.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import jp.ac.titech.itpro.sdl.frashcard.CreateActivity;
import jp.ac.titech.itpro.sdl.frashcard.MainActivity;
import jp.ac.titech.itpro.sdl.frashcard.R;

public class TestMainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

//    public final static String TEST_TYPE_ARG = "test_type";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_test_main);

        // Set create button and register intent.
        Button buttonNewCard = findViewById(R.id.test_front);
        buttonNewCard.setOnClickListener(v -> {
            Intent intent = new Intent(TestMainActivity.this, TestFrontActivity.class);
//            intent.putExtra(TestMainActivity.TEST_TYPE_ARG, cardData.toString());  // Add an argument, 'CardData' object.
            // 引数情報を追加
            startActivity(intent);
            // 特に結果を得ることは想定していない
        });
    }

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
