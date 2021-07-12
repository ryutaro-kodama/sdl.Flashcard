package jp.ac.titech.itpro.sdl.frashcard.test.with_friend.connection;

public class AgentSingleton {
    private static Agent agent;

    public static void setAgent(Agent agent) {
        AgentSingleton.agent = agent;
    }

    public static Agent getAgentAndSetNull() {
        Agent tmp = AgentSingleton.agent;
        AgentSingleton.agent = null;
        return tmp;
    }
}
