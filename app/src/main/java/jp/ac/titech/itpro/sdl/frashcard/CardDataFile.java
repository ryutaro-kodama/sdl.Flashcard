package jp.ac.titech.itpro.sdl.frashcard;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static jp.ac.titech.itpro.sdl.frashcard.Card.BACK_FALSE1;
import static jp.ac.titech.itpro.sdl.frashcard.Card.BACK_FALSE2;
import static jp.ac.titech.itpro.sdl.frashcard.Card.BACK_TRUE;
import static jp.ac.titech.itpro.sdl.frashcard.Card.FRONT;

public class CardDataFile {
    private final static String TAG = CardDataFile.class.getSimpleName();
    private final String FILENAME = "CardData.json";

    private final File file;
    private ArrayList<Card> cardData;

    public CardDataFile(Context context){
        // Set target file.
        file = new File(context.getExternalFilesDir(""), FILENAME);

        // Open data file if exists.
        if (!file.exists()) {
            cardData = new ArrayList<Card>();
            return;
        }

        // Get saved data(try-with-resources).
        ArrayList<String> lines = new ArrayList<>();
        try( JsonReader reader = new JsonReader(new FileReader(file)) ){
            cardData = read(reader);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private ArrayList<Card> read(JsonReader reader) throws IOException  {
        ArrayList<Card> result = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            result.add(readCard(reader));
        }
        reader.endArray();

        return result;
    }

    private Card readCard(JsonReader reader) throws IOException {
        String front = "", back_true = "", back_false1 = "", back_false2 = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(FRONT)) {
                front = reader.nextString();
            } else if (name.equals(BACK_TRUE)) {
                back_true = reader.nextString();
            } else if (name.equals(BACK_FALSE1)) {
                back_false1 = reader.nextString();
            } else if (name.equals(BACK_FALSE2)) {
                back_false2 = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new Card(front, back_true, back_false1, back_false2);
    }

    public void addAllTmpData(ArrayList<Card> tmpCardData) {
        cardData.addAll(tmpCardData);
    }

    public ArrayList<Card> getCardData() {
        return cardData;
    }

    public void save() {
        // Open or create file.
        if (!file.exists()) {
            try {
                Log.d(TAG, "getData - create new file at: " + file.toString());
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try ( JsonWriter writer = new JsonWriter(new FileWriter(file)) ) {
            write(writer);
        } catch (IOException e ) {
            System.err.println( e.getMessage() );
        }
    }

    private void write(JsonWriter writer) throws IOException {
        writer.setIndent("  ");

        writer.beginArray();
        for (Card card: cardData){
            card.writeCard(writer);
        }
        writer.endArray();

        writer.close();
    }

    public void resetFile() {
        if (file.exists()) {
            file.delete();
        }
    }

    public String toString() {
        ArrayList<String> tmp = new ArrayList<>();

        for (Card card: cardData) {
            tmp.add(card.toString());
        }
        return tmp.toString();
    }
}
