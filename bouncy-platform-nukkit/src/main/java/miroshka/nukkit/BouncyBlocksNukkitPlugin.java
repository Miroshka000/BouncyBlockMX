package miroshka.nukkit;

import miroshka.core.service.BounceRules;
import miroshka.core.session.BounceProtectionTracker;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;

import java.util.UUID;

public final class BouncyBlocksNukkitPlugin extends PluginBase implements Listener {

    private BounceRules bounceRules;
    private BounceProtectionTracker<UUID> protectionTracker;
    private NukkitBounceActivationTracker<UUID> activationTracker;
    private NukkitBounceResolver bounceResolver;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        var snapshot = new NukkitBounceBlockLoader().load(this.getConfig());
        this.bounceRules = new BounceRules(snapshot.loadResult().registry(), snapshot.triggerBlocks());
        this.protectionTracker = new BounceProtectionTracker<>();
        this.activationTracker = new NukkitBounceActivationTracker<>();
        this.bounceResolver = new NukkitBounceResolver(this.bounceRules);

        snapshot.loadResult().invalidEntries().forEach(entry -> this.getLogger().warning("Invalid bouncy block entry: " + entry));
        if (this.getConfig().get("TriggerBlocks") == null) {
            this.getLogger().warning("TriggerBlocks section is missing, fallback defaults 70 and 72 are used.");
        }

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        var player = event.getPlayer();
        var currentBlockKey = String.valueOf(player.getLevel().getBlockIdAt(player.getFloorX(), player.getFloorY(), player.getFloorZ()));
        var belowBlockKey = String.valueOf(player.getLevel().getBlockIdAt(player.getFloorX(), player.getFloorY() - 1, player.getFloorZ()));
        var twoBelowBlockKey = String.valueOf(player.getLevel().getBlockIdAt(player.getFloorX(), player.getFloorY() - 2, player.getFloorZ()));
        var context = this.bounceResolver.resolve(currentBlockKey, belowBlockKey, twoBelowBlockKey);

        if (context.isEmpty()) {
            this.activationTracker.deactivate(player.getUniqueId());
            return;
        }

        this.protectionTracker.markProtected(player.getUniqueId());
        if (!this.activationTracker.activate(player.getUniqueId())) {
            return;
        }

        var bounceContext = context.orElseThrow();
        player.setMotion(player.getMotion().add(0.0, bounceContext.jumpPower(), 0.0));
        player.getLevel().addSound(player, cn.nukkit.level.Sound.RANDOM_ANVIL_LAND, 1.0F, 1.0F, new Player[]{player});
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player) || event.getCause() != DamageCause.FALL) {
            return;
        }

        if (this.protectionTracker.consumeProtection(player.getUniqueId())) {
            event.setCancelled();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.protectionTracker.clearProtection(event.getPlayer().getUniqueId());
        this.activationTracker.clear(event.getPlayer().getUniqueId());
    }
}
