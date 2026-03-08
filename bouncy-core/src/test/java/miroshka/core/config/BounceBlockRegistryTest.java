package miroshka.core.config;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BounceBlockRegistryTest {

    @Test
    void shouldParseValidEntriesAndCollectInvalidOnes() {
        var result = BounceBlockRegistry.load(Map.of(
                "minecraft:diamond_block", 3.0D,
                "  minecraft:gold_block  ", 2,
                "", 1.0D,
                "minecraft:iron_block", "bad"
        ));

        assertEquals(3.0D, result.registry().findJumpPower("minecraft:diamond_block").orElseThrow());
        assertEquals(2.0D, result.registry().findJumpPower("minecraft:gold_block").orElseThrow());
        assertFalse(result.registry().findJumpPower("minecraft:iron_block").isPresent());
        assertEquals(2, result.invalidEntries().size());
        assertTrue(result.invalidEntries().contains(""));
        assertTrue(result.invalidEntries().contains("minecraft:iron_block"));
    }
}
