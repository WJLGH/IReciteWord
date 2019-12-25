package com.sdut.soft.ireciteword;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sdut.soft.ireciteword.bean.Unit;
import com.sdut.soft.ireciteword.bean.Word;
import com.sdut.soft.ireciteword.dao.WordDao;
import com.sdut.soft.ireciteword.fragment.DetailFragment;
import com.sdut.soft.ireciteword.utils.Const;
import com.sdut.soft.ireciteword.utils.SettingUtils;
import com.sdut.soft.ireciteword.utils.YouDaoAudioUriUtils;

import java.io.IOException;
import java.util.Set;

import static android.R.color.white;

public class WordSpecificActivity extends AppCompatActivity implements DetailFragment.onSpeechListener {

    DetailFragment detailFgt;
    WordDao wordDao;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_specific);
        wordDao = new WordDao(this);
        addDetailFgt();
       // setToolBar();
        initPlayer();
    }

    private void addDetailFgt() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 1);
        Word keyWord = new Word();
        keyWord.setId(id);
        Unit unit = new Unit(SettingUtils.getDefaultSearchUnit(WordSpecificActivity.this));
        Word w = wordDao.getWordInUnit(keyWord,unit);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        detailFgt = DetailFragment.newInstance(w);
        transaction.add(R.id.detail_container, detailFgt);
        transaction.commit();
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

    private void initPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                DetailFragment item = detailFgt;
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
                DetailFragment item = detailFgt;
                item.setSpeakImg(R.mipmap.icon_speaker_off);
            }
        });
    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Word");//设置主标题
        toolbar.setTitleTextColor(getResources().getColor(white));//设置主标题颜色

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        StatusBarUtil.setColor(this, R.color.color_main_blue);
        //  set status transparent
//        StatusBarUtil.setTransparent(this);
    }

    /**
     * use back button
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
