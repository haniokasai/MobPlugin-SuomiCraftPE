package suomicraftpe.mobplugin.entities;

import cn.nukkit.Player;

public interface Tameable {

    String NAMED_TAG_OWNER      = "Owner";

    String NAMED_TAG_OWNER_UUID = "OwnerUUID";

    String NAMED_TAG_SITTING    = "Sitting";

    Player getOwner();

    void setOwner(Player player);

    String getOwnerUUID();

    void setOwnerUUID(String uuid);

    boolean isSitting();

    void setSitting(boolean sitting);

}
