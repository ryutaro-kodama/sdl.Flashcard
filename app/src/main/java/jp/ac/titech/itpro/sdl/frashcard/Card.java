package jp.ac.titech.itpro.sdl.frashcard;

import android.util.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Card {
    public static final String FRONT = "front";
    public static final String BACK_TRUE = "back_true";
    public static final String BACK_FALSE1 = "back_false1";
    public static final String BACK_FALSE2 = "back_false2";
    private String front;
    private String back_true;
    private String back_false1;
    private String back_false2;

    public Card(String front, String back_true, String back_false1, String back_false2) {
        this.front = front;
        this.back_true = back_true;
        this.back_false1 = back_false1;
        this.back_false2 = back_false2;
    }

    public Card(String front, String back_true, String back_false1) {
        new Card(front, back_true, back_false1, "");
    }

    public Card(String front, String back_true) {
        new Card(front, back_true, "", "");
    }

    public String getFront() {
        return front;
    }

    public String getBack_true() {
        return back_true;
    }

    public String getBack_false1() {
        return back_false1;
    }

    public String getBack_false2() {
        return back_false2;
    }

    public void writeCard(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name(FRONT).value(front);
        writer.name(BACK_TRUE).value(back_true);
        writer.name(BACK_FALSE1).value(back_false1);
        writer.name(BACK_FALSE2).value(back_false2);
        writer.endObject();
    }

    @Override
    public String toString() {
        return "Card{" +
                "front='" + front + '\'' +
                ", back_true='" + back_true + '\'' +
                ", back_false1='" + back_false1 + '\'' +
                ", back_false2='" + back_false2 + '\'' +
                '}';
    }
}
