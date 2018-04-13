package suomicraftpe.mobplugin.entities.spawners;

import cn.nukkit.IPlayer;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.utils.Config;
import suomicraftpe.mobplugin.AutoSpawnTask;
import suomicraftpe.mobplugin.entities.autospawn.AbstractEntitySpawner;
import suomicraftpe.mobplugin.entities.autospawn.SpawnResult;
import suomicraftpe.mobplugin.entities.monster.jumping.Slime;

public class SlimeSpawner extends AbstractEntitySpawner {

    public SlimeSpawner(AutoSpawnTask spawnTask, Config pluginConfig) {
        super(spawnTask, pluginConfig);
    }

    public SpawnResult spawn(IPlayer iPlayer, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
        int time = level.getTime() % Level.TIME_FULL;

        if (biomeId != EnumBiome.SWAMP.biome.getId()) {
            result = SpawnResult.WRONG_BLOCK;
        } else if (pos.y > 127 || pos.y < 1 || level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) == Block.AIR) {
            result = SpawnResult.POSITION_MISMATCH;
        } else if (time > 13184 && time < 22800) {
            this.spawnTask.createEntity(getEntityName(), pos.add(0, 2.12, 0));
        }

        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return Slime.NETWORK_ID;
    }

    @Override
    public String getEntityName() {
        return "Slime";
    }

    @Override
    protected String getLogprefix() {
        return this.getClass().getSimpleName();
    }
}
