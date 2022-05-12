package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.model.quest.reward.QuestReward;
import fr.norrion.daily_quests.utils.enumeration.ListProjectile;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

public class QuestModelHit extends QuestModel {
    private ListProjectile damageCause;
    private EntityType entity;
    private int amount;
    private boolean dealOrHit;

    public QuestModelHit(String pattern, String key, List<String> description, List<QuestReward> rewards) {
        super(pattern, QuestModelType.HIT, key, description, rewards);

    }

    public ListProjectile getDamageCause() {
        return damageCause;
    }

    public int getAmount() {
        return amount;
    }

    public EntityType getEntity() {
        return entity;
    }

    public boolean isDealOrHit() {
        return dealOrHit;
    }

    @Override
    public int getProgressionEnd() {
        return amount;
    }

    public static QuestModelHit create(MemorySection memorySection, String key) {
        if (memorySection.contains("material")
                && memorySection.contains("damage-cause")
                && (memorySection.contains("amount-to-hit") || memorySection.contains("amount-damage"))
                && memorySection.contains("rarity")) {
            String material = memorySection.getString("material");
            int amount = memorySection.contains("amount") ? memorySection.getInt("amount") : 1;
            String name = memorySection.contains("name") ? memorySection.getString("name") : "";
            String customModel = (memorySection.contains("custom-model") && NumberUtils.isNumber(key)) ? memorySection.getString("custom-model") : "";
            String pattern = material + ":" + amount + ":" + name + ":" + customModel;
            String damageCause = memorySection.getString("damage-cause");
            String entity = memorySection.getString("entity");
            int amountToHit = memorySection.getInt("amount-to-hit");
            int amountDamage = memorySection.getInt("amount-damage");
            List<String> description = memorySection.contains("description") ? memorySection.getStringList("description") : new ArrayList<>();
            QuestModelHit quest = new QuestModelHit(pattern, key, description, QuestReward.create(memorySection, key));
            quest.damageCause = ListProjectile.valueOf(damageCause.toUpperCase());
            quest.entity = entity != null ? EntityType.valueOf(entity.toUpperCase()): null;
            quest.amount = amountDamage != 0 ? amountDamage : amountToHit;
            quest.dealOrHit = amountDamage != 0;
            return quest;
        }
        return null;
    }
}
