package jp.ac.titech.itpro.sdl.flashcard.test.with_friend;

import android.util.Log;

import jp.ac.titech.itpro.sdl.flashcard.card.Card;

public class TestWithFriendActivitySender extends TestWithFriendActivity {
    private final static String TAG = TestWithFriendActivitySender.class.getSimpleName();

    @Override
    protected Card getNextCard() {
        Log.d(TAG, "getNextCard");
        // Get next card from card data file.
        Card card = super.getNextCard();
        while (card.hasNoChoice()) {
            // Skip while the card has choice.
            card = super.getNextCard();
        }
        setCard(card);
        setState(State.CardReceived);

        // Send card to receiver.
        thread.send(communicationDataFactory.make(card));
        card.setChoiceOrder();
        thread.send(communicationDataFactory.makeChoiceOrder(card.getChoiceOrder()));

        setState(State.CanDisplayCard);
        return card;
    }
}

