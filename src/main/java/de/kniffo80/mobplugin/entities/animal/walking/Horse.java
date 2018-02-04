package de.kniffo80.mobplugin.entities.animal.walking;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import de.kniffo80.mobplugin.entities.animal.WalkingAnimal;
import de.kniffo80.mobplugin.utils.Utils;

public class Horse extends WalkingAnimal {

    public static final int NETWORK_ID = 23;

    public Horse(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 1.4f;
    }

    @Override
    public float getHeight() {
        return 1.6f;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(15);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed
                && (player.getInventory().getItemInHand().getId() == Item.WHEAT ||
                player.getInventory().getItemInHand().getId() == Item.APPLE ||
                player.getInventory().getItemInHand().getId() == Item.HAY_BALE ||
                player.getInventory().getItemInHand().getId() == Item.GOLDEN_APPLE ||
                player.getInventory().getItemInHand().getId() == Item.SUGAR ||
                player.getInventory().getItemInHand().getId() == Item.BREAD ||
                player.getInventory().getItemInHand().getId() == Item.GOLDEN_CARROT)
                && distance <= 49;
        }
        return false;
    }

    @Override
    public Item[] getDrops() {
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            return new Item[] { Item.get(Item.LEATHER, Utils.rand(0, 2), 1) };
        }
        return new Item[0];
    }

    @Override
    public int getKillExperience() {
        return Utils.rand(1, 4);
    }

}
