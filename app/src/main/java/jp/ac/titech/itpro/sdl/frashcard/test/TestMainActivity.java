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
    private final static String TAG = TestMainActivity.class.getSimpleName();

//    public final static String TEST_TYPE_ARG = "test_type";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_test_main);

        // Set create button and register intent.
        Button buttonTestFront = findViewById(R.id.test_front);
        buttonTestFront.setOnClickListener(v -> {
            Intent intent = new Intent(TestMainActivity.this, TestFrontActivity.class);
            startActivity(intent);
        });

        Button buttonTestBack = findViewById(R.id.test_back);
        buttonTestBack.setOnClickListener(v -> {
            Intent intent = new Intent(TestMainActivity.this, TestBackActivity.class);
            startActivity(intent);
        });

        Button buttonTestBackChoice = findViewById(R.id.test_back_from_choice);
        buttonTestBackChoice.setOnClickListener(v -> {
            Intent intent = new Intent(TestMainActivity.this, TestBackChoiceActivity.class);
            startActivity(intent);
        });

        Button buttonTestWithFriend = findViewById(R.id.test_with_friend);
        buttonTestWithFriend.setOnClickListener(v -> {
            Intent intent = new Intent(TestMainActivity.this, TestWithFriendHomeActivity.class);
            startActivity(intent);
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
