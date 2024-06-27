package BouncyBlocksMX;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.level.Location;
import cn.nukkit.block.BlockWool;

import java.util.HashMap;
import java.util.Map;

public class BouncyBlocksMX extends PluginBase implements Listener {

    private Map<Integer, Double> bouncyBlocks = new HashMap<>();
    private Config config;
    private boolean bounceOnWool;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.config = this.getConfig();
        this.bounceOnWool = config.getBoolean("bounce_on_wool", true);

        this.getServer().getPluginManager().registerEvents(this, this);

        loadBouncyBlocks();
    }

    private void loadBouncyBlocks() {
        Map<String, Object> blocks = config.getSection("BouncyBlocks").getAllMap();
        for (Map.Entry<String, Object> entry : blocks.entrySet()) {
            try {
                int blockId = Integer.parseInt(entry.getKey());
                double jumpPower = ((Number) entry.getValue()).doubleValue();
                bouncyBlocks.put(blockId, jumpPower);
            } catch (NumberFormatException e) {
                this.getLogger().warning("Некорректный ID блока в конфиге: " + entry.getKey());
            } catch (ClassCastException e) {
                this.getLogger().warning("Некорректная сила прыжка для блока " + entry.getKey());
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        Block blockBelowFeet = location.subtract(0, 1, 0).getLevelBlock();
        Block blockBelowFeetTwo = location.subtract(0, 2, 0).getLevelBlock();
        Block blockBelowHead = location.subtract(0, 2, 0).getLevelBlock();

        if (bouncyBlocks.containsKey(blockBelowFeet.getId())) {
            if (!bounceOnWool || (bounceOnWool && blockBelowFeetTwo instanceof BlockWool)) {
                double jumpPower = bouncyBlocks.get(blockBelowFeet.getId());
                player.setMotion(player.getMotion().add(0, jumpPower, 0));
            }
        } else if (bouncyBlocks.containsKey(blockBelowHead.getId())) {
            if (!bounceOnWool || (bounceOnWool && blockBelowFeet instanceof BlockWool)) {
                double jumpPower = bouncyBlocks.get(blockBelowHead.getId());
                player.setMotion(player.getMotion().add(0, jumpPower, 0));
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled();
        }
    }
}
