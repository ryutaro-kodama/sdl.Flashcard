package jp.ac.titech.itpro.sdl.frashcard;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import jp.ac.titech.itpro.sdl.frashcard.test.TestMainActivity;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
//    static final String CARD_DATA_ARG = "card_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        // Set toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set create button and register intent.
        Button buttonNewCard = findViewById(R.id.main_create_new_card);
        buttonNewCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateActivity.class);
//            intent.putExtra(MainActivity.CARD_DATA_ARG, cardData.toString());  // Add an argument, 'CardData' object.
            // 引数情報を追加
            startActivity(intent);
            // 特に結果を得ることは想定していない
        });

        // Set test button and register intent.
        Button buttonTestCard = findViewById(R.id.main_test_card);
        buttonTestCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TestMainActivity.class);
//            intent.putExtra(MainActivity.CARD_DATA_ARG, cardData.toString());  // Add an argument, 'CardData' object.
            // 引数情報を追加
            startActivity(intent);
            // 特に結果を得ることは想定していない
        });

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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