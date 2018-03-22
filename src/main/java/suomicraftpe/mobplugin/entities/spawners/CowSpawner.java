package suomicraftpe.mobplugin.entities.spawners;

import cn.nukkit.IPlayer;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.utils.Config;
import suomicraftpe.mobplugin.AutoSpawnTask;
import suomicraftpe.mobplugin.entities.animal.walking.Cow;
import suomicraftpe.mobplugin.entities.autospawn.AbstractEntitySpawner;
import suomicraftpe.mobplugin.entities.autospawn.SpawnResult;

public class CowSpawner extends AbstractEntitySpawner {

    public CowSpawner(AutoSpawnTask spawnTask, Config pluginConfig) {
        super(spawnTask, pluginConfig);
    }

    public SpawnResult spawn(IPlayer iPlayer, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);

        if (blockId != Block.GRASS) {
            result = SpawnResult.WRONG_BLOCK;
        } else if (pos.y > 127 || pos.y < 1 || level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) == Block.AIR) {
            result = SpawnResult.POSITION_MISMATCH;
        } else if (biomeId == Biome.HELL) {
            result = SpawnResult.WRONG_BLOCK;
        } else {
            this.spawnTask.createEntity(getEntityName(), pos.add(0, 2.3, 0));
        }

        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return Cow.NETWORK_ID;
    }

    @Override
    public String getEntityName() {
        return "Cow";
    }

    @Override
    protected String getLogprefix() {
        return this.getClass().getSimpleName();
    }

}
