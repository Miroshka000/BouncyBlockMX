package miroshka.nukkit;

import miroshka.core.config.BounceBlockRegistry;
import miroshka.core.service.BounceRules;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class NukkitBounceResolverTest {

    @Test
    void shouldResolveBounceWhenTriggerIsInCurrentBlock() {
        var resolver = new NukkitBounceResolver(new BounceRules(
                BounceBlockRegistry.load(Map.of("57", 3.0D)).registry(),
                List.of("70", "72")
        ));

        var context = resolver.resolve("70", "57", "1").orElseThrow();

        assertEquals("57", context.supportBlockKey());
        assertEquals(3.0D, context.jumpPower());
    }

    @Test
    void shouldResolveBounceWhenTriggerIsOneBlockLower() {
        var resolver = new NukkitBounceResolver(new BounceRules(
                BounceBlockRegistry.load(Map.of("57", 3.0D)).registry(),
                List.of("70", "72")
        ));

        var context = resolver.resolve("0", "70", "57").orElseThrow();

        assertEquals("57", context.supportBlockKey());
        assertEquals(3.0D, context.jumpPower());
    }

    @Test
    void shouldNotResolveWithoutValidTriggerSupportPair() {
        var resolver = new NukkitBounceResolver(new BounceRules(
                BounceBlockRegistry.load(Map.of("57", 3.0D)).registry(),
                List.of("70", "72")
        ));

        assertFalse(resolver.resolve("0", "57", "1").isPresent());
    }
}
