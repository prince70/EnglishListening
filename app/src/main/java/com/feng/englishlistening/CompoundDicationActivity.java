package com.feng.englishlistening;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CompoundDicationActivity extends BaseActivity {
    private final String DBPATH = "/data/data/com.feng.englishlistening/databases/";
    private final String DBFILENAME = "dbcet4l.db";
    private final String DBPATHFN = DBPATH + DBFILENAME;

    private SQLiteDatabase dbCD = null;
    private Cursor cCD = null;

    private MediaPlayer mMediaPlayer = null;

    private TextView tvQuestionLText = null;

    private EditText[] etUAnswer = new EditText[12];
    private String[] strRAnswer = new String[12];
    private String[] strUAnswer = new String[12];
    private boolean isRA = false;

    private TextView tv_grade;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvQuestionLText = (TextView) findViewById(R.id.tvCDQText);

        tv_grade = findViewById(R.id.tv_grade);

        for (int iet = 0; iet <= 11; iet++) {
            etUAnswer[iet] = null;
        }
        etUAnswer[1] = (EditText) findViewById(R.id.etCDQOne);
        etUAnswer[1].setFocusable(true);
        etUAnswer[2] = (EditText) findViewById(R.id.etCDQTwo);
        etUAnswer[3] = (EditText) findViewById(R.id.etCDQThree);
        etUAnswer[4] = (EditText) findViewById(R.id.etCDQFour);
        etUAnswer[5] = (EditText) findViewById(R.id.etCDQFive);
        etUAnswer[6] = (EditText) findViewById(R.id.etCDQSix);
        etUAnswer[7] = (EditText) findViewById(R.id.etCDQSeven);
        etUAnswer[8] = (EditText) findViewById(R.id.etCDQEight);
        etUAnswer[9] = (EditText) findViewById(R.id.etCDQNine);
        etUAnswer[10] = (EditText) findViewById(R.id.etCDQTen);
        etUAnswer[11] = (EditText) findViewById(R.id.etCDQEleven);

        initRUAnswer();

        dbCD = getDatabase();
        cCD = getCursor(dbCD);
        setTVText(cCD);
        getRAnswer(cCD);
        initMediaPlayer();

    }

    @Override
    public View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_cd, null);
    }

    @Override
    public String getHeadTitle() {
        headView.setRightText("提交");
        headView.setListenerRight(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int judge = Judge();
                setRAnswer();
                tv_grade.setText("每道题分数为5分，您最终分数为:"+judge+"分");

            }
        });
        return "复合式听写";
    }

    public void initMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.cet4201306);
        }
        mMediaPlayer.start();
    }

    public void initRUAnswer() {

        for (int iRU = 0; iRU <= 11; iRU++) {
            strRAnswer[iRU] = "";
            strUAnswer[iRU] = "";
        }
    }

    public void getUAnswer() {
        for (int iUA = 1; iUA <= 11; iUA++) {
            strUAnswer[iUA] = etUAnswer[iUA].getText().toString();
        }
    }

    public void getRAnswer(Cursor cRA) {
        if (cRA == null) {
            return;
        }
        if (cRA.moveToFirst()) {
            strRAnswer[1] = cRA.getString(cRA.getColumnIndex("QuestionRAnswer"));
        }
        for (int iRA = 2; iRA <= 11; iRA++) {
            if (cRA.moveToNext()) {
                strRAnswer[iRA] = cRA.getString(cRA.getColumnIndex("QuestionRAnswer"));

            }
        }
    }

    public void setUAnswer() {
        for (int iUA = 1; iUA <= 11; iUA++) {
            etUAnswer[iUA].setEnabled(true);
            etUAnswer[iUA].setBackgroundColor(0xffffff);
            etUAnswer[iUA].setText(strUAnswer[iUA]);
        }
    }

    private int Judge() {
        getUAnswer();
        int grade = 0;
        for (int i = 0; i <= 10; i++) {
            Log.e("Feng", "Judge: " + strUAnswer[i]);
            Log.e("Feng", "Judge: " + strRAnswer[i + 1]);
            if (strUAnswer[i + 1].equals(strRAnswer[i + 1])) {
                grade = grade + 5;
            }
        }
        return grade;
    }

    /**
     * 显示正确答案
     */
    public void setRAnswer() {
        for (int iRA = 1; iRA <= 11; iRA++) {
            etUAnswer[iRA].setBackgroundColor(0xff0000);
            etUAnswer[iRA].setText(strRAnswer[iRA]);
            etUAnswer[iRA].setEnabled(false);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.showcdanswer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.give_cdanswer:
                if (isRA == true) {
                    setUAnswer();
                    isRA = false;
                } else if (isRA == false) {
                    getUAnswer();
                    setRAnswer();
                    isRA = true;
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public SQLiteDatabase getDatabase() {
        try {
            File dir = new File(DBPATH);

            if (!dir.exists())
                dir.mkdir();
            if (!(new File(DBPATHFN)).exists()) {

                InputStream is = getResources().openRawResource(R.raw.dbcet4l);

                FileOutputStream fos = new FileOutputStream(DBPATHFN);

                byte[] buffer = new byte[100000];
                int count = 0;

                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }

                fos.close();

                is.close();

                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DBPATHFN, null);

                return db;
            } else {

                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DBPATHFN, null);

                return db;
            }
        } catch (Exception e) {

            return null;
        }
    }

    public Cursor getCursor(SQLiteDatabase dbTvt) {

        if (dbTvt != null) {

            cCD = dbTvt.query("TCompoundDictation", null, null, null, null, null, null);
            return cCD;
        } else {
            return null;
        }
    }

    public void setTVText(Cursor cTV) {


        if (cTV != null) {

            if (cTV.moveToFirst()) {

                tvQuestionLText.setText(cTV.getString(cTV.getColumnIndex("QuestionLText")));
            } else {
                tvQuestionLText.setText("Cursor moveToFirst Failed");
            }
        } else {
            tvQuestionLText.setText("Cursor is null");
        }
    }


    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        if (cCD != null) {
            cCD.close();
        }
        if (dbCD != null) {
            dbCD.close();
        }
    }


}
