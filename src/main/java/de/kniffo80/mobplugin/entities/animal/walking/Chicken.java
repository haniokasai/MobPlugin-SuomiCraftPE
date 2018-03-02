package suomicraftpe.mobplugin.entities.animal.walking;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import suomicraftpe.mobplugin.entities.animal.WalkingAnimal;
import suomicraftpe.mobplugin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Chicken extends WalkingAnimal {

    public static final int NETWORK_ID = 10;

    public Chicken(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.2f;
        }
        return 0.4f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.35f;
        }
        return 0.7f;
    }

    @Override
    public float getEyeHeight() {
        if (this.isBaby()) {
            return 0.51f;
        }
        return 0.7f;
    }

    @Override
    public float getDrag() {
        return 0.2f;
    }

    @Override
    public float getGravity() {
        return 0.04f;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(4);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.isAlive() && !player.closed
                    && (player.getInventory().getItemInHand().getId() == Item.SEEDS
                    || player.getInventory().getItemInHand().getId() == Item.BEETROOT_SEEDS
                    || player.getInventory().getItemInHand().getId() == Item.MELON_SEEDS
                    || player.getInventory().getItemInHand().getId() == Item.PUMPKIN_SEEDS) && distance <= 49;
        }
        return false;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int featherDrop = Utils.rand(0, 3);
            for (int i = 0; i < featherDrop; i++) {
                drops.add(Item.get(Item.FEATHER, 0, 1));
            }
            drops.add(Item.get(this.isOnFire() ? Item.COOKED_CHICKEN : Item.RAW_CHICKEN, 0, 1));
        }
        return drops.toArray(new Item[drops.size()]);
    }

    @Override
    public int getKillExperience() {
        return Utils.rand(1, 4);
    }

}
