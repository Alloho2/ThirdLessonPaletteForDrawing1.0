package com.msaggik.thirdlessonpalettefordrawing10;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    // поля
    private ImageView buttonMenu;
    private LinearLayout buttons;
    private boolean buttonsCheck = false; // поле включения кнопок
    private ImageView buttonPalette, buttonClear;
    private ArtView art;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // привязка кнопок к разметке
        buttonMenu = findViewById(R.id.buttonMenu);
        buttons = findViewById(R.id.buttons);
        buttonPalette = findViewById(R.id.buttonPalette);
        buttonClear = findViewById(R.id.buttonClear);
        art = findViewById(R.id.art);

        // обработка нажатия кнопок
        buttonMenu.setOnClickListener(listener);
        buttonPalette.setOnClickListener(listener);
        buttonClear.setOnClickListener(listener);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Если ускорение по любой оси больше 10 m/s^2, вызовите метод actionTriggered
        if (Math.abs(x) > 10 || Math.abs(y) > 10 || Math.abs(z) > 10) {
            actionTriggered();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
};

     private void actionTriggered() {
         if (buttonsCheck) {
             buttonsCheck = false;
             buttons.setVisibility(View.INVISIBLE);
         } else {
             buttonsCheck = true;
             buttons.setVisibility(View.VISIBLE);
         }

     }

        // слушатель для кнопок
        private View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.buttonMenu:
                        if (buttonsCheck) {
                            buttonsCheck = false;
                            buttons.setVisibility(View.INVISIBLE);
                        } else {
                            buttonsCheck = true;
                            buttons.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.buttonClear:
                        // код для очистки View
                        AlertDialog.Builder broomDialog = new AlertDialog.Builder(MainActivity.this); // создание диалогового окна типа AlertDialog
                        broomDialog.setTitle("Очистка рисунка"); // заголовок диалогового окна
                        broomDialog.setMessage("Очистить область рисования (имеющийся рисунок будет удалён)?"); // сообщение диалога

                        broomDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() { // пункт выбора "да"
                            public void onClick(DialogInterface dialog, int which) {
                                art.clear(); // метод очистки кастомизированного View
                                dialog.dismiss(); // закрыть диалог
                            }
                        });
                        broomDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {  // пункт выбора "нет"
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel(); // выход из диалога
                            }
                        });
                        broomDialog.show(); // отображение на экране данного диалога
                        break;
                }
            }
        };
    }
