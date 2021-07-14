package jp.ac.titech.itpro.sdl.flashcard.card;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Card implements Parcelable {
    public static final String FRONT = "front";
    public static final String BACK_TRUE = "back_true";
    public static final String BACK_FALSE1 = "back_false1";
    public static final String BACK_FALSE2 = "back_false2";
    private String front;
    private String backTrue;
    private String backFalse1;
    private String backFalse2;
    private int choiceOrder = -1;

    public Card(String front, String backTrue, String backFalse1, String backFalse2) {
        this.front = front;
        this.backTrue = backTrue;
        this.backFalse1 = backFalse1;
        this.backFalse2 = backFalse2;
    }

    public Card(String front, String backTrue, String backFalse1) {
        this(front, backTrue, backFalse1, "");
    }

    public Card(String front, String backTrue) {
        this(front, backTrue, "", "");
    }

    protected Card(Parcel in) {
        front = in.readString();
        backTrue = in.readString();
        backFalse1 = in.readString();
        backFalse2 = in.readString();
    }

    public String getFront() {
        return front;
    }

    public String getBackTrue() {
        return backTrue;
    }

    public String getBackFalse1() {
        return backFalse1;
    }

    public String getBackFalse2() {
        return backFalse2;
    }

    public boolean hasNoChoice() {
        return backFalse1.equals("") && backFalse2.equals("");
    }

    public boolean hasTwoChoice() { return backFalse2.equals(""); }

    public void setChoiceOrder() {
        Random random = new Random();
        if (hasTwoChoice()) {
            // If 'backFalse2' is nothing, get 1 or 2;
            choiceOrder = random.nextInt(2) + 1;
        } else {
            // If else, get 3 ~ 8.
            choiceOrder = random.nextInt(6) + 3;
        }
    }

    public void setChoiceOrder(int num) {
        choiceOrder = num;
    }

    public int getChoiceOrder() {
        return choiceOrder;
    }

    public ArrayList<String> getChoiceList() {
        if (choiceOrder == -1) {
            setChoiceOrder();
        }
        ArrayList<String> choiceList = new ArrayList<>();

        // Based on value of 'choiceOrder', decide sequence of choices.
        switch (choiceOrder) {
            case 1: choiceList.add(getBackTrue()); choiceList.add(getBackFalse1()); break;
            case 2: choiceList.add(getBackFalse1()); choiceList.add(getBackTrue()); break;
            case 3: choiceList.add(getBackTrue()); choiceList.add(getBackFalse1()); choiceList.add(getBackFalse2()); break;
            case 4: choiceList.add(getBackTrue()); choiceList.add(getBackFalse2()); choiceList.add(getBackFalse1()); break;
            case 5: choiceList.add(getBackFalse1()); choiceList.add(getBackTrue()); choiceList.add(getBackFalse2()); break;
            case 6: choiceList.add(getBackFalse1()); choiceList.add(getBackFalse2()); choiceList.add(getBackTrue()); break;
            case 7: choiceList.add(getBackFalse2()); choiceList.add(getBackTrue()); choiceList.add(getBackFalse1()); break;
            case 8: choiceList.add(getBackFalse2()); choiceList.add(getBackFalse1()); choiceList.add(getBackTrue()); break;
            default: assert false; break;
        }

        return choiceList;
    }

    public void writeCard(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name(FRONT).value(front);
        writer.name(BACK_TRUE).value(backTrue);
        writer.name(BACK_FALSE1).value(backFalse1);
        writer.name(BACK_FALSE2).value(backFalse2);
        writer.endObject();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(front);
        dest.writeString(backTrue);
        dest.writeString(backFalse1);
        dest.writeString(backFalse2);
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel src) {
            return new Card(src);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Card{" +
                "front='" + front + '\'' +
                ", backTrue='" + backTrue + '\'' +
                ", backFalse1='" + backFalse1 + '\'' +
                ", backFalse2='" + backFalse2 + '\'' +
                '}';
    }
}
