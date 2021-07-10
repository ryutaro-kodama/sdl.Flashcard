package jp.ac.titech.itpro.sdl.frashcard.test.communication;

import jp.ac.titech.itpro.sdl.frashcard.card.Card;

public class CommunicationDataFactory {
    public CommunicationData make(Card card) {
        return new CommunicationData(card);
    }

    public CommunicationData makeAnswer(int answer) {
        return new CommunicationData(CommunicationData.ANSWER, answer);
    }
}
