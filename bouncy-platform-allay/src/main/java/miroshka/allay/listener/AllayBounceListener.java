package miroshka.allay.listener;

import miroshka.core.service.BounceRules;
import miroshka.core.session.BounceProtectionTracker;
import org.allaymc.api.entity.interfaces.EntityPlayer;
import org.allaymc.api.eventbus.EventHandler;
import org.allaymc.api.eventbus.event.entity.EntityFallEvent;
import org.allaymc.api.eventbus.event.player.PlayerMoveEvent;
import org.allaymc.api.eventbus.event.server.PlayerQuitEvent;
import org.allaymc.api.world.sound.SimpleSound;
import org.joml.Vector3d;

import java.util.UUID;

public final class AllayBounceListener {

    private final BounceRules bounceRules;
    private final BounceProtectionTracker<UUID> protectionTracker;

    public AllayBounceListener(BounceRules bounceRules, BounceProtectionTracker<UUID> protectionTracker) {
        this.bounceRules = bounceRules;
        this.protectionTracker = protectionTracker;
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        var player = event.getPlayer();
        var location = event.getTo();
        var blockX = (int) Math.floor(location.x());
        var blockY = (int) Math.floor(location.y());
        var blockZ = (int) Math.floor(location.z());
        var dimension = player.getDimension();
        var triggerBlockKey = dimension.getBlockState(blockX, blockY, blockZ).getBlockType().getIdentifier().toString();
        var supportBlockKey = dimension.getBlockState(blockX, blockY - 1, blockZ).getBlockType().getIdentifier().toString();

        if (this.bounceRules.shouldProtectFromFall(supportBlockKey)) {
            this.protectionTracker.markProtected(player.getUniqueId());
        }

        this.bounceRules.resolveBounceMotion(triggerBlockKey, supportBlockKey).ifPresent(jumpPower -> {
            this.protectionTracker.markProtected(player.getUniqueId());
            player.resetFallDistance();
            player.setMotion(player.getMotion().add(0.0, jumpPower, 0.0, new Vector3d()));
            dimension.addSound(location, SimpleSound.ANVIL_LAND);
        });
    }

    @EventHandler
    private void onEntityFall(EntityFallEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer player)) {
            return;
        }

        if (this.protectionTracker.consumeProtection(player.getUniqueId())) {
            player.resetFallDistance();
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        var entity = event.getPlayer().getControlledEntity();
        if (entity != null) {
            this.protectionTracker.clearProtection(entity.getUniqueId());
        }
    }
}
