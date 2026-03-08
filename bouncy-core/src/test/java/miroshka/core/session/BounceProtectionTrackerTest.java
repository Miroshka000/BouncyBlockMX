package miroshka.core.session;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BounceProtectionTrackerTest {

    @Test
    void shouldConsumeProtectionOnlyOnce() {
        var tracker = new BounceProtectionTracker<String>();

        tracker.markProtected("player");

        assertTrue(tracker.consumeProtection("player"));
        assertFalse(tracker.consumeProtection("player"));
    }
}
