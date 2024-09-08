package BouncyBlocksMX;

import cn.nukkit.utils.Config;

import java.util.Map;
import java.util.HashMap;

public class BouncyBlockManager {

    private final Map<Integer, Double> bouncyBlocks;

    public BouncyBlockManager(Config config) {
        this.bouncyBlocks = new HashMap<>();
        loadBouncyBlocks(config);
    }

    private void loadBouncyBlocks(Config config) {
        Map<String, Object> blocks = config.getSection("BouncyBlocks").getAllMap();
        for (Map.Entry<String, Object> entry : blocks.entrySet()) {
            try {
                int blockId = Integer.parseInt(entry.getKey());
                double jumpPower = ((Number) entry.getValue()).doubleValue();
                bouncyBlocks.put(blockId, jumpPower);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ID блока: " + entry.getKey());
            } catch (ClassCastException e) {
                System.out.println("Некорректная сила прыжка для блока: " + entry.getKey());
            }
        }
    }

    public Double getJumpPower(int blockId) {
        return bouncyBlocks.get(blockId);
    }
}
