package BouncyBlocksMX;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.Task;

import java.util.Set;
import java.util.Iterator;

public class BouncyTask extends Task {

    private final BouncyBlockManager bouncyBlockManager;
    private final Set<Player> jumpPlayers;

    public BouncyTask(BouncyBlockManager bouncyBlockManager, Set<Player> jumpPlayers) {
        this.bouncyBlockManager = bouncyBlockManager;
        this.jumpPlayers = jumpPlayers;
    }

    @Override
    public void onRun(int i) {
        Iterator<Player> players = Server.getInstance().getOnlinePlayers().values().iterator();

        while (players.hasNext()) {
            Player player = players.next();
            int blockId = player.getLevel().getBlockIdAt(player.getFloorX(), player.getFloorY(), player.getFloorZ());
            if (blockId == 70 || blockId == 72) {
                int blockIdBelow = player.getLevel().getBlockIdAt(player.getFloorX(), player.getFloorY() - 1, player.getFloorZ());
                Double motion = bouncyBlockManager.getJumpPower(blockIdBelow);

                if (motion != null) {
                    jumpPlayers.add(player);

                    player.setMotion(player.getMotion().add(0.0, motion, 0.0));
                    player.getLevel().addSound(player, Sound.RANDOM_ANVIL_LAND, 1.0F, 1.0F, new Player[]{player});
                }
            }
        }
    }
}
