package miroshka.nukkit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NukkitBounceActivationTrackerTest {

    @Test
    void shouldActivateOnlyOnceUntilDeactivate() {
        var tracker = new NukkitBounceActivationTracker<String>();

        assertTrue(tracker.activate("player"));
        assertFalse(tracker.activate("player"));

        tracker.deactivate("player");

        assertTrue(tracker.activate("player"));
    }
}
