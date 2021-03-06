package com.example.user.sensor;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class SensorDataActivity extends AppCompatActivity implements SensorEventListener {

    public static final String SENSOR_INDEX = "SensorIndex";
    int sensorIndex;
    String sensorName;

    SensorManager manager = null;
    List<Sensor> sensorList = null;

    TextView txtSensorName = null;
    TextView txtSensorAccuracy = null;
    TextView txtSensorValues = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_data);

        txtSensorName = (TextView)findViewById(R.id.txtSensorName);
        txtSensorAccuracy = (TextView)findViewById(R.id.txtSensorAccuracy);
        txtSensorValues = (TextView)findViewById(R.id.txtSensorValues);

        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorList = manager.getSensorList(Sensor.TYPE_ALL);

        Intent passedIntent = getIntent();

        if(passedIntent != null){
            sensorIndex = getIntent().getIntExtra(SENSOR_INDEX, 0);
            sensorName = sensorList.get(sensorIndex).getName();
            txtSensorName.setText(sensorName);
        }
    }

    //센서의 데이터 값이 변할 때 화면에 표시
    public void onSensorChanged(SensorEvent event){
        String data = "Sensor Timestamp: "+event.timestamp+"\n\n"; //timestamp는 센서에서 값을 학인한 시간

        for(int index = 0; index < event.values.length; ++index){   // value는 float타입의 배열로, 센서의 종류에 따라 여러개의 값을 가진다.
            // ex)방향 센서의 경우 value배열의 크기 3
            data += ("Sensor Value #" + (index +1) +": "+event.values[index]+"\n");
        }
        txtSensorValues.setText(data);
    }

    @Override
    protected void onStop() {
        // 액티비티 중지 전에 센서 이벤트 리스너 해제
        manager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        manager.registerListener(this, sensorList.get(sensorIndex), SensorManager.SENSOR_DELAY_UI);
    }

    // 센서정확도가 변할 때 화면에 표시
    public void onAccuracyChanged(Sensor sensor, int accuracy){
        txtSensorAccuracy.setText("Sensor Accuracy : "+getSensorAccuracyAsString(accuracy));
    }

    private String getSensorAccuracyAsString(int accuracy){
        String accuracyString="";

        switch (accuracy){
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                accuracyString="High";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                accuracyString="Low";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                accuracyString="Medium";
                break;
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                accuracyString="Unreliable";
                break;
            default:
                accuracyString="Unknown";
                break;
        }
        return accuracyString;
    }
}
