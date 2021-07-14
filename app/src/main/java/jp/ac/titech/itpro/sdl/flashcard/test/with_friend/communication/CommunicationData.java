package jp.ac.titech.itpro.sdl.flashcard.test.with_friend.communication;

import android.os.Parcel;
import android.os.Parcelable;

import jp.ac.titech.itpro.sdl.flashcard.card.Card;

public class CommunicationData implements Parcelable {
    public static final int CARD = 1111;
    public static final int CHOICE_ORDER = 2222;
    public static final int ANSWER = 3333;
    public static final int FINISH = 4444;

    public static final String FIELD_DATA_TYPE = "fieldDataType";
    public static final String FIELD_CARD = "fieldCard";
    public static final String FIELD_CONTENTS = "fieldContents";

    private int dataType;
    private Card card;
    private int contents;

    public CommunicationData(int dataType, Card card, int contents) {
        this.dataType = dataType;
        this.card = card;
        this.contents = contents;
    }

    public CommunicationData(Card card) {
        this(CommunicationData.CARD, card, -1);
    }

    public CommunicationData(int dataType, int contents) {
        this(dataType, new Card("", ""), contents);
    }

    protected CommunicationData(Parcel in) {
        dataType = in.readInt();
        String front = in.readString();
        String backTrue = in.readString();
        String backFalse1 = in.readString();
        String backFalse2 = in.readString();
        card = new Card(front, backTrue, backFalse1, backFalse2);
        contents = in.readInt();
    }

    public int getDataType() {
        return dataType;
    }

    public Card getCard() {
        return card;
    }

    public int getContents() {
        return contents;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dataType);
        card.writeToParcel(dest, flags);
        dest.writeInt(contents);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommunicationData> CREATOR = new Creator<CommunicationData>() {
        @Override
        public CommunicationData createFromParcel(Parcel in) {
            return new CommunicationData(in);
        }

        @Override
        public CommunicationData[] newArray(int size) {
            return new CommunicationData[size];
        }
    };
}
