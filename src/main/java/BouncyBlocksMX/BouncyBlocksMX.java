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

    private Map<String, Double> bouncyBlocks = new HashMap<>();
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
                String blockName = entry.getKey();
                double jumpPower = ((Number) entry.getValue()).doubleValue();
                bouncyBlocks.put(blockName.toLowerCase().replace(" ", "_"), jumpPower);
            } catch (NumberFormatException e) {
                this.getLogger().warning("Некорректный ID блока в конфиге: " + entry.getKey());
            } catch (ClassCastException e) {
                this.getLogger().warning("Некорректная сила прыжка для блока " + entry.getKey());
            }
        }
    }

    private boolean isBouncyBlock(Block block) {
        return bouncyBlocks.containsKey(block.getName().toLowerCase().replace(" ", "_"));
    }

    private double getJumpPower(Block block) {
        return bouncyBlocks.get(block.getName().toLowerCase().replace(" ", "_"));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        Block blockBelowFeet = location.subtract(0, 1, 0).getLevelBlock();
        Block blockBelowFeetTwo = location.subtract(0, 2, 0).getLevelBlock();
        Block blockBelowHead = location.subtract(0, 2, 0).getLevelBlock();

        if (isBouncyBlock(blockBelowFeet)) {
            if (!bounceOnWool || (bounceOnWool && blockBelowFeetTwo instanceof BlockWool)) {
                double jumpPower = getJumpPower(blockBelowFeet);
                player.setMotion(player.getMotion().add(0, jumpPower, 0));
            }
        } else if (isBouncyBlock(blockBelowHead)) {
            if (!bounceOnWool || (bounceOnWool && blockBelowFeet instanceof BlockWool)) {
                double jumpPower = getJumpPower(blockBelowHead);
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
