package miroshka.core.config;

import java.util.List;

public record BounceBlockLoadResult(BounceBlockRegistry registry, List<String> invalidEntries) {
}
