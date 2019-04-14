package com.feng.englishlistening;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.feng.englishlistening.impl.DefaultLrcBuilder;
import com.feng.englishlistening.impl.LrcRow;
import com.feng.englishlistening.interfaces.ILrcBuilder;
import com.feng.englishlistening.interfaces.ILrcView;
import com.feng.englishlistening.interfaces.ILrcViewListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by prince70 on 2019/4/13.
 */
public class ListenDetailActivity extends BaseActivity {

    private static final String TAG = "ListenDetailActivity";
    /**
     * 自定义LrcView，用来展示歌词
     */
    ILrcView mLrcView;
    /**
     * 更新歌词的频率，每100ms更新一次
     */
    private int mPlayerTimerDuration = 100;
    /**
     * 更新歌词的定时器
     */
    private Timer mTimer;
    /**
     * 更新歌词的定时任务
     */
    private TimerTask mTask;
    /**
     * 播放器
     */
    private MediaPlayer mPlayer;

    private String ircname;
    private String mp3name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取自定义的LrcView
        mLrcView = findViewById(R.id.lrcView);

        Intent intent = getIntent();
        if (intent != null) {
            int first = intent.getIntExtra("first", -1);
            int second = intent.getIntExtra("second", -1);
            int third = intent.getIntExtra("third", -1);
            int fourth = intent.getIntExtra("fourth", -1);
            int fifth = intent.getIntExtra("fifth", -1);
            if (first != -1) {
                ircname = "1.lrc";
                mp3name = "1.mp3";

            } else if (second != -1) {
                ircname = "2.lrc";
                mp3name = "2.mp3";

            } else if (third != -1) {
                ircname = "3.lrc";
                mp3name = "3.mp3";

            } else if (fourth != -1) {
                ircname = "4.lrc";
                mp3name = "4.mp3";
            } else if (fifth != -1) {
                ircname = "5.lrc";
                mp3name = "5.mp3";
            }
        }

        //从assets目录下读取歌词文件内容
        String lrc = getFromAssets(ircname);
        //解析歌词构造器
        ILrcBuilder builder = new DefaultLrcBuilder();
        //解析歌词返回LrcRow集合
        List<LrcRow> rows = builder.getLrcRows(lrc);
        //将得到的歌词集合传给mLrcView用来展示
        mLrcView.setLrc(rows);

        //开始播放歌曲并同步展示歌词
        beginLrcPlay();

        //设置自定义的LrcView上下拖动歌词时监听
        mLrcView.setListener(new ILrcViewListener() {
            //当歌词被用户上下拖动的时候回调该方法,从高亮的那一句歌词开始播放
            public void onLrcSought(int newPosition, LrcRow row) {
                if (mPlayer != null) {
                    Log.d(TAG, "onLrcSought:" + row.startTime);
                    mPlayer.seekTo((int) row.startTime);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            /**
             * 先停线程，再调用stop()方法,不然会报空指针
             */
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }


            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    /**
     * 从assets目录下读取歌词文件内容
     *
     * @param fileName
     * @return
     */
    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String result = "";
            while ((line = bufReader.readLine()) != null) {
                if (line.trim().equals(""))
                    continue;
                result += line + "\r\n";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 开始播放歌曲并同步展示歌词
     */
    public void beginLrcPlay() {
        mPlayer = new MediaPlayer();
        try {
            Log.e(TAG, "beginLrcPlay: " + mp3name);

            /**
             * 注意，这点
             * 用MediaPlayer播放assets中的音频文件播放中断并重新播放，代码如上。通过打印Log，发现报错：java.io.IOException: Prepare failed.: status=0x1。
             */
//            mPlayer.setDataSource(getAssets().openFd(mp3name).getFileDescriptor());
            mPlayer.setDataSource(getAssets().openFd(mp3name).getFileDescriptor(),
                    getAssets().openFd(mp3name).getStartOffset(), getAssets().openFd(mp3name).getLength());


            //准备播放歌曲监听
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                //准备完毕
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    if (mTimer == null) {
                        mTimer = new Timer();
                        mTask = new LrcTask();
                        mTimer.scheduleAtFixedRate(mTask, 0, mPlayerTimerDuration);
                    }
                }
            });
            //歌曲播放完毕监听
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    stopLrcPlay();
                }
            });
            //准备播放歌曲
//            mPlayer.prepare();
            mPlayer.prepareAsync();
            //开始播放歌曲
            mPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 停止展示歌曲
     */
    public void stopLrcPlay() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
//        if (mTask != null) {
//            mTask.cancel();
//            mTask = null;
//        }
    }

    /**
     * 展示歌曲的定时任务
     */
    class LrcTask extends TimerTask {
        @Override
        public void run() {
            //获取歌曲播放的位置
            final long timePassed = mPlayer.getCurrentPosition();
            runOnUiThread(new Runnable() {
                public void run() {
                    //滚动歌词
                    mLrcView.seekLrcToTime(timePassed);
                }
            });

        }
    }

    ;

    @Override
    public View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_listen_detail, null);
    }

    @Override
    public String getHeadTitle() {
        return "音乐鉴赏";
    }

}
