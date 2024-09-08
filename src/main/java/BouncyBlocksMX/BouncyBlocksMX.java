package BouncyBlocksMX;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import java.util.HashSet;
import java.util.Set;

public class BouncyBlocksMX extends PluginBase implements Listener {

    private BouncyBlockManager bouncyBlockManager;
    private Set<Player> jumpPlayers;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        Config config = this.getConfig();

        this.bouncyBlockManager = new BouncyBlockManager(config);
        this.jumpPlayers = new HashSet<>();

        this.getServer().getPluginManager().registerEvents(this, this);

        Server.getInstance().getScheduler().scheduleRepeatingTask(new BouncyTask(this.bouncyBlockManager, this.jumpPlayers), 10);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (bouncyBlockManager.getJumpPower(player.getLevel().getBlockIdAt(player.getFloorX(), player.getFloorY() - 1, player.getFloorZ())) != null) {
            jumpPlayers.add(player);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == DamageCause.FALL) {
            Player player = (Player) event.getEntity();
            if (jumpPlayers.contains(player)) {
                jumpPlayers.remove(player);
                event.setCancelled();
            }
        }
    }
}
