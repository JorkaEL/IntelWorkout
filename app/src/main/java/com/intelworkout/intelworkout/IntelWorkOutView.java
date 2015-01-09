package com.intelworkout.intelworkout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by xavier on 09/01/15.
 */
public class IntelWorkOutView extends SurfaceView implements View.OnClickListener,
        View.OnTouchListener, SurfaceHolder.Callback, Runnable {

    private Time iniTime = new Time();
    private Time currentTime = new Time();
    private int tempsJeu = 0;
    private int minuteJeu = 0;
    private int oldTempsJeu = 0;
    private boolean first;
    private String temps;

    private Bitmap case_Bleu;
    private Bitmap case_Bleu_claire;
    private Bitmap case_Rouge;
    private Bitmap case_Rouge_claire;
    private Bitmap case_Bleu_Small;
    private Bitmap case_Rouge_Small;

    public Activity parentActivity;

    Intent intentDataPlayer = null;

    private Resources intelWorkRes;
    private Context intelWorkcontext;

    private boolean in = true;
    private Thread cv_thread;
    SurfaceHolder holder;
    private Random rd;

    static final int mvtInitial = 0;
    static final int mvtHorizontal = 1;
    static final int mvtVertical = 2;
    int typeMvt;

    static final int mapWidth = 5;
    static final int mapHeight = 5;

    int mapTileSize = 0;// Initialisation dans le loadImage
    int mapTileSizeMinus = 0;// Initialisation dans le loadImage

    static final int cst_Bleu = 0;
    static final int cst_Rouge = 1;
    static final int cst_Bleu_Claire = 2;
    static final int cst_Rouge_Claire = 3;

    private String matriceJeu = "";

    int mapTopAnchor; // coordonnes en Y du point d'ancrage de notre terrain
    int mapTopAnchorMinus;

    int mapLeftAnchor; // coordonnes en X du point d'ancrage de notre terrain
    int mapLeftAnchorMinus;

    int mapBotAnchor;
    int mapRightAnchor;

    int idReference = 0;
    // Variables
    int colonneXOrigine = -1;
    int ligneYOrigine = -1;
    int colonneXNew = -1;
    int ligneYNew = -1;

    boolean mvtAxeHorizontal = false;
    boolean mvtAxeVertical = false;
    boolean boolVictory = false;

    public int[][] tabRef1 = {
            { cst_Rouge, cst_Bleu, cst_Bleu, cst_Bleu, cst_Rouge },
            { cst_Bleu, cst_Rouge, cst_Bleu, cst_Rouge, cst_Bleu },
            { cst_Bleu, cst_Bleu, cst_Rouge, cst_Bleu, cst_Bleu },
            { cst_Bleu, cst_Rouge, cst_Bleu, cst_Rouge, cst_Bleu },
            { cst_Rouge, cst_Bleu, cst_Bleu, cst_Bleu, cst_Rouge } };

    public int[][] tabRef2 = {
            { cst_Rouge, cst_Bleu, cst_Bleu, cst_Bleu, cst_Rouge },
            { cst_Bleu, cst_Rouge, cst_Bleu, cst_Rouge, cst_Bleu },
            { cst_Bleu, cst_Bleu, cst_Rouge, cst_Bleu, cst_Bleu },
            { cst_Bleu, cst_Bleu, cst_Rouge, cst_Bleu, cst_Bleu },
            { cst_Bleu, cst_Bleu, cst_Rouge, cst_Bleu, cst_Bleu } };

    public int[][] tabRef3 = {
            { cst_Bleu, cst_Bleu, cst_Bleu, cst_Rouge, cst_Bleu },
            { cst_Bleu, cst_Rouge, cst_Bleu, cst_Bleu, cst_Bleu },
            { cst_Bleu, cst_Bleu, cst_Bleu, cst_Rouge, cst_Bleu },
            { cst_Bleu, cst_Rouge, cst_Bleu, cst_Bleu, cst_Bleu },
            { cst_Bleu, cst_Bleu, cst_Bleu, cst_Rouge, cst_Bleu } };

    static final String MATRICE1 = "Modele 1";
    static final String MATRICE2 = "Modele 2";
    static final String MATRICE3 = "Modele 3";

    Matrice tabRaf1 = new Matrice(tabRef1, MATRICE1);
    Matrice tabRaf2 = new Matrice(tabRef2, MATRICE2);
    Matrice tabRaf3 = new Matrice(tabRef3, MATRICE3);

    public Object[] tabMapRef = { tabRaf1, tabRaf2, tabRaf3 };

    // D�claration d'un tableau de 25 cases
    public Object[] tabCasesMap = new Object[25];

    public int[][][] tabTest = {};

    public int[][] mapJeu;
    public int[][] mapRef;

    /** tests Variables **/
    float mPreviousX = 0;
    float mPreviousY = 0;
    MotionEvent act;
    float positionClickX;
    float positionClickY;

    public IntelWorkOutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(">>> Projet", "IntelWorkOutView");

        holder = getHolder();// Recuperation du Holder
        holder.addCallback(this);// Ajout

        intelWorkcontext = context;
        Log.i(">>> Projet", "IntelWorkOutView 1 ");

        intelWorkRes = intelWorkcontext.getResources();
        Log.i(">>> loadimages", "IntelWorkOutView 2 ");

        loadimages(intelWorkRes);
        Log.i(">>> Projet", "IntelWorkOutView 3 ");

        rd = new Random();
        cv_thread = new Thread(this);
        setFocusable(true);
        setOnClickListener(this);

    }

    private void loadimages(Resources res) {
        Log.i("-> Fct <-", " loadimages ");

        // Chargement Img BLeu
        case_Bleu = BitmapFactory.decodeResource(res, R.drawable.img_bleu_big);
        case_Bleu_claire = BitmapFactory.decodeResource(res,
                R.drawable.img_bleu_big_claire);
        case_Bleu_Small = BitmapFactory.decodeResource(res,
                R.drawable.img_bleu_little);

        // Chargement Img Rouge
        case_Rouge = BitmapFactory
                .decodeResource(res, R.drawable.img_rouge_big);
        case_Rouge_claire = BitmapFactory.decodeResource(res,
                R.drawable.img_rouge_big_claire);
        case_Rouge_Small = BitmapFactory.decodeResource(res,
                R.drawable.img_rouge_little);

        // D�finition de la taille de chaque images
        mapTileSize = case_Bleu.getWidth() + 2;
        mapTileSizeMinus = case_Bleu_Small.getWidth() + 1;

        Log.i(">>> Projet", " loadimage ");

    }

    public void initparameters() {
        Log.i("-> Fct <-", " initparameters ");
        // D�finition des tailles des MAPs
        mapJeu = new int[mapHeight][mapWidth];
        mapRef = new int[mapHeight][mapWidth];

        // Initialisation Variables
        typeMvt = mvtInitial;

        // Limite haute matrice
        double temp = getHeight() / 2.2;
        mapTopAnchor = (int) temp;

        // Limite gauche matrice
        mapLeftAnchor = (getWidth() - mapWidth * mapTileSize) / 2;

        // Limite basse matrice
        mapBotAnchor = getHeight() / 2 + mapWidth * mapTileSize;

        // Limite droite matrice
        mapRightAnchor = getWidth() - mapLeftAnchor;

        // limite haute petite matrice
        mapTopAnchorMinus = (getHeight() - mapHeight * mapTileSizeMinus - case_Bleu
                .getHeight()) / 6;

        // Limite gauche petite matrice
        mapLeftAnchorMinus = (getWidth() - mapWidth * mapTileSizeMinus) / 2;

        Log.i(">>> InitJeu", " InitJeu ");
        initRandMapRef();
        initmap();
        mellangeMap();

        iniTime.setToNow();

        if (loadExist()) {
            loadGame();
            Log.i(">>> LoadGame", "LoadJeu");
        } else {
            Log.i(">>> InitJeu", " InitJeu ");
            initRandMapRef();
            initmap();
            mellangeMap();
        }
    }

    private void initRandMapRef() {
        rd = new Random();

        idReference = rd.nextInt(3);

    }

    private void initmap() {
        Log.i("-> Fct <-", " initmap ");
        int compteur = 0;
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {

                compteur = compteur + 1;

                mapRef[i][j] = ((Matrice) tabMapRef[idReference])
                        .getElementMatrice(i, j);
                mapJeu[i][j] = ((Matrice) tabMapRef[idReference])
                        .getElementMatrice(i, j);

                matriceJeu = ((Matrice) tabMapRef[idReference]).getNom();// Recuperation
                // du
                // nom
                // de
                // la
                // matrice
                // de
                // jeu
            }
        }
    }

    private void mellangeMap() {
        int intTmp, jNew, iNew;

        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                intTmp = mapJeu[i][j];
                rd = new Random();
                iNew = rd.nextInt(mapHeight);
                rd = new Random();
                jNew = rd.nextInt(mapWidth);
                mapJeu[i][j] = mapJeu[iNew][jNew];
                mapJeu[iNew][jNew] = intTmp;
            }
        }
    }

    private void tempsEcoule() {

        if (!boolVictory) {
            currentTime.setToNow();
            if (currentTime.second >= iniTime.second) {
                tempsJeu = (currentTime.second - iniTime.second);
                tempsJeu += oldTempsJeu;
                if (tempsJeu >= 60) {
                    tempsJeu -= 60;

                } else {
                    if (tempsJeu != 59)
                        first = true;
                }
                if (tempsJeu < 10) {
                    first = true;
                    if (minuteJeu < 10) {

                        temps = "0" + minuteJeu + "" + ":0" + tempsJeu;

                    } else {
                        temps = minuteJeu + "" + ":0" + tempsJeu;

                    }
                } else {
                    if (first) {
                        if (minuteJeu < 10) {

                            temps = "0" + minuteJeu + "" + ":" + tempsJeu;
                        } else {
                            temps = minuteJeu + "" + ":" + tempsJeu;
                        }
                    }
                    if (tempsJeu == 59 && first) {
                        minuteJeu++;
                        first = false;
                    }
                }

            } else {
                tempsJeu = ((currentTime.second + 60) - iniTime.second);
                tempsJeu += oldTempsJeu;
                if (tempsJeu >= 60) {
                    tempsJeu -= 60;
                } else {
                    if (tempsJeu != 59)
                        first = true;
                }
                if (tempsJeu < 10) {
                    first = true;
                    if (minuteJeu < 10) {
                        temps = "0" + minuteJeu + "" + ":0" + tempsJeu;

                    } else {
                        temps = minuteJeu + "" + ":0" + tempsJeu;

                    }
                } else {
                    if (first) {
                        if (minuteJeu < 10) {
                            temps = "0" + minuteJeu + "" + ":" + tempsJeu;
                        } else {
                            temps = minuteJeu + "" + ":" + tempsJeu;
                        }
                    }
                    if (tempsJeu == 59 && first) {
                        minuteJeu++;
                        first = false;
                    }
                }
            }
        }
    }

    public void setIntentDataPlayer(Intent intentClassDataPlayer) {
        this.intentDataPlayer = intentClassDataPlayer;
    }

    @Override
    public void run() {
        Log.i("-> Fct <-", " run ");
        Canvas c = null;
        while (in) {
            tempsEcoule();
            try {
                cv_thread.sleep(40);
                try {

                    c = holder.lockCanvas(null);
                    dessin(c);

                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            } catch (Exception e) {
                Log.e("-> RUN <-", "PB DANS RUN");
                cv_thread.currentThread().interrupt();
                break;
            }
        }
        cv_thread.interrupt();

    }

    public void paintMap(Canvas canvas) {
        // Log.i("-> Fct <-", " paintMap ");
        Bitmap tmpimg = case_Bleu;
        Bitmap tmpSmallimg = case_Bleu_Small;

        // Fct de dessin de la matrice de r�f�rence
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {

                switch (mapRef[i][j]) {

                    case cst_Bleu:
                        tmpSmallimg = case_Bleu_Small;
                        break;
                    case cst_Rouge:
                        tmpSmallimg = case_Rouge_Small;
                        break;
                    default:
                        break;
                }

                canvas.drawBitmap(tmpSmallimg, mapLeftAnchorMinus + j
                        * mapTileSizeMinus, mapTopAnchorMinus + i
                        * mapTileSizeMinus, null);
            }
        }

        // Fct de dessin de la matrice Modifiable
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {

                switch (mapJeu[i][j]) {
                    case cst_Bleu:
                        tmpimg = case_Bleu;
                        break;
                    case cst_Rouge:
                        tmpimg = case_Rouge;
                        break;
                    case cst_Bleu_Claire:
                        tmpimg = case_Bleu_claire;
                        break;
                    case cst_Rouge_Claire:
                        tmpimg = case_Rouge_claire;
                        break;
                    default:
                        break;

                }
                canvas.drawBitmap(tmpimg, mapLeftAnchor + j * mapTileSize,
                        mapTopAnchor + i * mapTileSize, null);
            }
        }
    }

    private void dessin(Canvas canvas) {
        // Log.i("-> Fct <-", " dessin ");
        canvas.drawRGB(105, 105, 105);
        paintMap(canvas);
        paintScore(canvas);

    }

    private void paintScore(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(mapTopAnchorMinus/2);
        canvas.drawText(temps, getWidth() / 3, mapTopAnchorMinus/2, paint);

    }

    public void fctModifMapX() {
        int[] tmp = { -1, -1, -1, -1, -1 };
        int i;
        if (colonneXOrigine < colonneXNew) {
            for (i = 0; i < mapWidth; i++) {
                switch (i) {
                    case 0:
                        tmp[i] = mapJeu[ligneYOrigine][4];
                        break;
                    case 1:
                        tmp[i] = mapJeu[ligneYOrigine][0];
                        break;
                    case 2:
                        tmp[i] = mapJeu[ligneYOrigine][1];
                        break;
                    case 3:
                        tmp[i] = mapJeu[ligneYOrigine][2];
                        break;
                    case 4:
                        tmp[i] = mapJeu[ligneYOrigine][3];
                        break;
                }
            }
        } else {
            for (i = 0; i < mapWidth; i++) {
                switch (i) {
                    case 0:
                        tmp[i] = mapJeu[ligneYOrigine][1];
                        break;
                    case 1:
                        tmp[i] = mapJeu[ligneYOrigine][2];
                        break;
                    case 2:
                        tmp[i] = mapJeu[ligneYOrigine][3];
                        break;
                    case 3:
                        tmp[i] = mapJeu[ligneYOrigine][4];
                        break;
                    case 4:
                        tmp[i] = mapJeu[ligneYOrigine][0];
                        break;
                }
            }
        }

        for (i = 0; i < mapWidth; i++) {
            if (tmp[i] == cst_Bleu) {
                mapJeu[ligneYOrigine][i] = cst_Bleu_Claire;
            } else if (tmp[i] == cst_Rouge) {
                mapJeu[ligneYOrigine][i] = cst_Rouge_Claire;
            } else {
                mapJeu[ligneYOrigine][i] = tmp[i];
            }
        }

    }

    public void fctModifMapY() {
        Log.d("fctModifMapY", "Start");
        int[] tmp = { -1, -1, -1, -1, -1 };
        int i;

        if (ligneYOrigine < ligneYNew) {
            Log.d("fctModifMapY", "Cas ligne origine < ligne new");
            for (i = 0; i < mapHeight; i++) {
                switch (i) {
                    case 0:
                        tmp[i] = mapJeu[4][colonneXOrigine];
                        break;
                    case 1:
                        tmp[i] = mapJeu[0][colonneXOrigine];
                        break;
                    case 2:
                        tmp[i] = mapJeu[1][colonneXOrigine];
                        break;
                    case 3:
                        tmp[i] = mapJeu[2][colonneXOrigine];
                        break;
                    case 4:
                        tmp[i] = mapJeu[3][colonneXOrigine];
                        break;
                }
            }
        } else {
            Log.d("fctModifMapY", "Cas ligne origine > ligne new");
            for (i = 0; i < mapHeight; i++) {
                System.out.println("Colonne : " + colonneXOrigine);
                switch (i) {
                    case 0:
                        tmp[i] = mapJeu[1][colonneXOrigine];
                        break;
                    case 1:
                        tmp[i] = mapJeu[2][colonneXOrigine];
                        break;
                    case 2:
                        tmp[i] = mapJeu[3][colonneXOrigine];
                        break;
                    case 3:
                        tmp[i] = mapJeu[4][colonneXOrigine];
                        break;
                    case 4:
                        tmp[i] = mapJeu[0][colonneXOrigine];
                        break;
                }
            }
        }

        for (i = 0; i < mapHeight; i++) {
            if (tmp[i] == cst_Bleu) {
                mapJeu[i][colonneXOrigine] = cst_Bleu_Claire;
            } else if (tmp[i] == cst_Rouge) {
                mapJeu[i][colonneXOrigine] = cst_Rouge_Claire;
            } else {
                mapJeu[i][colonneXOrigine] = tmp[i];
            }
        }
    }

    public void fctRazColor() {
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if (mapJeu[i][j] == cst_Bleu_Claire) {
                    mapJeu[i][j] = cst_Bleu;
                } else if (mapJeu[i][j] == cst_Rouge_Claire) {
                    mapJeu[i][j] = cst_Rouge;
                }
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        initparameters();
        in=true;
        cv_thread = new Thread(this);
        cv_thread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        Log.i("-> FCT <-", "surfaceCreated");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("-> FCT <-", "surfaceDestroyed");
        if(boolVictory){
            deleteSave();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        // Log.d("onTouchEvent", "r�cup�ration de la position du doigt");
        positionClickX = event.getX();// recuperation position X
        positionClickY = event.getY();// recuperation position Y

        switch (event.getAction()) {// Swtich sur le type d'action
            case MotionEvent.ACTION_MOVE:
                // System.out.println("ACTION_MOVE");

                if (moveInMatrice(event)) {// test si on est dans la matrice alors
                    colonneXNew = fctRecupColX(positionClickX);// R�cup�ration
                    // de la
                    // colonne o�
                    // notre
                    // doigt se trouve
                    ligneYNew = fctRecupLigneY(positionClickY);// R�cup�ration
                    // de la
                    // ligne o� notre
                    // doigt se trouve

                    if (colonneXOrigine == colonneXNew
                            && ligneYOrigine != ligneYNew) {// si nous sommes tjrs
                        // dans la meme colonne
                        // mais pu sur la m�me
                        // ligne alors
                        mvtAxeVertical = true; // mvt sur l'axe des Y
                        if (mvtAxeHorizontal == false && mvtAxeVertical == true) { // test
                            // qu'il
                            // n'y
                            // a
                            // qu'un
                            // mouvement
                            // sur
                            // l'axe
                            // des
                            // Y

                            fctModifMapY();// Appel fct pour modifier la Map
                            ligneYOrigine = ligneYNew;// nouvelle ligne devient
                            // alors la ligne de
                            // r�f�rence
                        }
                    } else if (colonneXOrigine != colonneXNew
                            && ligneYOrigine == ligneYNew) {// si nous sommes tjrs
                        // dans la meme ligne
                        // mais pu sur la m�me
                        // colonne alors
                        mvtAxeHorizontal = true;// mvt sur l'axe des X
                        if (mvtAxeHorizontal == true && mvtAxeVertical == false) {// test
                            // qu'il
                            // n'y
                            // a
                            // qu'un
                            // mouvement
                            // sur
                            // l'axe
                            // des
                            // X
                            fctModifMapX();// Appel fct pour modifier la Map
                            colonneXOrigine = colonneXNew;// nouvelle colonne
                            // devient la colonne de
                            // r�f�rence
                        }
                    } else if (colonneXOrigine != colonneXNew
                            && ligneYOrigine != ligneYNew) {
                        mvtAxeHorizontal = true;
                        mvtAxeVertical = true;
                    }
                }

                break;
            case MotionEvent.ACTION_DOWN:
                //System.out.println("ACTION_DOWN");
                // Action lorsque le joueur touche l'ecran
                if (moveInMatrice(event)) {
                    colonneXOrigine = fctRecupColX(positionClickX);
                    ligneYOrigine = fctRecupLigneY(positionClickY);
                }
                break;
            case MotionEvent.ACTION_UP:
                //System.out.println("ACTION_UP");
                // RAZ des booleans
                mvtAxeHorizontal = false;
                mvtAxeVertical = false;
                colonneXOrigine = -1;
                ligneYOrigine = -1;

                fctRazColor();// Remise en bleu et rouge couleur initiale
                fctTestVictory();// Test vitoire
                if (boolVictory) {
                    fctMessageVictory();
                    deleteSave();
                }
                break;
            default:
                System.out.println();
        }

        return super.onTouchEvent(event);
    }


    public int fctRecupColX(float positionX) {
        int i, x = 0;
        float x_event = positionX;

        for (i = 0; i < mapWidth; i++) {
            if (x_event > mapLeftAnchor + (mapTileSize * i)
                    && x_event < mapLeftAnchor + (mapTileSize * (i + 1))) {
                x = i;
            }
        }
        return x;
    }

    public int fctRecupLigneY(float positionY) {
        int i, y = 0;
        float y_event = positionY;

        for (i = 0; i < mapWidth; i++) {
            if (y_event > mapTopAnchor + (mapTileSize * i)
                    && y_event < mapTopAnchor + (mapTileSize * (i + 1))) {
                y = i;
            }
        }
        return y;
    }

    public void fctTestVictory() {

        boolean boolTest = true;
        int i = 0, j = 0;
        for (i = 0; i < mapHeight; i++) {
            for (j = 0; j < mapWidth; j++) {
                if (mapJeu[i][j] == mapRef[i][j]) {
                    boolVictory = true;
                } else if (mapJeu[i][j] != mapRef[i][j]) {
                    boolVictory = false;
                    boolTest = false;
                    break;
                }
            }
            if (boolTest == false) {
                break;
            }
        }
    }

    public boolean moveInMatrice(MotionEvent event) {
        boolean boolTest = false;
        float x = event.getX();
        float y = event.getY();
        if ((y > mapTopAnchor && y < mapBotAnchor)
                && (x < mapRightAnchor && x > mapLeftAnchor)) {
            boolTest = true;
        } else {
            boolTest = false;
        }
        return boolTest;
    }

    public void fctMessageVictory() {

        AlertDialog.Builder messVictory = new AlertDialog.Builder(
                intelWorkcontext);

        messVictory.setTitle(R.string.TitreBtnAPropos);

        messVictory.setIcon(R.drawable.ic_about);

        TextView txtViewQts = new TextView(intelWorkcontext);

        txtViewQts.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        txtViewQts.setPadding(20, 10, 20, 10);
        txtViewQts.setTextSize(20);
        txtViewQts
                .setText(Html
                        .fromHtml("<center><small>Victoire</small></center>"
                                + "<br>"
                                + "<br>"
                                + "<center><b>Vous avez gagne ! Felicitation</b></center><br>"
                                + "<br>"
                                + "<small>Voulez vous sauvegarder votre temps</small><br>"));
        messVictory.setView(txtViewQts);

        messVictory.setPositiveButton("Oui",
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        in = false;
                        try {
                            intentDataPlayer.putExtra("TEMPS", temps);
                            intentDataPlayer.putExtra("MATRICE", matriceJeu);
                            parentActivity.startActivity(intentDataPlayer);
                        } catch (Exception e) {
                            Log.i("Launch Activity fail", e.toString());
                        }

                        parentActivity.finish();

                    }
                });
        messVictory.setNegativeButton("Non",
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        in = false;
                        parentActivity.finish();

                    }
                });
        messVictory
                .setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
        messVictory.show();
    }

    public void saveGame() {
        SharedPreferences prefs = getContext().getSharedPreferences(
                "MyContext", IntelWorkOutActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        StringBuilder sauvegardeRef = new StringBuilder();
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                sauvegardeRef.append(mapRef[i][j]).append(",");
            }
        }
        editor.putString("mapPref", sauvegardeRef.toString());
        StringBuilder sauvegardeJeu = new StringBuilder();
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                sauvegardeJeu.append(mapJeu[i][j]).append(",");
            }
        }
        editor.putString("mapJeu", sauvegardeJeu.toString());

        editor.putInt("MinuteJeu", minuteJeu);
        editor.putInt("TempsJeu", tempsJeu);

        editor.commit();

    }

    public void loadGame() {
        SharedPreferences prefs = getContext().getSharedPreferences(
                "MyContext", IntelWorkOutActivity.MODE_PRIVATE);

        String retourRef = prefs.getString("mapPref", null);
        String retourJeu = prefs.getString("mapJeu", null);

        Log.i(">>> restaurer", " mapRef ");
        StringTokenizer stRef = new StringTokenizer(retourRef, ",");
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                mapRef[i][j] = Integer.parseInt(stRef.nextToken());

            }
        }

        Log.i(">>> restaurer", " mapJeu ");
        StringTokenizer stJeu = new StringTokenizer(retourJeu, ",");
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                mapJeu[i][j] = Integer.parseInt(stJeu.nextToken());
            }
        }

        minuteJeu = prefs.getInt("MinuteJeu", 0);
        oldTempsJeu = prefs.getInt("TempsJeu", 0);

    }

    public boolean loadExist() {
        SharedPreferences prefs = getContext().getSharedPreferences(
                "MyContext", IntelWorkOutActivity.MODE_PRIVATE);

        if (prefs.getString("mapPref", null) != null
                && prefs.getString("mapJeu", null) != null
                && prefs.getInt("TempsJeu", 0) != 0) {
            return true;
        } else {

            return false;
        }
    }

    public void deleteSave() {
        SharedPreferences prefs = getContext().getSharedPreferences(
                "MyContext", IntelWorkOutActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.remove("mapRef");
        editor.remove("mapJeu");
        editor.remove("MinuteJeu");
        editor.remove("TempsJeu");

        editor.commit();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    public void setThread(boolean etat){
        this.in=etat;

    }


}
