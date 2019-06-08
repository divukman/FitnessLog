package com.dimitar.testcustomcomponents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dimitar.testcustomcomponents.widgets.ExerciseWidget;
import com.dimitar.testcustomcomponents.widgets.HistoryWidget;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout exercisesLayout = (LinearLayout) findViewById(R.id.verticalLayout);

        ExerciseWidget exerciseWidget = new ExerciseWidget(getApplicationContext());
        exerciseWidget.setLayoutParams(lp);
        exerciseWidget.setHeader("Pull ups");
        exerciseWidget.addSet("Warmup set", 0, 5);
        exerciseWidget.addSet("Work set", 0, 7);
        exerciseWidget.addSet("Heavy", 0, 10);
        exerciseWidget.addSet("Rest set", 0, 5);
        exerciseWidget.addSet("Final set", 0, 10);
        exerciseWidget.addAllDone();
        exercisesLayout.addView(exerciseWidget);


        HistoryWidget historyWidget = new HistoryWidget(getApplicationContext());
        exercisesLayout.addView(historyWidget);




    }
}
