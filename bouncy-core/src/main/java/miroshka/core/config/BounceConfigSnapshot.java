package miroshka.core.config;

import java.util.Set;

public record BounceConfigSnapshot(BounceBlockLoadResult loadResult, Set<String> triggerBlocks) {
}
