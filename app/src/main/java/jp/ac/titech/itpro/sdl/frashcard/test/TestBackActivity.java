package jp.ac.titech.itpro.sdl.frashcard.test;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jp.ac.titech.itpro.sdl.frashcard.AccelerationSensor;
import jp.ac.titech.itpro.sdl.frashcard.R;
import jp.ac.titech.itpro.sdl.frashcard.card.Card;
import jp.ac.titech.itpro.sdl.frashcard.databinding.TestContentsBackBindingImpl;

public class TestBackActivity extends TestActivity implements SensorEventListener {
    private final static String TAG = TestBackActivity.class.getSimpleName();

    private TestContentsBackBindingImpl binding;
    private Button buttonAnswer;

    private AccelerationSensor accelerationSensor;
    private final double SENSOR_THRESHOLD = 10.0f;

    @Override
    protected void onResume() {
        super.onResume();
        accelerationSensor.registerSensorListener(this);
    }

    @Override
    protected void initTesting() {
        Log.d(TAG, "initTesting");
        binding = (TestContentsBackBindingImpl) setContent(R.layout.test_contents_back);

        buttonAnswer = findViewById(R.id.test_answer_button);

        accelerationSensor = new AccelerationSensor(this);
    }

    @Override
    protected void displayCard(Card card) {
        Log.d(TAG, "displayCard");

        // Set card data to layout by using "data binding".
        binding.setCard(card);

        // Next button and finish button are invisible at first.
        setNextAndFinishButton();

        // Answer text is invisible at first.
        TextView textAnswer = findViewById(R.id.test_answer_text);
        textAnswer.setVisibility(View.INVISIBLE);

        buttonAnswer.setOnClickListener(v -> {
            // If "Answer" button clicked, make next button, finish button and answer text visible.
            visibleNextAndFinishButton();
            textAnswer.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "onSensorChanged");
        if (accelerationSensor.calcAcceleration(event) > SENSOR_THRESHOLD) {
            buttonAnswer.performClick();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        return;
    }

    @Override
    protected void onPause() {
        super.onPause();
        accelerationSensor.unregisterSensorListener(this);
    }

    @Override
    protected void finishTesting() {
        Log.d(TAG, "finishTesting");
        finish();
    }
}
