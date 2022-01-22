package fr.norrion.daily_quests.model;

import java.util.Date;

public class Quest {
    private final String playerName;
    private final Date start;
    private final Date end;
    private final QuestModel questModel;

    public Quest(String playerName, Date start, Date end, QuestModel questModel) {
        this.playerName = playerName;
        this.start = start;
        this.end = end;
        this.questModel = questModel;
    }
}
