package jp.ac.titech.itpro.sdl.flashcard.card;

import android.util.JsonReader;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

public class CardReader implements Closeable {
    private final static String TAG = CardReader.class.getSimpleName();
    private final JsonReader reader;

    public CardReader(JsonReader reader) {
        this.reader = reader;
    }

    @Override
    public void close() throws IOException {
        Log.d(TAG, "close");
        reader.close();
    }

    public boolean hasNext() throws IOException {
        Log.d(TAG, "hasNext");
        return reader.hasNext();
    }

    public void beginArray() throws IOException {
        Log.d(TAG, "beginArray");
        reader.beginArray();
    }

    public void endArray() throws IOException {
        Log.d(TAG, "endArray");
        reader.endArray();
    }

    public Card read() throws IOException {
        Log.d(TAG, "read");
        String front = null;
        String backTrue = null;
        String backFalse1 = null;
        String backFalse2 = null;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case Card.FRONT:
                    front = reader.nextString();
                    break;
                case Card.BACK_TRUE:
                    backTrue = reader.nextString();
                    break;
                case Card.BACK_FALSE1:
                    backFalse1 = reader.nextString();
                    break;
                case Card.BACK_FALSE2:
                    backFalse2 = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Card(front, backTrue, backFalse1, backFalse2);
    }
}