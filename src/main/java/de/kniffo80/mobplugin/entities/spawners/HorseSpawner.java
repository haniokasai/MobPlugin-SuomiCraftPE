package de.kniffo80.mobplugin.entities.spawners;

import cn.nukkit.IPlayer;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import de.kniffo80.mobplugin.AutoSpawnTask;
import de.kniffo80.mobplugin.entities.animal.walking.Horse;
import de.kniffo80.mobplugin.entities.autospawn.AbstractEntitySpawner;
import de.kniffo80.mobplugin.entities.autospawn.SpawnResult;

/**
 * @author PikyCZ
 */
public class HorseSpawner extends AbstractEntitySpawner {

    public HorseSpawner(AutoSpawnTask spawnTask, Config pluginConfig) {
        super(spawnTask, pluginConfig);
    }

    @Override
    protected String getLogprefix() {
        return this.getClass().getSimpleName();
    }

    @Override
    public SpawnResult spawn(IPlayer iPlayer, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        int blockLightLevel = level.getBlockLightAt((int) pos.x, (int) pos.y, (int) pos.z);

        if (!Block.solid[blockId]) { // only spawns on solid blocks
            result = SpawnResult.WRONG_BLOCK;
        } else if (blockLightLevel > 9) {
            result = SpawnResult.WRONG_LIGHTLEVEL;
        } else if (pos.y > 127 || pos.y < 1 || level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) == Block.AIR) { // cannot spawn on AIR block
            result = SpawnResult.POSITION_MISMATCH;
        } else { // horse is spawned
            this.spawnTask.createEntity(getEntityName(), pos.add(0, 2.8, 0));
        }

        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return Horse.NETWORK_ID;
    }

    @Override
    public String getEntityName() {
        return "Horse";
    }

}
