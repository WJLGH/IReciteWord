package com.sdut.soft.ireciteword.reviewWord;

import android.content.Context;

import com.sdut.soft.ireciteword.bean.User;
import com.sdut.soft.ireciteword.bean.Word;
import com.sdut.soft.ireciteword.dao.WordDao;
import com.sdut.soft.ireciteword.user.UserService;
import com.sdut.soft.ireciteword.utils.SettingsUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 背诵单词序列类
 */
public class WordReciteService {
    Context context;
    String meta;
    WordOptions currentE;
    List<WordOptions> eList ;
    Iterator<WordOptions> itr;

    public WordReciteService(Context context) {
        this.context = context;
        this.meta = SettingsUtils.getMeta(context);
        eList = new ArrayList<>();
        User user = new UserService(context).currentUser();
        List<Word> words = new WordDao(context).getReviewWords(meta, user);
        if(words == null) {
            return;
        }
        for (Word word : words) {
            eList.add(new WordOptions(context, meta, word));
        }
        itr = eList.iterator();
    }

    /**
     * 移除当前记住的单词
     */
    private void removeFromList() {
        itr.remove();
    }
    /**
     * 开始的时候也要调用getNext类似于迭代器模式，
     * 获取下一个单词
     * @return
     */
    public WordOptions getNext() {
        //如果背完了
        if (isFinish()) {
            return null;
        }
        //如果到列表最后则从头循环
        if (!itr.hasNext()) {
            itr = eList.iterator();
        }
        currentE = itr.next();
        return currentE ;
    }
    public int choose(int index) {
        if(currentE.isRight(index)) {
            removeFromList();
            return 1 ;
        }
        return 0;
    }
    /**
     * 是否背完了一个单词序列
     * @return
     */
    private boolean isFinish() {
        return null == eList || eList.isEmpty()|| null == itr;
    }
    public int getCnt() {
        if (null == eList) {
            return 0;
        }
        return eList.size();
    }
}
