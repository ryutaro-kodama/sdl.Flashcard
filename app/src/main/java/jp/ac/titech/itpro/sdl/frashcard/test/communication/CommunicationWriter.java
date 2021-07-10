package jp.ac.titech.itpro.sdl.frashcard.test.communication;

import android.util.JsonWriter;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

import jp.ac.titech.itpro.sdl.frashcard.card.Card;
import jp.ac.titech.itpro.sdl.frashcard.card.CardWriter;

public class CommunicationWriter  implements Closeable {
    private final static String TAG = CommunicationWriter.class.getSimpleName();

    private final JsonWriter writer;
    private final CardWriter cardWriter;

    public CommunicationWriter(JsonWriter writer) {
        this.writer = writer;
        this.cardWriter = new CardWriter(writer);
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

    public void write(CommunicationData data) throws IOException {
        Log.d(TAG, "write");
        writer.beginObject();
        writer.name(CommunicationData.FIELD_DATA_TYPE).value(data.getDataType());
        writer.name(CommunicationData.FIELD_CARD);
        cardWriter.write(data.getCard());
        writer.name(CommunicationData.FIELD_CONTENTS).value(data.getContents());
        writer.endObject();
    }
}
