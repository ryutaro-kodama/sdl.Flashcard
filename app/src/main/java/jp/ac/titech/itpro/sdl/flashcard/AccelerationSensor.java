package jp.ac.titech.itpro.sdl.flashcard;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;

import static android.content.Context.SENSOR_SERVICE;

public class AccelerationSensor {
    private final static String TAG = AccelerationSensor.class.getSimpleName();

    private SensorManager manager;
    private Sensor sensor;

    private int delay = SensorManager.SENSOR_DELAY_NORMAL;
    private int type = Sensor.TYPE_LINEAR_ACCELERATION;

    public AccelerationSensor(AppCompatActivity activity) {
        manager = (SensorManager) activity.getSystemService(SENSOR_SERVICE);
        if (manager == null) {
            Toast.makeText(activity, R.string.toast_no_sensor_manager, Toast.LENGTH_LONG).show();
            activity.finish();
            return;
        }

        sensor = manager.getDefaultSensor(type);
        if (sensor == null) {
            String text = activity.getString(R.string.toast_no_sensor_available, sensorTypeName(type));
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }

    public void registerSensorListener(AppCompatActivity activity) {
        Log.d(TAG, "registerSensorListener");
        manager.registerListener((SensorEventListener) activity, sensor, delay);
    }

    // Calculation size of acceleration.
    public double calcAcceleration(SensorEvent event) {
        Log.d(TAG, "calcAcceleration");
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        double result = Math.sqrt(x*x + y*y + z*z);
//        Log.i(TAG, "acceleration=" + result);

        return result;
    }

    public void unregisterSensorListener(AppCompatActivity activity) {
        Log.d(TAG, "unregisterSensorListener");
        manager.unregisterListener((SensorEventListener) activity);
    }

    private String sensorTypeName(int sensorType) {
        try {
            Class<Sensor> klass = Sensor.class;
            for (Field field : klass.getFields()) {
                String fieldName = field.getName();
                if (fieldName.startsWith("TYPE_") && field.getInt(klass) == sensorType)
                    return fieldName;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
