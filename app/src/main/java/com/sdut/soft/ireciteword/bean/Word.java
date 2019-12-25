package com.sdut.soft.ireciteword.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.sdut.soft.ireciteword.utils.Const;

/**
 * 单词类
 */
public class Word  implements Parcelable {
    //Word_Id, Word_Key, Word_Phono, Word_Trans, Word_Example, Word_Unit;
    private int mId;
    private String mKey;
    private String mPhono;
    private String mTrans;
    private String mExample;
    private Unit mUnit;

    public Word() {
    }

    public Word(int id, String key, String phono, String trans, String example, Unit unit) {
        mId = id;
        mKey = key;
        mPhono = phono;
        mTrans = trans;
        mExample = example;
        mUnit = unit;
    }

    public Word(String key, String phono, String trans, String example) {
        mKey = key;
        mPhono = phono;
        mTrans = trans;
        mExample = example;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getPhono() {
        return mPhono;
    }

    public void setPhono(String phono) {
        mPhono = phono;
    }

    public String getTrans() {
        return mTrans;
    }

    public void setTrans(String trans) {
        mTrans = trans;
    }

    public String getExample() {
        return mExample;
    }

    public void setExample(String example) {
        mExample = example;
    }

    public Unit getmUnit() {
        return mUnit;
    }

    public void setmUnit(Unit mUnit) {
        this.mUnit = mUnit;
    }

    @Override
    public String toString() {
        return "Word{" +
                "mId=" + mId +
                ", mKey='" + mKey + '\'' +
                ", mPhono='" + mPhono + '\'' +
                ", mTrans='" + mTrans + '\'' +
                ", mExample='" + mExample + '\'' +
                ", mUnit=" + mUnit +
                '}';
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mKey);
        dest.writeString(this.mPhono);
        dest.writeString(this.mTrans);
        dest.writeString(this.mExample);
    }

    protected Word(Parcel in) {
        this.mId = in.readInt();
        this.mKey = in.readString();
        this.mPhono = in.readString();
        this.mTrans = in.readString();
        this.mExample = in.readString();
    }

    public static final Parcelable.Creator<Word> CREATOR = new Parcelable.Creator<Word>() {
        public Word createFromParcel(Parcel source) {
            return new Word(source);
        }

        public Word[] newArray(int size) {
            return new Word[size];
        }
    };
}
