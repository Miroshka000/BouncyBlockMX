package miroshka.core.service;

import miroshka.core.config.BounceBlockRegistry;

import java.util.Collection;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

public final class BounceRules {

    private final BounceBlockRegistry registry;
    private final Set<String> triggerBlocks;

    public BounceRules(BounceBlockRegistry registry, Collection<String> triggerBlocks) {
        this.registry = registry;
        this.triggerBlocks = triggerBlocks.stream()
                .map(BounceBlockRegistry::normalizeKey)
                .filter(key -> !key.isBlank())
                .collect(Collectors.toUnmodifiableSet());
    }

    public OptionalDouble resolveBounceMotion(String triggerBlockKey, String supportBlockKey) {
        if (!this.triggerBlocks.contains(BounceBlockRegistry.normalizeKey(triggerBlockKey))) {
            return OptionalDouble.empty();
        }

        return this.registry.findJumpPower(supportBlockKey);
    }

    public boolean shouldProtectFromFall(String supportBlockKey) {
        return this.registry.contains(supportBlockKey);
    }
}
