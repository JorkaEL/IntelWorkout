package com.intelworkout.intelworkout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class MenuActivity extends Activity {

    public boolean testStateSound = false;
    public MediaPlayer monMedPlayer;
    Intent intentVar;
    final Context context = this;
    private MatricesDataSource database= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        intentVar = new Intent(this, IntelWorkOutActivity.class);

        Button btnQuitter = (Button) findViewById(R.id.buttonQuit);
        Button btnStart = (Button) findViewById(R.id.buttonStart);
        Button btnSound = (Button) findViewById(R.id.buttonSound);
        Button btnScore = (Button) findViewById(R.id.buttonScores);

        btnQuitter.setOnClickListener(new ButtonQuitClickListener());
        btnStart.setOnClickListener(new ButtonStartClickListener(btnStart));
        btnSound.setOnClickListener(new ButtonSoundClickListener(btnSound));
        btnScore.setOnClickListener(new ButtonScoreClickListener(btnScore));

        Log.i("onCreate", "CreationBase de donnees");
        database = new MatricesDataSource(context);

        monMedPlayer = MediaPlayer
                .create(this.getBaseContext(), R.raw.ambiance);
        monMedPlayer.setLooping(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inf = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.help:
                fctHelp();
                return true;
            case R.id.APropos:
                fctAPropos();
                return true;
            default:
                return false;
        }
    }

    class ButtonQuitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    class ButtonScoreClickListener implements View.OnClickListener {
        Button btn;

        ButtonScoreClickListener(Button button) {
            this.btn = button;
        }

        @Override
        public void onClick(View v) {
            Log.i("Affichier score", "Affichier score");
            database.open();
            fctAffScore(database.fctRecupDatas());
            database.close();

        }
    };

    private void fctAffScore(List<MatriceData> lstDatas) {
        Log.i("DataBaseActivity", "fctAffScore");
        String strTxt = "";
        int classement = 0;

        for(MatriceData data : lstDatas){
            classement++;
            if(classement==1){
                strTxt =strTxt+ data.toString()+"<br>";
            }else{
                strTxt =strTxt+ data.toString()+"<br>";
            }
        }
        AlertDialog.Builder dialogMessScore = new AlertDialog.Builder(
                context);

        dialogMessScore.setTitle("Tableau des scores");

        TextView txtViewQts = new TextView(context);

        txtViewQts.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        txtViewQts.setPadding(20, 10, 20, 10);
        txtViewQts.setTextSize(20);
        txtViewQts.setTextColor(Color.WHITE);

        txtViewQts.setMovementMethod(new ScrollingMovementMethod());

        txtViewQts
                .setText(Html
                        .fromHtml("<br>" +
                                "<br>" +
                                "<small>" +
                                strTxt +
                                "</small>"));
        dialogMessScore.setView(txtViewQts);

        dialogMessScore.setPositiveButton("OK",
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dialogMessScore
                .setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
        dialogMessScore.show();

    }


    class ButtonStartClickListener implements View.OnClickListener {
        Button btn;

        ButtonStartClickListener(Button button) {
            this.btn = button;
        }

        @Override
        public void onClick(View v) {
            startActivity(intentVar);
        }
    };

    class ButtonSoundClickListener implements View.OnClickListener {
        Button btn;
        boolean blnSound;

        ButtonSoundClickListener(Button button) {
            this.btn = button;
        }

        @Override
        public void onClick(View v) {
            testStateSound = !testStateSound;

            if (testStateSound == true) {
                btn.setText(R.string.bouttonSonOn);
                monMedPlayer.start();
            } else {
                btn.setText(R.string.bouttonSonOff);
                monMedPlayer.pause();
            }
        }

    };

    private void fctAPropos() {
        AlertDialog.Builder dialogAPropos = new AlertDialog.Builder(
                context);

        dialogAPropos.setTitle(R.string.TitreBtnAPropos);

        dialogAPropos.setIcon(R.drawable.ic_about);

        TextView txtViewQts = new TextView(context);

        txtViewQts.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        txtViewQts.setPadding(20, 10, 20, 10);
        txtViewQts.setTextColor(Color.WHITE);
        txtViewQts.setTextSize(20);
        txtViewQts
                .setText(Html
                        .fromHtml("<center><small>PARIS 8 - Intelligent WorkdOut</small></center>"
                                + "<br>"
                                + "<br>"
                                + "<center><b>Developpe par :</b></center><br>"
                                + "<small>- Xavier BOUTANQUOY</small><br>"
                                + "<small>- Yann MAHUET</small><br>"
                                + "<br>"
                                + "<small>Developpe dans le cadre du M1 Informatique</small><br>"
                                + "<small>Professeur : Ludovic GREMY</small>"));
        dialogAPropos.setView(txtViewQts);

        dialogAPropos.setPositiveButton("OK",
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dialogAPropos
                .setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
        dialogAPropos.show();

    }

    private void fctHelp() {
        AlertDialog.Builder dialogHelp = new AlertDialog.Builder(
                context);

        dialogHelp.setTitle(R.string.TitreBtnHelps);

        dialogHelp.setIcon(R.drawable.ic_help);

        TextView txtViewQts = new TextView(context);

        txtViewQts.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        txtViewQts.setPadding(20, 10, 20, 10);
        txtViewQts.setTextSize(20);
        txtViewQts.setTextColor(Color.WHITE);

        //txtViewQts.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        txtViewQts.setMovementMethod(new ScrollingMovementMethod());

        txtViewQts
                .setText(Html
                        .fromHtml("<b>Objectif :<b><br>" +
                                "<br>" +
                                "<small>Reformer la forme presente en haut à partir de la matrice au centrale</small><br>" +
                                "<br>" +
                                "<b>Rules</b><br>" +
                                "<br>" +
                                "<small>Utilisez la matrice centrale pour deplacer les cases par des mouvements Gauche/Droite ou Haut/Bas</small>"));
        dialogHelp.setView(txtViewQts);

        dialogHelp.setPositiveButton("OK",
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dialogHelp
                .setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
        dialogHelp.show();

    }


}
