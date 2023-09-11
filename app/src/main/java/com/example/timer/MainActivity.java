package com.example.timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTimer; // переменная для доступа к тексту таймера
    private int seconds = 0;
    private boolean isRunning = false;
    private boolean wasRunning = false; //переменная , которая хранит состояние таймера до метода стоп

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTimer = findViewById(R.id.textViewTimer);//присваем занчение

        if (savedInstanceState != null) {//Теперь восстанавливаем наши значения
            seconds = savedInstanceState.getInt("seconds");
            isRunning = savedInstanceState.getBoolean("isRunning");

            wasRunning = savedInstanceState.getBoolean("wasRunning"); // восстанавливаем это значение , шаг четыре к теме onResume
        }

        //метод runTimer начинает работать при создании данной активности, поэтому мы вызываем его в этом методе
        runTimer();
    }

    @Override
    protected void onPause() { //в этом методе реализовываю сохранение состояния таймера, а после его остановки
        //вызывается тогда, когда активность видима, но фокус принадлежит другой активности
        super.onPause();
        wasRunning = isRunning;
        isRunning = false;
    }

    @Override
    protected void onResume() {//аналогичный код методу onStop
        super.onResume();//вызывается непосредственно перед тем, когда активность начинает взаимодействовать с пользователем
        isRunning = wasRunning;
    }

    //что бы сохранить текущее состояние активности метод:
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) { //Bundle позволяет объединить  разные типы данных в один объект, метод работает через сохранение данных (если проще говоря), при уничтожении активности и возвращает их при восстановлении активности
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds); //сохрняем наши значения
        outState.putBoolean("isRunning", isRunning);

        outState.putBoolean("wasRunning", wasRunning); //затем добавляем сюда переменную , шаг три к данной теме
    }

    public void onClickStartTimer(View view) {
        isRunning = true;
    }
    public void inClickPauseTimer(View view) {
        isRunning = false;
    }

    public void onClickResetTimer(View view) {
        isRunning = false;
        seconds = 0;
    }

    private void runTimer() {
        final Handler handler = new Handler(); //создаем объект , его тип Handler
        handler.post(new Runnable() { //у этого объекта вызываем метод post; метод post - как можно быстрее вызови данный код
            @Override
            //сейчас он вызывается один раз
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int second = seconds % 60;

                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, second); //преобразовываем в строки
                textViewTimer.setText(time); // прередаем текст равный времени

                if (isRunning) {
                    seconds++;
                }
                //вызываем его еще раз, но уже с задержкой в секунду? (говорим handler выполни этот метод еще раз )
                handler.postDelayed(this, 1000); //в качесве первого параметра передаем объект Runnable - т.е. себя, втрой объект - задержка в мил.секундах - это 1000
            }
        });
    }
}