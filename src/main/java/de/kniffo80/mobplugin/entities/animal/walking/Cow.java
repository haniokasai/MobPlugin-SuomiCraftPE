package suomicraftpe.mobplugin.entities.animal.walking;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.sound.CowMilkSound;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import suomicraftpe.mobplugin.entities.animal.WalkingAnimal;
import suomicraftpe.mobplugin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Cow extends WalkingAnimal {

    public static final int NETWORK_ID = 11;

    public Cow(FullChunk chunk, CompoundTag nbt) {
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
        return 0.45f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.7f;
        }
        return 1.4f;
    }

    @Override
    public float getEyeHeight() {
        if (this.isBaby()) {
            return 0.65f;
        }
        return 1.2f;
    }

    @Override
    public boolean isBaby() {
        return this.getDataFlag(DATA_FLAGS, Entity.DATA_FLAG_BABY);
    }

    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(10);
    }

    @Override
    public boolean onInteract(Player player, Item item) {
        if (item.equals(Item.get(Item.BUCKET,0),true)) {
            player.getInventory().removeItem(Item.get(Item.BUCKET,0,1));
            player.getInventory().addItem(Item.get(Item.BUCKET,1,1));
            this.level.addSound(new CowMilkSound(this), this.getViewers().values());
            return true;
        }else if(item.equals(Item.get(Item.WHEAT,0)) && !this.isBaby()){
            player.getInventory().removeItem(Item.get(Item.WHEAT,0,1));
            this.level.addParticle(new ItemBreakParticle(this.add(0,this.getMountedYOffset(),0),Item.get(Item.WHEAT)));
            this.setInLove();
        }
        return false;
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.isAlive() && !player.closed && player.getInventory().getItemInHand().getId() == Item.WHEAT && distance <= 49;
        }
        return false;
    }

    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {

            int leatherDropCount = Utils.rand(0, 3);
            int beefDrop = Utils.rand(1, 4);

            for (int i = 0; i < leatherDropCount; i++) {
                drops.add(Item.get(Item.LEATHER, 0, 1));
            }
            for (int i = 0; i < beefDrop; i++) {
                drops.add(Item.get(this.isOnFire() ? Item.STEAK : Item.RAW_BEEF, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }

    @Override
    public int getKillExperience() {
        return Utils.rand(1, 4);
    }

}
