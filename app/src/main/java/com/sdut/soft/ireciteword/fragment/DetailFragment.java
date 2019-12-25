package com.sdut.soft.ireciteword.fragment;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;
import com.sdut.soft.ireciteword.R;
import com.sdut.soft.ireciteword.bean.Unit;
import com.sdut.soft.ireciteword.bean.User;
import com.sdut.soft.ireciteword.bean.Word;
import com.sdut.soft.ireciteword.dao.UnitDao;
import com.sdut.soft.ireciteword.dao.WordDao;
import com.sdut.soft.ireciteword.user.UserService;
import com.sdut.soft.ireciteword.utils.Const;



public class DetailFragment extends Fragment {
    private onSpeechListener mOnSpeechListener;
    private ImageView mImageView;
    WordDao wordDao;
    UserService userService;
    UnitDao unitDao;
    ShineButton btnStar;
    public static DetailFragment newInstance(Word word) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Const.WORD_KEY, word);
        DetailFragment detailFgt = new DetailFragment();
        detailFgt.setArguments(bundle);
        return detailFgt;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onSpeechListener) {
            mOnSpeechListener = (onSpeechListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView tvExample = (TextView) view.findViewById(R.id.tv_exam);
        TextView tvKey = (TextView) view.findViewById(R.id.tv_key);
        TextView tvPhono = (TextView) view.findViewById(R.id.tv_phono);
        TextView tvTrans = (TextView) view.findViewById(R.id.tv_trans);
        unitDao = new UnitDao(getContext());
        wordDao = new WordDao(getContext());
        userService = new UserService(getContext());
        final Word word = getArguments().getParcelable(Const.WORD_KEY);
        mImageView = (ImageView) view.findViewById(R.id.icon_speech);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSpeechListener != null) {
                    mOnSpeechListener.speech(word);
                }
            }
        });
        if (word != null) {
            tvExample.setText(word.getExample());
            tvKey.setText(word.getKey());
            tvPhono.setText("[" + word.getPhono() + "]");
            tvTrans.setText(word.getTrans());
        }

        btnStar = (ShineButton)view.findViewById(R.id.btn_star);
        btnStar.init(getActivity());
        User user = userService.currentUser();
        final Unit unit = unitDao.getUnitByName(userService.getUserScb());
        final Word schWord = wordDao.getWordInUnit(word, unit);
        btnStar.setChecked(schWord != null);

        btnStar.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                if(checked) {
                     wordDao.insertWordToUnit(word,unit);
                } else {
                    wordDao.deleteWordFromUnit(schWord,unit);
                }
            }
        });
        return view;
    }

    public void setSpeakImg(int resId) {
        mImageView.setImageResource(resId);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnSpeechListener = null;
    }

    public interface onSpeechListener {
        void speech(Word word);
    }
}
