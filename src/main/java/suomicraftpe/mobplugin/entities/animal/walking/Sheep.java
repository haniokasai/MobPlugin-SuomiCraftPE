package suomicraftpe.mobplugin.entities.animal.walking;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDye;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.DyeColor;
import suomicraftpe.mobplugin.entities.animal.WalkingAnimal;
import suomicraftpe.mobplugin.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Sheep extends WalkingAnimal {

    public static final int NETWORK_ID = 13;

    public boolean sheared = false;
    public int color = 0;

    public Sheep(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (isBaby()) {
            return 0.65f;
        }
        return 1.3f;
    }

    @Override
    public float getEyeHeight() {
        if (isBaby()) {
            return 0.65f;
        }
        return 1.1f;
    }

    @Override
    public boolean isBaby() {
        return this.getDataFlag(DATA_FLAGS, Entity.DATA_FLAG_BABY);
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(8);

        if (!this.namedTag.contains("Color")) {
            this.setColor(randomColor());
        } else {
            this.setColor(this.namedTag.getByte("Color"));
        }

        if (!this.namedTag.contains("Sheared")) {
            this.namedTag.putByte("Sheared", 0);
        } else {
            this.sheared = this.namedTag.getBoolean("Sheared");
        }

        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SHEARED, this.sheared);
    }

    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putByte("Color", this.color);
        this.namedTag.putBoolean("Sheared", this.sheared);
    }

    @Override
    public boolean onInteract(Player player, Item item) {
        if (item.getId() == Item.DYE) {
            this.setColor(((ItemDye) item).getDyeColor().getWoolData());;
            return true;
        }else if(item.equals(Item.get(Item.WHEAT,0,1)) && !this.isBaby()){
            player.getInventory().removeItem(Item.get(Item.WHEAT,0,1));
            this.level.addSound(this.getLocation(), Sound.MOB_SHEEP_SAY);
            this.level.addParticle(new ItemBreakParticle(this.add(0,this.getMountedYOffset(),0),Item.get(Item.WHEAT)));
            this.setInLove();
            return true;
        }else if(item.equals(Item.get(Item.SHEARS,0,1),false) && !isBaby() && !this.sheared){
            this.shear();
            this.level.addSound(this.getLocation(), Sound.MOB_SHEEP_SHEAR);
            player.getInventory().getItemInHand().setDamage(item.getDamage() + 1);
            return true;
        }
        return false;

    }

    public void shear() {
        this.sheared = true;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SHEARED, true);
        this.level.dropItem(this, Item.get(Item.WOOL, getColor(), Utils.rand(0,4)));
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed && player.getInventory().getItemInHand().getId() == Item.WHEAT && distance <= 40;
        }
        return false;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            drops.add(Item.get(Item.WOOL, this.namedTag.getByte("Color"), 1));
            int muttonDrop = Utils.rand(1, 3);
            for (int i = 0; i < muttonDrop; i++) {
                drops.add(Item.get(this.isOnFire() ? Item.COOKED_MUTTON : Item.RAW_MUTTON, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }


    public void setColor(int color) {
        this.color = color;
        this.namedTag.putByte("Color",color);
        this.setDataProperty(new ByteEntityData(DATA_COLOUR, color));
    }

    public int getColor() {
        return namedTag.getByte("Color");
    }

    private int randomColor() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int rand = random.nextInt(0, 2500);

        if(rand < 125 && 0 <= rand)return DyeColor.BLACK.getDyeData();
        else if(rand < 250 && 125 <= rand)return DyeColor.GRAY.getDyeData();
        else if(rand < 375 && 250 <= rand)return DyeColor.LIGHT_GRAY.getDyeData();
        else if(rand < 500 && 375 <= rand)return DyeColor.GRAY.getDyeData();
        else if(rand < 541 && 500 <= rand)return DyeColor.PINK.getDyeData();
        else return DyeColor.WHITE.getDyeData();
    }

    @Override
    public int getKillExperience() {
        return Utils.rand(1, 4);
    }

}
