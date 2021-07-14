package jp.ac.titech.itpro.sdl.flashcard.card;

import android.util.JsonWriter;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

public class CardWriter implements Closeable {
    private final static String TAG = CardWriter.class.getSimpleName();
    private final JsonWriter writer;

    public CardWriter(JsonWriter writer) {
        this.writer = writer;
    }

    @Override
    public void close() throws IOException {
        Log.d(TAG, "close");
        writer.close();
    }

    public void flush() throws IOException {
        Log.d(TAG, "flush");
        writer.flush();
    }

    public void beginArray() throws IOException {
        Log.d(TAG, "beginArray");
        writer.beginArray();
    }

    public void endArray() throws IOException {
        Log.d(TAG, "endArray");
        writer.endArray();
    }

    public void write(Card card) throws IOException {
        Log.d(TAG, "write");
        writer.beginObject();
        writer.name(Card.FRONT).value(card.getFront());
        writer.name(Card.BACK_TRUE).value(card.getBackTrue());
        writer.name(Card.BACK_FALSE1).value(card.getBackFalse1());
        writer.name(Card.BACK_FALSE2).value(card.getBackFalse2());
        writer.endObject();
    }
}
