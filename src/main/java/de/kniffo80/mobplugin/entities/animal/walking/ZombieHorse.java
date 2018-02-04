package de.kniffo80.mobplugin.entities.animal.walking;

import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import de.kniffo80.mobplugin.entities.animal.WalkingAnimal;
import de.kniffo80.mobplugin.utils.Utils;

public class ZombieHorse extends WalkingAnimal {

    public static final int NETWORK_ID = 27;

    public ZombieHorse(FullChunk chunk, CompoundTag nbt) {
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
    public Item[] getDrops() {
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            return new Item[] { Item.get(Item.ROTTEN_FLESH, 1, 1) };
        }
        return new Item[0];
    }

    @Override
    public int getKillExperience() {
        return Utils.rand(1, 4);
    }

}
