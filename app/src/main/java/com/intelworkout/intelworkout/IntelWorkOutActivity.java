package com.intelworkout.intelworkout;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

/**
 * Created by xavier on 09/01/15.
 */
public class IntelWorkOutActivity extends Activity {
    IntelWorkOutView workout;
    MatricesDataSource database = null;
    Intent intentDataPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //INTENT pour lancer la class ActivityDatasPlayer
        intentDataPlayer = new Intent(this,ActivityDatasPlayer.class);

        //DATABASE
        database = new MatricesDataSource(this);

        //Workout
        workout=(IntelWorkOutView)findViewById(R.id.IntelWorkOutView);
        workout.parentActivity=this;
        workout.setIntentDataPlayer(intentDataPlayer);
        workout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause(){
        super.onPause();
        workout.saveGame();
        workout.setThread(false);
        Log.i("Test", "bouton menu");
    }

    @Override
    public void onResume(){
        super.onResume();
        workout.initparameters();
        workout.setThread(true);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            workout.saveGame();
            this.finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
