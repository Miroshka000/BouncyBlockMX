package miroshka.nukkit;

import miroshka.core.service.BounceRules;

import java.util.Optional;

public final class NukkitBounceResolver {

    private final BounceRules bounceRules;

    public NukkitBounceResolver(BounceRules bounceRules) {
        this.bounceRules = bounceRules;
    }

    public Optional<NukkitBounceContext> resolve(String currentBlockKey, String belowBlockKey, String twoBelowBlockKey) {
        var directBounce = this.bounceRules.resolveBounceMotion(currentBlockKey, belowBlockKey);
        if (directBounce.isPresent()) {
            return Optional.of(new NukkitBounceContext(belowBlockKey, directBounce.getAsDouble()));
        }

        var shiftedBounce = this.bounceRules.resolveBounceMotion(belowBlockKey, twoBelowBlockKey);
        if (shiftedBounce.isPresent()) {
            return Optional.of(new NukkitBounceContext(twoBelowBlockKey, shiftedBounce.getAsDouble()));
        }

        return Optional.empty();
    }
}
