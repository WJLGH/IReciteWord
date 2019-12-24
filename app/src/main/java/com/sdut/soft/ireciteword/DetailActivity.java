package com.sdut.soft.ireciteword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sdut.soft.ireciteword.adapter.WordPagerAdapter;
import com.sdut.soft.ireciteword.bean.Unit;
import com.sdut.soft.ireciteword.bean.User;
import com.sdut.soft.ireciteword.bean.Word;
import com.sdut.soft.ireciteword.dao.UnitDao;
import com.sdut.soft.ireciteword.dao.WordDao;
import com.sdut.soft.ireciteword.fragment.DetailFragment;
import com.sdut.soft.ireciteword.user.UserService;
import com.sdut.soft.ireciteword.utils.Const;
import com.sdut.soft.ireciteword.utils.SettingsUtils;
import com.sdut.soft.ireciteword.utils.YouDaoAudioUriUtils;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener, DetailFragment.onSpeechListener {
    private static final int MSG_REFRESH = 0x1;

    UnitDao unitDao;
    long period = 2500L;
    private int mLevel = 0;// 2, 1, 0, -1, -2分别代表极慢、稍慢、普通、稍快、极快。
    private List<Word> mWordList;
    private int mWordKey;
    private TextView tvPlay;
    private boolean mIsPlaying = false;
    private Timer mTimer;
    private PlayHandler mPlayHandler;
    private String mMetaKey;
    private ViewPager mViewPager;
    private MediaPlayer mMediaPlayer;
    private WordPagerAdapter mWordPagerAdapter;
    SharedPreferences mSharedPreferences;
    private boolean mIsAutoSpeak;
    private UserService userService;
    Unit unit;
    @BindView(R.id.btn_rm)
    Button btnRm ;

    int reciteCnt = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initData();
        if(mWordList == null) {
            return;
        }
        initViews();
        initPlayer();
        showProgress();
        if (mIsAutoSpeak) {
            speech(mWordList.get(mWordKey));
        }

    }

    private void showProgress() {
        setTitle((mWordKey + 1) + "/" + mWordList.size());
    }

    private void initData() {
        Intent intent = getIntent();



        //获得要背诵的单词
        WordDao wordDao = new WordDao(this);
        unitDao = new UnitDao(this);
        String unitName = intent.getStringExtra("unitName");
        unit = unitDao.getUnitByName(unitName);
        mWordList = wordDao.getNewWordsFromUnit(unit,Const.PER_DAY);

        if (mWordList == null || mWordList.size() == 0) {
            finish();
            return;
        }
        reciteCnt = mWordList.size();


        //设置时间间隔

        period = intent.getLongExtra("period",2500L);

        //设置定时回调函数
        mPlayHandler = new PlayHandler(this);
        mSharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    private void initPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                DetailFragment item = (DetailFragment) mWordPagerAdapter.getFragment(mWordKey);
                item.setSpeakImg(R.mipmap.icon_speaker_on);
                mMediaPlayer.start();
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mMediaPlayer.reset();
                return true;
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                DetailFragment item = (DetailFragment) mWordPagerAdapter.getFragment(mWordKey);
                item.setSpeakImg(R.mipmap.icon_speaker_off);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveReciteWord();
    }

    private void saveReciteWord() {
        unit.setProgress(unit.getProgress()+ reciteCnt);
        unitDao.saveUnit(unit);
        if (mIsPlaying) {
            mTimer.cancel();
        }
        setResult(Const.RECITE_FLAG,null);
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tvPlay = (TextView) findViewById(R.id.tv_play);
        tvPlay.setOnClickListener(this);
        findViewById(R.id.btn_prev).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        btnRm.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mWordPagerAdapter = new WordPagerAdapter(getSupportFragmentManager(), mWordList);
        mViewPager.setAdapter(mWordPagerAdapter);
        mViewPager.setCurrentItem(mWordKey);
        mViewPager.addOnPageChangeListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_prev:
                if (mIsPlaying) {
                    pause();
                }
                if (mWordKey - 1 < 0) {
//                    Toast.makeText(this, R.string.first_page, Toast.LENGTH_SHORT).show();
//                    break;
                    mWordKey = mWordList.size()- 1;
                } else {
                    mWordKey--;
                }
                mViewPager.setCurrentItem(mWordKey);
                break;
            case R.id.tv_play:
                if (mIsPlaying) {
                    pause();
                } else {
                    play();
                }
                break;
            case R.id.btn_next:
                if (mIsPlaying) {
                    pause();
                }
                if (mWordKey + 1 >= mWordList.size()) {
//                    Toast.makeText(this, R.string.last_page, Toast.LENGTH_SHORT).show();
//                    break;
                    mWordKey = 0;
                } else {
                    mWordKey++;
                }
                mViewPager.setCurrentItem(mWordKey);
                break;
            case R.id.btn_rm:
                if(mWordList.size() -1 == 0) {
                    saveReciteWord();
                    finish();
                } else {
                    mWordPagerAdapter.removeItem(mWordKey);
                    showProgress();
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mWordKey = position;
        setTitle((mWordKey + 1) + "/" + mWordList.size());
        if (mIsAutoSpeak) {
            if (mIsPlaying && mLevel < 0) {
                Toast.makeText(this, R.string.toast_too_fast, Toast.LENGTH_SHORT).show();
                pause();
            } else {
                speech(mWordList.get(mWordKey));
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void speech(Word word) {
        String content = word.getKey();
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(this, YouDaoAudioUriUtils.getUri(content));
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class PlayHandler extends Handler {
        private WeakReference<Context> mWeakReference;

        public PlayHandler(DetailActivity detailActivity) {
            mWeakReference = new WeakReference<Context>(detailActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            DetailActivity detailActivity = (DetailActivity) mWeakReference.get();
            if (detailActivity != null) {
                if (msg.what == MSG_REFRESH) {
                    if (detailActivity.mWordKey + 1 > detailActivity.mWordList.size()) {
                     //   detailActivity.pause();
                      //  Toast.makeText(detailActivity, R.string.last_page, Toast.LENGTH_SHORT).show();
                        //页面归零
                        detailActivity.mWordKey = 0;
                        detailActivity.mViewPager.setCurrentItem(detailActivity.mWordKey);
                    } else {
                        detailActivity.mViewPager.setCurrentItem(detailActivity.mWordKey);
                    }
                }
            }
        }
    }


    private void pause() {
        Drawable drawable = getResources().getDrawable(R.drawable.btn_play_selector);
        if (drawable != null) {
            drawable.setBounds(0, 0, 51, 51);
        }
        tvPlay.setCompoundDrawables(null, drawable, null, null);
        tvPlay.setText(R.string.tv_play);
        mIsPlaying = false;
        mTimer.cancel();
    }

    private void play() {
        Drawable drawable = getResources().getDrawable(R.drawable.btn_pause_selector);
        if (drawable != null) {
            drawable.setBounds(0, 0, 51, 51);
        }
        tvPlay.setCompoundDrawables(null, drawable, null, null);
        tvPlay.setText(R.string.tv_pause);
        mIsPlaying = true;
        if (mIsAutoSpeak) {
            speech(mWordList.get(mWordKey));
        }
        mWordKey -= 1;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mWordKey++;
                //定时任务
                mPlayHandler.sendEmptyMessage(MSG_REFRESH);
            }
        };

        mTimer = new Timer();
        mTimer.schedule(timerTask, 0, period);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayHandler.removeCallbacksAndMessages(null);
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        menu.findItem(R.id.menu_item_speak).setChecked(mIsAutoSpeak);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mIsPlaying) {
            pause();
        }
        switch (item.getItemId()) {
            //这里必须是android.R.id.home
            // 不能是 R.id.home
            case android.R.id.home: {
                finish();
                return true;
            }
           /* case R.id.menu_item_speak: {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                if (item.isChecked()) {
                    item.setChecked(false);
                    mIsAutoSpeak = false;
                    editor.putBoolean(Const.AUTO_SPEAK, false);
                } else {
                    item.setChecked(true);
                    mIsAutoSpeak = true;
                    editor.putBoolean(Const.AUTO_SPEAK, true);
                }
                editor.apply();
            }
            break;
            case R.id.menu_item_speed: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.select_speed)
                        .setSingleChoiceItems(
                                getResources().getStringArray(R.array.speed_type),
                                3,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mLevel = which - 2;
                                        mSharedPreferences.edit().putInt(Const.PLAY_SPEED, mLevel).apply();
                                        dialog.dismiss();
                                    }
                                }).show();
            }
            break;*/
        }
        return true;
    }


}

