package jp.ac.titech.itpro.sdl.frashcard.test.communication;

import android.util.JsonReader;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

import jp.ac.titech.itpro.sdl.frashcard.card.Card;
import jp.ac.titech.itpro.sdl.frashcard.card.CardReader;

public class CommunicationReader implements Closeable {
    private final static String TAG = CommunicationReader.class.getSimpleName();

    private final JsonReader reader;
    private final CardReader cardReader;

    public CommunicationReader(JsonReader reader) {
        this.reader = reader;
        this.cardReader = new CardReader(reader);
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

    public CommunicationData read() throws IOException {
        Log.d(TAG, "read");
        int dataType = -1;
        Card card = null;
        int contents = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case CommunicationData.FIELD_DATA_TYPE:
                    dataType = reader.nextInt();
                    break;
                case CommunicationData.FIELD_CARD:
                    card = cardReader.read();
                    break;
                case CommunicationData.FIELD_CONTENTS:
                    contents = reader.nextInt();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new CommunicationData(dataType, card, contents);
    }
}
