package jp.ac.titech.itpro.sdl.flashcard.test;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jp.ac.titech.itpro.sdl.flashcard.AccelerationSensor;
import jp.ac.titech.itpro.sdl.flashcard.R;
import jp.ac.titech.itpro.sdl.flashcard.card.Card;
import jp.ac.titech.itpro.sdl.flashcard.databinding.TestContentsFrontBinding;

public class TestFrontActivity extends TestActivity implements SensorEventListener {
    private final static String TAG = TestFrontActivity.class.getSimpleName();

    private TestContentsFrontBinding binding;
    private Button buttonAnswer;

    private AccelerationSensor accelerationSensor;
    private final double SENSOR_THRESHOLD = 10.0f;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        accelerationSensor.registerSensorListener(this);
    }

    @Override
    protected void initTesting() {
        Log.d(TAG, "initTesting");
        binding = (TestContentsFrontBinding) setContent(R.layout.test_contents_front);

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

        Button buttonAnswer = findViewById(R.id.test_answer_button);
        buttonAnswer.setOnClickListener(v -> {
            // If "Answer" button clicked, make next button, finish button and answer text visible.
            visibleNextAndFinishButton();
            textAnswer.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "onSensorChanged");

        // If acceleration of phone moving is more than threshold, make answer visible.
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
        Log.d(TAG, "onPause");
        accelerationSensor.unregisterSensorListener(this);
    }
}
