package fr.norrion.daily_quests.model.quest;

import fr.norrion.daily_quests.fileData.Config;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.fileData.QuestModelData;
import fr.norrion.daily_quests.model.quest.model.QuestModel;
import fr.norrion.daily_quests.model.quest.reward.QuestReward;
import fr.norrion.daily_quests.utils.BossBarUtils;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Quest {
    private final int id;
    private final UUID uuid;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private LocalDateTime completeTime;
    private final QuestModel questModel;
    private int progression;
    private final int progressionEnd;

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public Quest(int id, UUID uuid, LocalDateTime start, LocalDateTime end, LocalDateTime complete, QuestModel questModel) {
        this.id = id;
        this.uuid = uuid;
        this.start = start;
        this.end = end;
        this.completeTime = complete;
        this.questModel = questModel;
        this.progression = 0;
        this.progressionEnd = questModel.getProgressionEnd();
    }

    public Quest(int id, MemorySection memorySection, UUID uuid) {
        this.id = id;
        this.uuid = uuid;
        this.start = LocalDateTime.parse(memorySection.getString("start"), formatter);
        this.end = LocalDateTime.parse(memorySection.getString("end"), formatter);
        String completeTime = memorySection.getString("complete");
        this.completeTime = completeTime == null ? null : LocalDateTime.parse(completeTime, formatter);
        this.questModel = QuestModelData.getQuestModel(memorySection.getString("model"));
        this.progression = memorySection.getInt("progression");
        this.progressionEnd = memorySection.getInt("progression-end");
    }

    public QuestModel getQuestModel() {
        return questModel;
    }

    public void save(MemorySection memorySection) {
        memorySection.set(uuid + ".quest." + id + ".start", start.format(formatter));
        memorySection.set(uuid + ".quest." + id + ".end", end.format(formatter));
        if (this.isComplete()) {
            memorySection.set(uuid + ".quest." + id + ".complete", completeTime.format(formatter));
        }
        memorySection.set(uuid + ".quest." + id + ".model", questModel.getKey());
        memorySection.set(uuid + ".quest." + id + ".progression", this.progression);
        memorySection.set(uuid + ".quest." + id + ".progression-end", this.progressionEnd);
    }

    public String getStart() {
        return start.format(formatter);
    }

    public String getEnd() {
        return end.format(formatter);
    }

    public String getComplete() {
        return this.completeTime == null ? "----/--/-- --:--:--" : completeTime.format(formatter);
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getProgression() {
        return progression;
    }

    public int getProgressionEnd() {
        return progressionEnd;
    }

    public int getId() {
        return id;
    }

    public String timeRemainingString() {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (isComplete()) {
            return Message.QUEST$COMPLETED.getString();
        }
        if (isFailed()) {
            return Message.QUEST$FAILED.getString();
        }
        long d = ChronoUnit.DAYS.between(localDateTime, end);
        long h = ChronoUnit.HOURS.between(localDateTime, end) - d * 24;
        long m = ChronoUnit.MINUTES.between(localDateTime, end) - d * 24 * 60 - h * 60;
        long s = ChronoUnit.SECONDS.between(localDateTime, end) - d * 24 * 60 * 60 - h * 60 * 60 - m * 60;
        String dString = d < 1 ? "" : d + Message.DATE$DAYS.getString();
        dString = dString.replace("%s%", d > 1 ? "s " : " ");
        String hString = h < 10 ? "0" + h + Message.DATE$HOURS_SHORT.getString() : h + Message.DATE$HOURS_SHORT.getString();
        String mmString = m < 10 ? "0" + m + Message.DATE$MINUTES_SHORT.getString() : m + Message.DATE$MINUTES_SHORT.getString();
        String sString = s < 10 ? "0" + s + Message.DATE$SECONDS_SHORT.getString() : s + Message.DATE$SECONDS_SHORT.getString();
        return dString + hString + mmString + sString;
    }

    public String getProgressString() {
        String progressBar = "";
        int characterNumber = Config.QUEST$PROGRESS_BAR$CHARACTER_NUMBER.getInt();
        String character = Config.QUEST$PROGRESS_BAR$CHARACTER.getString();
        if (Config.QUEST$PROGRESS_BAR$VALUE_IN_MIDDLE.getBoolean()) {
            StringBuilder left = new StringBuilder(Config.QUEST$PROGRESS_BAR$GREEN.getString());
            StringBuilder right = new StringBuilder();
            int progress = progression * characterNumber / progressionEnd;
            if (progress >= characterNumber / 2) {
                progress -= characterNumber / 2;
                right.append(Config.QUEST$PROGRESS_BAR$GREEN.getString());
                right.append(character.repeat(progress));
                right.append(Config.QUEST$PROGRESS_BAR$RED.getString());
                right.append(character.repeat(Math.max((characterNumber / 2 - progress), 0)));
                left.append(character.repeat(characterNumber / 2));
            } else {
                left.append(character.repeat(progress));
                left.append(Config.QUEST$PROGRESS_BAR$RED.getString());
                left.append(character.repeat(characterNumber / 2 - progress));
                right.append(Config.QUEST$PROGRESS_BAR$RED.getString());
                right.append(character.repeat(characterNumber / 2));
            }
            String value = Config.QUEST$PROGRESS_BAR$VALUE_IN_MIDDLE_PATTERN.getString()
                    .replace("%progress-value%", String.valueOf(progression))
                    .replace("%progress-end%", String.valueOf(progressionEnd));
            progressBar = left + value + right;
        } else {
            StringBuilder value = new StringBuilder(Config.QUEST$PROGRESS_BAR$GREEN.getString());
            int progress = progression / progressionEnd * characterNumber;
            value.append(character.repeat(progress));
            value.append(Config.QUEST$PROGRESS_BAR$RED.getString());
            value.append(character.repeat(characterNumber - progress));
            progressBar = value.toString();
        }

        return Config.QUEST$PROGRESS_BAR$PATTERN.getString()
                .replace("%prefix%", Config.QUEST$PROGRESS_BAR$PREFIX.getString())
                .replace("%progress-bar-character%", progressBar)
                .replace("%suffix%", Config.QUEST$PROGRESS_BAR$SUFFIX.getString());
    }

    public void setProgression(int i) {
        this.progression = i;
        if (isComplete()) {
            finish();
        }
        save();
    }

    public void addProgression(int i) {
        setProgression(i + this.progression);
    }

    public void addProgressionWithBossBar(int i, Player player) {
        addProgression(i);
        if (!Config.QUEST$BOSS_BAR_ENABLE.getBoolean()) {
            return;
        }
        String title = Config.QUEST$BOSS_BAR_MESSAGE.getString()
                .replace("%quest%", this.questModel.getName())
                .replace("%progress-value%", String.valueOf(this.progression))
                .replace("%progress-end%", String.valueOf(this.progressionEnd));
        KeyedBossBar bossBar = BossBarUtils.getBossBar("QuestProgress" + player.getUniqueId() + this.id, title, BarColor.valueOf(Config.QUEST$BOSS_BAR_COLOR.getString()), BarStyle.valueOf(Config.QUEST$BOSS_BAR_STYLE.getString()));
        bossBar.setProgress(1.0 * this.progression / this.progressionEnd);
        bossBar.addPlayer(player);
        BossBarUtils.resetDelay(bossBar);
    }

    private void save() {
        try {
            QuestData.saveOneQuest(this);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$CANNOT_SAVE_QUEST.getString());
        }
    }

    public boolean isComplete() {
        return progression >= progressionEnd;
    }

    public boolean isFailed() {
        return LocalDateTime.now().isAfter(end);
    }

    public boolean needToBePurge() {
        return (isComplete() && completeTime.plusHours(Config.PURGE_OLD_HOUR.getInt()).isBefore(LocalDateTime.now())) ||
                (isFailed() && end.plusHours(Config.PURGE_OLD_HOUR.getInt()).isBefore(LocalDateTime.now()));
    }

    private void finish() {
        this.progression = this.progressionEnd;
        this.completeTime = LocalDateTime.now();
        for (QuestReward reward : this.getQuestModel().getRewards()) {
            reward.execute(this);
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(this.uuid);
        if (offlinePlayer.isOnline()) {
            Player p = offlinePlayer.getPlayer();
            this.questModel.playSound(p);
        }
    }
}
