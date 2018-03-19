package suomicraftpe.mobplugin.entities.spawners;

import cn.nukkit.IPlayer;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import suomicraftpe.mobplugin.AutoSpawnTask;
import suomicraftpe.mobplugin.entities.animal.walking.Ocelot;
import suomicraftpe.mobplugin.entities.autospawn.AbstractEntitySpawner;
import suomicraftpe.mobplugin.entities.autospawn.SpawnResult;
import suomicraftpe.mobplugin.utils.Utils;

public class OcelotSpawner extends AbstractEntitySpawner {

    public OcelotSpawner(AutoSpawnTask spawnTask, Config pluginConfig) {
        super(spawnTask, pluginConfig);
    }

    public SpawnResult spawn(IPlayer iPlayer, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        if (Utils.rand(0, 3) == 0) {
            return SpawnResult.SPAWN_DENIED;
        }

        int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);

        if (blockId != Block.GRASS && blockId != Block.LEAVE) {
            result = SpawnResult.WRONG_BLOCK;
        } else if (pos.y > 127 || pos.y < 1 || level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) == Block.AIR) {
            result = SpawnResult.POSITION_MISMATCH;
        } else {
            this.spawnTask.createEntity(getEntityName(), pos.add(0, 1.9, 0));
        }

        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return Ocelot.NETWORK_ID;
    }

    @Override
    public String getEntityName() {
        return "Ocelot";
    }

    @Override
    protected String getLogprefix() {
        return this.getClass().getSimpleName();
    }

}
