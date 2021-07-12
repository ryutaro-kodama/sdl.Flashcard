package jp.ac.titech.itpro.sdl.frashcard.test.with_friend.connection;

import android.content.Intent;

import jp.ac.titech.itpro.sdl.frashcard.test.with_friend.TestWithFriendHomeActivity;

public abstract class Agent {
    private final static String TAG = Agent.class.getSimpleName();

    final TestWithFriendHomeActivity activity;

    Agent(TestWithFriendHomeActivity activity) {
        this.activity = activity;
    }

    public void close() {
//        if (thread != null) {
//            thread.close();
//            thread = null;
//        }
    }

    public abstract void onActivityResult(int reqCode, int resCode, Intent data);
}
