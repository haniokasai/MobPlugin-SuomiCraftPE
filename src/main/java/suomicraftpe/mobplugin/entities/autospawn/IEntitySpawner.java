package suomicraftpe.mobplugin.entities.autospawn;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

import java.util.List;

public interface IEntitySpawner {

    void spawn(List<Player> onlinePlayers);

    SpawnResult spawn(IPlayer iPlayer, Position pos, Level level);

    int getEntityNetworkId();

    String getEntityName();

}
