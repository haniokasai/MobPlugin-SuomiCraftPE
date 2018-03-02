package suomicraftpe.mobplugin.entities;

import cn.nukkit.Player;

public interface Tameable {

    public static final String NAMED_TAG_OWNER      = "Owner";

    public static final String NAMED_TAG_OWNER_UUID = "OwnerUUID";

    public static final String NAMED_TAG_SITTING    = "Sitting";

    Player getOwner();

    void setOwner(Player player);

}
