package miroshka.nukkit;

import miroshka.core.config.BounceBlockRegistry;
import miroshka.core.config.BounceConfigSnapshot;
import cn.nukkit.utils.Config;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public final class NukkitBounceBlockLoader {

    private static final List<String> DEFAULT_TRIGGER_BLOCKS = List.of("70", "72");

    public BounceConfigSnapshot load(Config config) {
        var blocksSection = config.getSection("BouncyBlocks");
        Map<String, Object> blocks = blocksSection == null ? Collections.emptyMap() : blocksSection.getAllMap();
        var triggerBlocks = new LinkedHashSet<>(config.getStringList("TriggerBlocks"));
        if (triggerBlocks.isEmpty() && config.get("TriggerBlocks") == null) {
            triggerBlocks.addAll(DEFAULT_TRIGGER_BLOCKS);
        }
        return new BounceConfigSnapshot(BounceBlockRegistry.load(blocks), triggerBlocks);
    }
}
