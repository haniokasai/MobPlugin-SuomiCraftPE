package suomicraftpe.mobplugin.entities.spawners;

import cn.nukkit.IPlayer;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.utils.Config;
import suomicraftpe.mobplugin.AutoSpawnTask;
import suomicraftpe.mobplugin.entities.autospawn.AbstractEntitySpawner;
import suomicraftpe.mobplugin.entities.autospawn.SpawnResult;
import suomicraftpe.mobplugin.entities.monster.flying.Blaze;

public class BlazeSpawner extends AbstractEntitySpawner {

    public BlazeSpawner(AutoSpawnTask spawnTask, Config pluginConfig) {
        super(spawnTask, pluginConfig);
    }

    @Override
    protected String getLogprefix() {
        return this.getClass().getSimpleName();
    }

    @Override
    public SpawnResult spawn(IPlayer iPlayer, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);

        if (biomeId != EnumBiome.HELL.biome.getId()) {
            result = SpawnResult.WRONG_BLOCK;
        } else {
            this.spawnTask.createEntity(getEntityName(), pos.add(0, 2.3, 0));
        }

        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return Blaze.NETWORK_ID;
    }

    @Override
    public String getEntityName() {
        return "Blaze";
    }
}
