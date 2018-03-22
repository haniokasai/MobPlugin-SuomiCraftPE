package suomicraftpe.mobplugin.entities.spawners;

import cn.nukkit.IPlayer;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.utils.Config;
import suomicraftpe.mobplugin.AutoSpawnTask;
import suomicraftpe.mobplugin.entities.autospawn.AbstractEntitySpawner;
import suomicraftpe.mobplugin.entities.autospawn.SpawnResult;
import suomicraftpe.mobplugin.entities.monster.walking.Enderman;
import suomicraftpe.mobplugin.utils.Utils;

public class EndermanSpawner extends AbstractEntitySpawner {

    public EndermanSpawner(AutoSpawnTask spawnTask, Config pluginConfig) {
        super(spawnTask, pluginConfig);
    }

    public SpawnResult spawn(IPlayer iPlayer, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        if (Utils.rand(0, 3) > 0) {
            return SpawnResult.SPAWN_DENIED;
        }

        int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
        int time = level.getTime() % Level.TIME_FULL;

        if (pos.y > 127 || pos.y < 1 || level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) == Block.AIR) {
            result = SpawnResult.POSITION_MISMATCH;
        } else if (time > 13184 && time < 22800) {
            this.spawnTask.createEntity(getEntityName(), pos.add(0, 3.8, 0));
        }

        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return Enderman.NETWORK_ID;
    }

    @Override
    public String getEntityName() {
        return "Enderman";
    }

    @Override
    protected String getLogprefix() {
        return this.getClass().getSimpleName();
    }

}
