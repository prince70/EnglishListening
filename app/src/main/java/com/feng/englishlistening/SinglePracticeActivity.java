package com.feng.englishlistening;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SinglePracticeActivity extends BaseActivity {
    private final String DBPATH = "/data/data/com.feng.englishlistening/databases/";
    private final String DBFILENAME = "dbcet4l.db";
    private final String DBPATHFN = DBPATH + DBFILENAME;

    private SQLiteDatabase dbSP = null;
    private Cursor cSP = null;

    private MediaPlayer mMediaPlayer = null;
    private TextView tvQuestionID = null, tvQuestionLText = null;
    private RadioButton rbtQuestionAText = null, rbtQuestionBText = null, rbtQuestionCText = null, rbtQuestionDText = null;
    private Button btnSPPlay = null, btnSPNext = null, btnSPPre = null, btnSPLText = null;
    private RadioGroup rbtQuestiongroup = null;

    private int iPer = 1000;
    private int iCurSec = 0;
    private String strRightA = "";
    private int[] iUserAnswer = new int[100];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvQuestionID = (TextView) findViewById(R.id.tvSPQID);
        rbtQuestionAText = (RadioButton) findViewById(R.id.rbtSPAText);
        rbtQuestionBText = (RadioButton) findViewById(R.id.rbtSPBText);
        rbtQuestionCText = (RadioButton) findViewById(R.id.rbtSPCText);
        rbtQuestionDText = (RadioButton) findViewById(R.id.rbtSPDText);
        rbtQuestiongroup = (RadioGroup) findViewById(R.id.rgtSP);


        for (int iUA = 0; iUA < 100; iUA++) {
            iUserAnswer[iUA] = 0;
        }

        dbSP = getDatabase();
        setRBText(dbSP);
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.cet4201306);
        }

        mMediaPlayer.start();

        tvQuestionLText = (TextView) findViewById(R.id.tvSPLText);
        btnSPNext = (Button) findViewById(R.id.btnSPNext);
        btnSPPre = (Button) findViewById(R.id.btnSPPre);
        btnSPLText = (Button) findViewById(R.id.btnSPLText);
        btnSPPlay = (Button) findViewById(R.id.btnSPPlay);


        btnSPPlay.setOnClickListener(new MyListener());
        btnSPNext.setOnClickListener(new MyListener());
        btnSPPre.setOnClickListener(new MyListener());
        btnSPLText.setOnClickListener(new MyListener());


        rbtQuestiongroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == R.id.rbtSPAText) {

                    setRbtValue(cSP, rbtQuestionAText, 1);
                } else if (checkedId == R.id.rbtSPBText) {

                    setRbtValue(cSP, rbtQuestionBText, 2);
                } else if (checkedId == R.id.rbtSPCText) {

                    setRbtValue(cSP, rbtQuestionCText, 3);
                } else if (checkedId == R.id.rbtSPDText) {

                    setRbtValue(cSP, rbtQuestionDText, 4);
                }
            }
        });
    }

    @Override
    public View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_sp,null);
    }

    @Override
    public String getHeadTitle() {
        return "听力测试模式";
    }


    private void setRbtValue(Cursor Cs, RadioButton rbt, int iValue) {
        int iUa = -1;

        if (rbt.isChecked() == true) {

            iUa = Cs.getInt(Cs.getColumnIndex("QuestionID"));
            if (iUa > 0) {

                iUserAnswer[iUa] = iValue;
            }
        }
    }


    /**
     * 上一道题
     *
     * @param Cs
     */
    private void movePreRecord(Cursor Cs) {
        getUserAnswer(Cs);

        Cs.moveToPrevious();
        tvQuestionID.setText("第" + Cs.getString(Cs.getColumnIndex("QuestionID")) + "题");
        rbtQuestionAText.setText("A." + Cs.getString(Cs.getColumnIndex("OptAText")));
        rbtQuestionBText.setText("B." + Cs.getString(Cs.getColumnIndex("OptBText")));
        rbtQuestionCText.setText("C." + Cs.getString(Cs.getColumnIndex("OptCText")));
        rbtQuestionDText.setText("D." + Cs.getString(Cs.getColumnIndex("OptDText")));

        tvQuestionLText.setText(Cs.getString(Cs.getColumnIndex("QuestionLText")));
//        tvQuestionLText.setText(Cs.getString(Cs.getColumnIndex("QuestionComments")));


        tvQuestionLText.setVisibility(View.GONE);
        btnSPLText.setText("显示听力原文");

        if (Cs.isFirst()) {
            btnSPNext.setEnabled(true);
            btnSPPre.setEnabled(false);
        }
        setUserAnswer(Cs);
    }

    /**
     * 下一道题
     *
     * @param Cs
     */
    private void moveNextRecord(Cursor Cs) {
        getUserAnswer(Cs);

        Cs.moveToNext();
        tvQuestionID.setText("第" + Cs.getString(Cs.getColumnIndex("QuestionID")) + "题");
        rbtQuestionAText.setText("A." + Cs.getString(Cs.getColumnIndex("OptAText")));
        rbtQuestionBText.setText("B." + Cs.getString(Cs.getColumnIndex("OptBText")));
        rbtQuestionCText.setText("C." + Cs.getString(Cs.getColumnIndex("OptCText")));
        rbtQuestionDText.setText("D." + Cs.getString(Cs.getColumnIndex("OptDText")));

        tvQuestionLText.setText(Cs.getString(Cs.getColumnIndex("QuestionLText")));
//        tvQuestionLText.setText(Cs.getString(Cs.getColumnIndex("QuestionComments")));

        tvQuestionLText.setVisibility(View.GONE);
        btnSPLText.setText("显示听力原文");

        if (Cs.isLast()) {
            btnSPNext.setEnabled(false);
            btnSPPre.setEnabled(true);
        }
        setUserAnswer(Cs);
    }

    /**
     * 播放听力
     *
     * @param cs
     */
    public void playLis(Cursor cs) {

        if (mMediaPlayer == null)
            return;

        if (mMediaPlayer.isPlaying() == true) {

            mMediaPlayer.pause();
        }


        if (cs != null) {
            if (cSP.moveToFirst()) {
                iCurSec = cSP.getInt(cSP.getColumnIndex("QuestionStartTime")) * iPer;
            } else {
                iCurSec = 0;
            }

        } else {

            iCurSec = 0;
        }


        mMediaPlayer.seekTo(iCurSec);

        mMediaPlayer.start();
    }


    public void setRBText(SQLiteDatabase dbRbt) {
        if (dbRbt != null) {

            cSP = dbRbt.query("TConversation", null, null, null, null, null, null);

            if (cSP.moveToFirst()) {

                tvQuestionID.setText("第" + cSP.getString(cSP.getColumnIndex("QuestionID")) + "题");

                rbtQuestionAText.setText("A. " + cSP.getString(cSP.getColumnIndex("OptAText")));

                rbtQuestionBText.setText("B. " + cSP.getString(cSP.getColumnIndex("OptBText")));

                rbtQuestionCText.setText("C. " + cSP.getString(cSP.getColumnIndex("OptCText")));

                rbtQuestionDText.setText("D. " + cSP.getString(cSP.getColumnIndex("OptDText")));

                strRightA = cSP.getString(cSP.getColumnIndex("QuestionRAnswer"));
            }
        }
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

    public Cursor getCursor() {
        try {

            dbSP = getDatabase();

            if (dbSP != null) {

                Cursor c = dbSP.query("TConversation", null, null, null, null, null, null);
                return c;
            } else {
                return null;
            }

        } catch (Exception e) {

            return null;
        }
    }

    public void playLis() {

        if (mMediaPlayer == null)
            return;

        if (mMediaPlayer.isPlaying() == true) {

            mMediaPlayer.pause();
        }

        if (cSP != null) {
            if (cSP.moveToFirst()) {
                iCurSec = cSP.getInt(cSP.getColumnIndex("QuestionStartTime")) * iPer;
            } else {
                iCurSec = 0;
            }

        } else {
            iCurSec = 0;
        }


        mMediaPlayer.seekTo(iCurSec);

        mMediaPlayer.start();
    }

    public void getUserAnswer(Cursor cs) {
        int iUa = -1;

        iUa = cs.getInt(cs.getColumnIndex("QuestionID"));
        if (rbtQuestionAText.isChecked() == true) {
            iUserAnswer[iUa] = 1;
        } else if (rbtQuestionBText.isChecked() == true) {
            iUserAnswer[iUa] = 2;
        } else if (rbtQuestionCText.isChecked() == true) {
            iUserAnswer[iUa] = 3;
        } else if (rbtQuestionDText.isChecked() == true) {
            iUserAnswer[iUa] = 4;
        }

    }

    public void setUserAnswer(Cursor cs) {
        int iUa = -1;


        iUa = cs.getInt(cs.getColumnIndex("QuestionID"));

        rbtQuestiongroup.clearCheck();

        if (iUserAnswer[iUa] == 0) {
            rbtQuestionAText.setChecked(false);
            rbtQuestionBText.setChecked(false);
            rbtQuestionCText.setChecked(false);
            rbtQuestionDText.setChecked(false);
        } else if (iUserAnswer[iUa] == 1) {
            rbtQuestiongroup.check(rbtQuestionAText.getId());
        } else if (iUserAnswer[iUa] == 2) {
            rbtQuestiongroup.check(rbtQuestionBText.getId());
        } else if (iUserAnswer[iUa] == 3) {
            rbtQuestiongroup.check(rbtQuestionCText.getId());
        } else if (iUserAnswer[iUa] == 4) {
            rbtQuestiongroup.check(rbtQuestionDText.getId());
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        if (cSP != null) {
            cSP.close();
        }
        if (dbSP != null) {
            dbSP.close();
        }
    }


    public class MyListener implements OnClickListener {
        public void onClick(View v) {

            if (v.getId() == R.id.btnSPPre) {

                if (!cSP.isFirst()) {

                    movePreRecord(cSP);

                    if (!btnSPNext.isEnabled()) {

                        btnSPNext.setEnabled(true);
                    }
                } else {

                    btnSPPre.setEnabled(false);

                    btnSPNext.setEnabled(true);
                }
            } else if (v.getId() == R.id.btnSPNext) {

                if (!cSP.isLast()) {

                    moveNextRecord(cSP);

                    if (!btnSPPre.isEnabled()) {

                        btnSPPre.setEnabled(true);
                    }
                } else {

                    btnSPNext.setEnabled(false);

                    btnSPPre.setEnabled(true);
                }
            } else if (v.getId() == R.id.btnSPLText) {

                if (tvQuestionLText.getVisibility() == View.VISIBLE) {

                    tvQuestionLText.setVisibility(View.GONE);

                    btnSPLText.setText("显示听力原文");
                } else {

                    tvQuestionLText.setVisibility(View.VISIBLE);

                    btnSPLText.setText("隐藏听力原文");
                }
            } else if (v.getId() == R.id.btnSPPlay) {

                playLis(cSP);
            }
        }
    }
}