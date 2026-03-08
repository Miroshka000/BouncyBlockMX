package miroshka.core.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.OptionalDouble;

public final class BounceBlockRegistry {

    private final Map<String, Double> jumpPowers;

    private BounceBlockRegistry(Map<String, Double> jumpPowers) {
        this.jumpPowers = Map.copyOf(jumpPowers);
    }

    public static BounceBlockLoadResult load(Map<String, ?> rawBlocks) {
        var parsedBlocks = new LinkedHashMap<String, Double>();
        var invalidEntries = new java.util.ArrayList<String>();

        rawBlocks.forEach((rawBlockId, rawJumpPower) -> {
            if (!(rawJumpPower instanceof Number number)) {
                invalidEntries.add(rawBlockId);
                return;
            }

            var normalizedBlockKey = normalizeKey(rawBlockId);
            if (normalizedBlockKey.isBlank()) {
                invalidEntries.add(rawBlockId);
                return;
            }

            parsedBlocks.put(normalizedBlockKey, number.doubleValue());
        });

        return new BounceBlockLoadResult(new BounceBlockRegistry(parsedBlocks), List.copyOf(invalidEntries));
    }

    public boolean contains(String blockKey) {
        return this.jumpPowers.containsKey(normalizeKey(blockKey));
    }

    public OptionalDouble findJumpPower(String blockKey) {
        var jumpPower = this.jumpPowers.get(normalizeKey(blockKey));
        return jumpPower == null ? OptionalDouble.empty() : OptionalDouble.of(jumpPower);
    }

    public static String normalizeKey(String blockKey) {
        return blockKey == null ? "" : blockKey.trim().toLowerCase(Locale.ROOT);
    }
}
