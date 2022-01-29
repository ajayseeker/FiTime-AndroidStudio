package com.example.learningas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbarlayout);
    }

    public void StartSession(View view) {
        try{
            int setsCount = getEntry((EditText)findViewById(R.id.sets));
            int exerciseCount = getEntry((EditText)findViewById(R.id.exercises));
            int workTime = getEntry((EditText)findViewById(R.id.work));
            int restTime = getEntry((EditText)findViewById(R.id.rest));
            Intent intent = new Intent(MainActivity.this, SessionPage.class);
            Bundle bundle = new Bundle();
            bundle.putInt("sets", setsCount);
            bundle.putInt("exercises", exerciseCount);
            bundle.putInt("work", workTime);
            bundle.putInt("rest", restTime);

            intent.putExtras(bundle);
            startActivity(intent);
        }
        catch (Exception ex)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please enter valid values")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getApplicationContext(),"let's try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Alert!");
            alert.show();  ;

        }

    }

    private int getEntry(EditText view)
    {
        return Integer.parseInt(view.getText().toString());
    }
}