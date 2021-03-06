package suomicraftpe.mobplugin.entities.spawners;

import cn.nukkit.IPlayer;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.utils.Config;
import suomicraftpe.mobplugin.AutoSpawnTask;
import suomicraftpe.mobplugin.entities.animal.flying.Bat;
import suomicraftpe.mobplugin.entities.autospawn.AbstractEntitySpawner;
import suomicraftpe.mobplugin.entities.autospawn.SpawnResult;

public class BatSpawner extends AbstractEntitySpawner {

    public BatSpawner(AutoSpawnTask spawnTask, Config pluginConfig) {
        super(spawnTask, pluginConfig);
    }

    public SpawnResult spawn(IPlayer iPlayer, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);

        if (biomeId == EnumBiome.HELL.biome.getId()) {
            result = SpawnResult.WRONG_BLOCK;
        } else {
            this.spawnTask.createEntity(getEntityName(), pos.add(0, 1.3, 0));
        }

        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return Bat.NETWORK_ID;
    }

    @Override
    public String getEntityName() {
        return "Bat";
    }

    @Override
    protected String getLogprefix() {
        return this.getClass().getSimpleName();
    }

}
