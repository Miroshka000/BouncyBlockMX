package miroshka.core.service;

import miroshka.core.config.BounceBlockRegistry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BounceRulesTest {

    @Test
    void shouldReturnBouncePowerOnlyForSupportedTriggerBlocks() {
        var rules = new BounceRules(
                BounceBlockRegistry.load(Map.of("minecraft:diamond_block", 3.0D)).registry(),
                List.of("minecraft:stone_pressure_plate", "minecraft:wooden_pressure_plate")
        );

        assertEquals(3.0D, rules.resolveBounceMotion("minecraft:stone_pressure_plate", "minecraft:diamond_block").orElseThrow());
        assertFalse(rules.resolveBounceMotion("minecraft:air", "minecraft:diamond_block").isPresent());
        assertFalse(rules.resolveBounceMotion("minecraft:stone_pressure_plate", "minecraft:air").isPresent());
    }

    @Test
    void shouldMarkConfiguredBlocksAsFallProtected() {
        var rules = new BounceRules(
                BounceBlockRegistry.load(Map.of("minecraft:diamond_block", 3.0D)).registry(),
                List.of("minecraft:stone_pressure_plate")
        );

        assertTrue(rules.shouldProtectFromFall("minecraft:diamond_block"));
        assertFalse(rules.shouldProtectFromFall("minecraft:air"));
    }
}
