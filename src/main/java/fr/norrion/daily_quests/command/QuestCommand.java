package fr.norrion.daily_quests.command;

import org.bukkit.command.CommandSender;

public interface QuestCommand {
    void execute(CommandSender commandSender, String[] args);
}
