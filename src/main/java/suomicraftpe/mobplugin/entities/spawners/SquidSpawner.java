package suomicraftpe.mobplugin.entities.spawners;

import cn.nukkit.IPlayer;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.utils.Config;
import suomicraftpe.mobplugin.AutoSpawnTask;
import suomicraftpe.mobplugin.entities.animal.swimming.Squid;
import suomicraftpe.mobplugin.entities.autospawn.AbstractEntitySpawner;
import suomicraftpe.mobplugin.entities.autospawn.SpawnResult;

public class SquidSpawner extends AbstractEntitySpawner {

    public SquidSpawner(AutoSpawnTask spawnTask, Config pluginConfig) {
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
        int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);

        if (biomeId != Biome.OCEAN) {
            result = SpawnResult.WRONG_BLOCK;
        } else if (blockId != Block.WATER && blockId != Block.STILL_WATER) {
            result = SpawnResult.WRONG_BLOCK;
        } else {
            this.spawnTask.createEntity(getEntityName(), pos.add(0, 2.8, 0));
        }
        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return Squid.NETWORK_ID;
    }

    @Override
    public String getEntityName() {
        return "Squid";
    }
}
