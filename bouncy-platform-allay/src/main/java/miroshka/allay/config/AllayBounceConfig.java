package miroshka.allay.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import miroshka.core.config.BounceBlockLoadResult;
import miroshka.core.config.BounceBlockRegistry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Header("BouncyBlocksMX Allay config")
public class AllayBounceConfig extends OkaeriConfig {

    @Comment("Allay block identifiers used as bounce triggers")
    private List<String> triggerBlocks = new ArrayList<>(List.of(
            "minecraft:stone_pressure_plate",
            "minecraft:wooden_pressure_plate"
    ));

    @Comment("Support block identifier -> jump power")
    private Map<String, Double> bouncyBlocks = new LinkedHashMap<>(Map.of(
            "minecraft:diamond_block", 3.0D,
            "minecraft:gold_block", 2.0D,
            "minecraft:iron_block", 1.0D
    ));

    public List<String> getTriggerBlocks() {
        return this.triggerBlocks;
    }

    public Map<String, Double> getBouncyBlocks() {
        return this.bouncyBlocks;
    }

    public BounceBlockLoadResult toLoadResult() {
        return BounceBlockRegistry.load(new LinkedHashMap<>(this.bouncyBlocks));
    }
}
