/**
 * MobPlugin.java
 * Special build for SuomiCraft PE
 * By PetteriM1
 */
package de.kniffo80.mobplugin;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerMouseOverEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.DyeColor;
import de.kniffo80.mobplugin.entities.BaseEntity;
import de.kniffo80.mobplugin.entities.animal.flying.*;
import de.kniffo80.mobplugin.entities.animal.swimming.Squid;
import de.kniffo80.mobplugin.entities.animal.walking.*;
import de.kniffo80.mobplugin.entities.block.BlockEntitySpawner;
import de.kniffo80.mobplugin.entities.monster.flying.*;
import de.kniffo80.mobplugin.entities.monster.walking.*;
import de.kniffo80.mobplugin.entities.projectile.EntityFireBall;
import de.kniffo80.mobplugin.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MobPlugin extends PluginBase implements Listener {

    public static boolean MOB_AI_ENABLED = false;

    private int           counter        = 0;

    private Config        pluginConfig   = null;

    @Override
    public void onLoad() {
        registerEntities();
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        pluginConfig = getConfig();

        MOB_AI_ENABLED = pluginConfig.getBoolean("entities.mob-ai", false);
        int spawnDelay = pluginConfig.getInt("entities.auto-spawn-tick", 0);

        this.getServer().getPluginManager().registerEvents(this, this);

        if (spawnDelay > 0) {
            this.getServer().getScheduler().scheduleRepeatingTask(new AutoSpawnTask(this), spawnDelay, true);
        }

        Utils.logServerInfo(String.format("Plugin enabled successful [aiEnabled:%s] [autoSpawnTick:%d]", MOB_AI_ENABLED, spawnDelay));
    }

    @Override
    public void onDisable() {
        Utils.logServerInfo("Plugin disabled successful.");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, cn.nukkit.command.Command cmd, String label, String[] args) {
        if (cmd.getName().toLowerCase().equals("mob")) {

        if (args.length == 0) {
            commandSender.sendMessage("-- MobPlugin 1.0 --");
            commandSender.sendMessage("/mob spawn <mob> <opt:player> - Spawn a mob");
            commandSender.sendMessage("/mob removeall - Remove all living mobs");
            commandSender.sendMessage("/mob removeitems - Remove all items from ground");
        } else {
            switch (args[0]) {

                case "spawn":
                    String mob = args[1];
                    Player playerThatSpawns = null;

                    if (args.length == 3) {
                        playerThatSpawns = this.getServer().getPlayer(args[2]);
                    } else {
                        playerThatSpawns = (Player) commandSender;
                    }

                    if (playerThatSpawns != null) {
                        Position pos = playerThatSpawns.getPosition();

                        Entity ent;
                        if ((ent = MobPlugin.create(mob, pos)) != null) {
                            ent.spawnToAll();
                            commandSender.sendMessage("Spawned " + mob + " to " + playerThatSpawns.getName());
                        } else {
                            commandSender.sendMessage("Unable to spawn " + mob);
                        }
                    } else {
                        commandSender.sendMessage("Unknown player " + (args.length == 3 ? args[2] : ((Player) commandSender).getName()));
                    }
                    break;
                case "removeall":
                    int count = 0;
                    for (Level level : getServer().getLevels().values()) {
                        for (Entity entity : level.getEntities()) {
                            if (entity instanceof BaseEntity && !entity.closed && entity.isAlive()) {
                                entity.close();
                                count++;
                            }
                        }
                    }
                    commandSender.sendMessage("Removed " + count + " entities from all levels.");
                    break;
                case "removeitems":
                    count = 0;
                    for (Level level : getServer().getLevels().values()) {
                        for (Entity entity : level.getEntities()) {
                            if (entity instanceof EntityItem && entity.isOnGround()) {
                                entity.close();
                                count++;
                            }
                        }
                    }
                    commandSender.sendMessage("Removed " + count + " items on ground from all levels.");
                    break;
                default:
                    commandSender.sendMessage("Unkown command.");
                    break;
                }
            }
        }
        return true;

    }

    public Config getPluginConfig() {
        return this.pluginConfig;
    }

    private void registerEntities() {
        Entity.registerEntity(Bat.class.getSimpleName(), Bat.class);
        Entity.registerEntity(Chicken.class.getSimpleName(), Chicken.class);
        Entity.registerEntity(Cow.class.getSimpleName(), Cow.class);
        Entity.registerEntity(Donkey.class.getSimpleName(), Donkey.class);
        Entity.registerEntity(Horse.class.getSimpleName(), Horse.class);
        Entity.registerEntity(Llama.class.getSimpleName(), Llama.class);
        Entity.registerEntity(Mooshroom.class.getSimpleName(), Mooshroom.class);
        Entity.registerEntity(Mule.class.getSimpleName(), Mule.class);
        Entity.registerEntity(Ocelot.class.getSimpleName(), Ocelot.class);
        Entity.registerEntity(Parrot.class.getSimpleName(), Parrot.class);
        Entity.registerEntity(Pig.class.getSimpleName(), Pig.class);
        Entity.registerEntity(PolarBear.class.getSimpleName(), PolarBear.class);
        Entity.registerEntity(Rabbit.class.getSimpleName(), Rabbit.class);
        Entity.registerEntity(Sheep.class.getSimpleName(), Sheep.class);
        Entity.registerEntity(SkeletonHorse.class.getSimpleName(), SkeletonHorse.class);
        Entity.registerEntity(Squid.class.getSimpleName(), Squid.class);
        Entity.registerEntity(Villager.class.getSimpleName(), Villager.class);
        Entity.registerEntity(ZombieHorse.class.getSimpleName(), ZombieHorse.class);

        Entity.registerEntity(Blaze.class.getSimpleName(), Blaze.class);
        Entity.registerEntity(Ghast.class.getSimpleName(), Ghast.class);
        Entity.registerEntity(CaveSpider.class.getSimpleName(), CaveSpider.class);
        Entity.registerEntity(Creeper.class.getSimpleName(), Creeper.class);
        Entity.registerEntity(EnderDragon.class.getSimpleName(), EnderDragon.class);
        Entity.registerEntity(Enderman.class.getSimpleName(), Enderman.class);
        Entity.registerEntity(Husk.class.getSimpleName(), Husk.class);
        Entity.registerEntity(IronGolem.class.getSimpleName(), IronGolem.class);
        Entity.registerEntity(PigZombie.class.getSimpleName(), PigZombie.class);
        Entity.registerEntity(Shulker.class.getSimpleName(), Shulker.class);
        Entity.registerEntity(Silverfish.class.getSimpleName(), Silverfish.class);
        Entity.registerEntity(Skeleton.class.getSimpleName(), Skeleton.class);
        Entity.registerEntity(SnowGolem.class.getSimpleName(), SnowGolem.class);
        Entity.registerEntity(Spider.class.getSimpleName(), Spider.class);
        Entity.registerEntity(Stray.class.getSimpleName(), Stray.class);
        Entity.registerEntity(Witch.class.getSimpleName(), Witch.class);
        Entity.registerEntity(Wither.class.getSimpleName(), Wither.class);
        Entity.registerEntity(WitherSkeleton.class.getSimpleName(), WitherSkeleton.class);
        Entity.registerEntity(Wolf.class.getSimpleName(), Wolf.class);
        Entity.registerEntity(Zombie.class.getSimpleName(), Zombie.class);
        Entity.registerEntity(ZombieVillager.class.getSimpleName(), ZombieVillager.class);

        Entity.registerEntity("FireBall", EntityFireBall.class);

        BlockEntity.registerBlockEntity("MobSpawner", BlockEntitySpawner.class);
    }

    public static Entity create(Object type, Position source, Object... args) {
        FullChunk chunk = source.getLevel().getChunk((int) source.x >> 4, (int) source.z >> 4, true);
        if (!chunk.isGenerated()) {
            chunk.setGenerated();
        }
        if (!chunk.isPopulated()) {
            chunk.setPopulated();
        }

        CompoundTag nbt = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", source.x)).add(new DoubleTag("", source.y)).add(new DoubleTag("", source.z)))
                .putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0)).add(new DoubleTag("", 0)).add(new DoubleTag("", 0)))
                .putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", source instanceof Location ? (float) ((Location) source).yaw : 0))
                        .add(new FloatTag("", source instanceof Location ? (float) ((Location) source).pitch : 0)));

        return Entity.createEntity(type.toString(), chunk, nbt, args);
    }

    public List<IPlayer> getAllRegisteredPlayers() {
        List<IPlayer> playerList = new ArrayList<>();
        for (Player player : this.getServer().getOnlinePlayers().values()) {
            playerList.add(player);
        }
        return playerList;
    }

    private boolean isPlayerAlreadyInList(String name, List<IPlayer> playerList) {
        for (IPlayer player : playerList) {
            if (player.getName().toLowerCase().equals(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void EntityDeathEvent(EntityDeathEvent ev) {
        if (ev.getEntity() instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) ev.getEntity();
            if (baseEntity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                Entity damager = ((EntityDamageByEntityEvent) baseEntity.getLastDamageCause()).getDamager();
                if (damager instanceof Player) {
                    Player player = (Player) damager;
                    int killExperience = baseEntity.getKillExperience();
                    if (killExperience > 0 && player != null && player.isSurvival()) {
                        player.addExperience(killExperience);
                    }
                }
            }
        }
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent ev) {
        if (ev.getFace() == null || ev.getAction() != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Item item = ev.getItem();
        Block block = ev.getBlock();
        if (item.getId() == Item.SPAWN_EGG && block.getId() == Item.MONSTER_SPAWNER) {
            ev.setCancelled(true);

            BlockEntity blockEntity = block.getLevel().getBlockEntity(block);
            if (blockEntity != null && blockEntity instanceof BlockEntitySpawner) {
                ((BlockEntitySpawner) blockEntity).setSpawnEntityType(item.getDamage());
            } else {
                if (blockEntity != null) {
                    blockEntity.close();
                }
                CompoundTag nbt = new CompoundTag().putString("id", BlockEntity.MOB_SPAWNER).putInt("EntityId", item.getDamage()).putInt("x", (int) block.x).putInt("y", (int) block.y).putInt("z",
                        (int) block.z);

                new BlockEntitySpawner(block.getLevel().getChunk((int) block.x >> 4, (int) block.z >> 4), nbt);
            }
        }
    }

    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent ev) {
        if (ev.isCancelled()) {
            return;
        }

        Block block = ev.getBlock();
        if (block.getId() == Item.JACK_O_LANTERN || block.getId() == Item.PUMPKIN) {
            if (block.getSide(BlockFace.DOWN).getId() == Item.SNOW_BLOCK && block.getSide(BlockFace.DOWN, 2).getId() == Item.SNOW_BLOCK) {
                Entity entity = create("SnowGolem", block.add(0.5, -2, 0.5));
                if (entity != null) {
                    entity.spawnToAll();
                }

                ev.setCancelled();
                block.getLevel().setBlock(block.add(0, -1, 0), new BlockAir());
                block.getLevel().setBlock(block.add(0, -2, 0), new BlockAir());
            } else if (block.getSide(BlockFace.DOWN).getId() == Item.IRON_BLOCK && block.getSide(BlockFace.DOWN, 2).getId() == Item.IRON_BLOCK) {
                block = block.getSide(BlockFace.DOWN);

                Block first, second = null;
                if ((first = block.getSide(BlockFace.EAST)).getId() == Item.IRON_BLOCK && (second = block.getSide(BlockFace.WEST)).getId() == Item.IRON_BLOCK) {
                    block.getLevel().setBlock(first, new BlockAir());
                    block.getLevel().setBlock(second, new BlockAir());
                } else if ((first = block.getSide(BlockFace.NORTH)).getId() == Item.IRON_BLOCK && (second = block.getSide(BlockFace.SOUTH)).getId() == Item.IRON_BLOCK) {
                    block.getLevel().setBlock(first, new BlockAir());
                    block.getLevel().setBlock(second, new BlockAir());
                }

                if (second != null) {
                    Entity entity = MobPlugin.create("IronGolem", block.add(0.5, -1, 0.5));
                    if (entity != null) {
                        entity.spawnToAll();
                    }
                    block.getLevel().setBlock(block, new BlockAir());
                    block.getLevel().setBlock(block.add(0, -1, 0), new BlockAir());
                    ev.setCancelled();
                }
            }
        }
    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent ev) {
        if (ev.isCancelled()) {
            return;
        }

        Block block = ev.getBlock();
        if ((block.getId() == Block.MONSTER_EGG)
            && block.getLevel().getBlockLightAt((int) block.x, (int) block.y, (int) block.z) < 12 && Utils.rand(1, 5) == 1) {

              Silverfish entity = (Silverfish) create("Silverfish", block.add(0.5, 0, 0.5)); if(entity != null){ entity.spawnToAll(); }
        }
    }

    @EventHandler
    public void PlayerMouseOverEntityEvent(PlayerMouseOverEntityEvent ev) {
        if (this.counter > 10) {
            counter = 0;
            if (ev != null && ev.getEntity() != null && ev.getPlayer() != null && ev.getEntity().getNetworkId() == Wolf.NETWORK_ID && ev.getPlayer().getInventory().getItemInHand().getId() == Item.BONE) {
                Wolf wolf = (Wolf)ev.getEntity();
                if (!wolf.isAngry() && wolf.getOwner() == null) {
                    EntityEventPacket packet = new EntityEventPacket();
                    packet.eid = ev.getEntity().getId();
                    packet.event = EntityEventPacket.TAME_SUCCESS;
                    Server.broadcastPacket(new Player[] { ev.getPlayer() }, packet);

                    wolf.setOwner(ev.getPlayer());
                    wolf.setCollarColor(DyeColor.BLUE);
                    wolf.saveNBT();
                }
            }
        } else {
            counter++;
        }
    }
}
