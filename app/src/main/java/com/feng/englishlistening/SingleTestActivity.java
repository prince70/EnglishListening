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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class SingleTestActivity extends BaseActivity {
    private static final String TAG = "SingleTestActivity";
    private final String DBPATH = "/data/data/com.feng.englishlistening/databases/";
    private final String DBFILENAME = "dbcet4l.db";
    private final String DBPATHFN = DBPATH + DBFILENAME;
    private SQLiteDatabase dbST = null;
    private Cursor cST = null;


    private MediaPlayer mMediaPlayer = null;
    private ImageView imgVRight = null, imgVWrong = null;
    private TextView tvRAnswer = null;

    private TextView tvQuestionID = null;
    private RadioButton rbtQuestionAText = null, rbtQuestionBText = null, rbtQuestionCText = null, rbtQuestionDText = null;
    private RadioGroup rbtQuestiongroup = null;
    private RatingBar rBarLevel = null;

    private Button btnSubmit = null, btnNext = null;

    private String strRightA = "", strUserA = "";
    private int isRW = 0;
    private static int rightCount = 0;
    private static int totalCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        tvQuestionID = (TextView) findViewById(R.id.tvSTQID);
        rbtQuestionAText = (RadioButton) findViewById(R.id.rbtSTAText);
        rbtQuestionBText = (RadioButton) findViewById(R.id.rbtSTBText);
        rbtQuestionCText = (RadioButton) findViewById(R.id.rbtSTCText);
        rbtQuestionDText = (RadioButton) findViewById(R.id.rbtSTDText);
        rbtQuestiongroup = (RadioGroup) findViewById(R.id.rgtST);

        rBarLevel = (RatingBar) findViewById(R.id.rBarSTLevel);

        dbST = getDatabase();
        setRBText(dbST);
        if (mMediaPlayer == null) {

            mMediaPlayer = MediaPlayer.create(this, R.raw.cet4201306);

        }
        mMediaPlayer.start();


        imgVRight = (ImageView) findViewById(R.id.imgSTVright);
        imgVWrong = (ImageView) findViewById(R.id.imgSTVwrong);
        tvRAnswer = (TextView) findViewById(R.id.tvSTRAnswer);

        imgVRight.setVisibility(View.INVISIBLE);
        imgVWrong.setVisibility(View.INVISIBLE);
        tvRAnswer.setVisibility(View.INVISIBLE);


        btnSubmit = (Button) findViewById(R.id.btnSTSubmit);
        btnNext = (Button) findViewById(R.id.btnSTNext);


        Button.OnClickListener listener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.btnSTSubmit) {
                    isRW = judgeRW();
                    if (cST.isLast()) {
                        setRightRating(rightCount + isRW, totalCount + 1);//成绩
                    }
                } else if (v.getId() == R.id.btnSTNext) {
                    if (cST == null)
                        return;
                    if (!cST.isLast()) {
                        judgeRW();
                        Log.e(TAG, "onClick: "+strUserA );
                        if (strUserA.equals("")) {
                            Toast.makeText(SingleTestActivity.this, "请您先作答", Toast.LENGTH_SHORT).show();
                        } else {
                            moveNextRecord(cST);
                        }
                    } else {
                        btnNext.setEnabled(false);
                    }
                }

            }
        };

        btnSubmit.setOnClickListener(listener);
        btnNext.setOnClickListener(listener);
    }

    @Override
    public View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_st, null);
    }

    @Override
    public String getHeadTitle() {
        return "听力练习模式";
    }


    /**
     * 下一题
     *
     * @param Cs
     */
    private void moveNextRecord(Cursor Cs) {


        rightCount = rightCount + isRW;

        totalCount = totalCount + 1;

        setRightRating(rightCount, totalCount);

        imgVRight.setVisibility(View.INVISIBLE);
        imgVWrong.setVisibility(View.INVISIBLE);
        tvRAnswer.setVisibility(View.INVISIBLE);

        tvRAnswer.setText("");

        Cs.moveToNext();
        if (Cs != null) {
            tvQuestionID.setText("第" + Cs.getString(Cs.getColumnIndex("QuestionID")) + "题");
            rbtQuestionAText.setText("A." + Cs.getString(Cs.getColumnIndex("OptAText")));
            rbtQuestionBText.setText("B." + Cs.getString(Cs.getColumnIndex("OptBText")));
            rbtQuestionCText.setText("C." + Cs.getString(Cs.getColumnIndex("OptCText")));
            rbtQuestionDText.setText("D." + Cs.getString(Cs.getColumnIndex("OptDText")));
            strRightA = Cs.getString(Cs.getColumnIndex("QuestionRAnswer"));
            if (Cs.isLast()) {
                btnNext.setEnabled(false);
            }
        }

    }


    private int judgeRW() {
        strUserA = "";

        if (rbtQuestionAText.isChecked() == true) {
            strUserA = "A";
        } else if (rbtQuestionBText.isChecked() == true) {
            strUserA = "B";
        } else if (rbtQuestionCText.isChecked() == true) {
            strUserA = "C";
        } else if (rbtQuestionDText.isChecked() == true) {
            strUserA = "D";
        }

        if (strUserA.equals(strRightA)) {
            imgVRight.setVisibility(View.VISIBLE);
            imgVWrong.setVisibility(View.INVISIBLE);
            tvRAnswer.setText("恭喜您，答题正确！");
            tvRAnswer.setVisibility(View.VISIBLE);
            return 1;
        } else if (strUserA.equals("")) {
            Toast.makeText(this, "请您先作答", Toast.LENGTH_SHORT).show();
            return 0;
        } else {
            imgVRight.setVisibility(View.INVISIBLE);
            imgVWrong.setVisibility(View.VISIBLE);
            tvRAnswer.setText("正确答案为" + strRightA);
            tvRAnswer.setVisibility(View.VISIBLE);
            return 0;
        }

    }


    private void setRightRating(int iRcount, int iTotal) {
        float dRate = 0;
        rbtQuestiongroup.clearCheck();
        dRate = (float) (Math.round(100 * iRcount / iTotal) / 100.0);
        rBarLevel.setRating(dRate * rBarLevel.getNumStars());

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


    public void setRBText(SQLiteDatabase dbRbt) {
        if (dbRbt != null) {

            cST = dbRbt.query("TConversation", null, null, null, null, null, null);

            if (cST.moveToFirst()) {

                tvQuestionID.setText("第" + cST.getString(cST.getColumnIndex("QuestionID")) + "题");

                rbtQuestionAText.setText("A. " + cST.getString(cST.getColumnIndex("OptAText")));

                rbtQuestionBText.setText("B. " + cST.getString(cST.getColumnIndex("OptBText")));

                rbtQuestionCText.setText("C. " + cST.getString(cST.getColumnIndex("OptCText")));

                rbtQuestionDText.setText("D. " + cST.getString(cST.getColumnIndex("OptDText")));

                strRightA = cST.getString(cST.getColumnIndex("QuestionRAnswer"));
            }
        }
    }


    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        if (cST != null) {
            cST.close();
        }
        if (dbST != null) {
            dbST.close();
        }
    }

}
