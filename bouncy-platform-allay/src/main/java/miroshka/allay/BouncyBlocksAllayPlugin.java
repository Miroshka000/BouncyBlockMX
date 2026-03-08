package miroshka.allay;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import miroshka.allay.config.AllayBounceConfig;
import miroshka.allay.listener.AllayBounceListener;
import miroshka.core.service.BounceRules;
import miroshka.core.session.BounceProtectionTracker;
import org.allaymc.api.server.Server;
import org.allaymc.api.plugin.Plugin;

import java.nio.file.Files;
import java.util.UUID;

public final class BouncyBlocksAllayPlugin extends Plugin {

    private BounceRules bounceRules;
    private BounceProtectionTracker<UUID> protectionTracker;
    private AllayBounceListener listener;
    private AllayBounceConfig config;

    @Override
    public void onLoad() {
        this.pluginLogger.info("BouncyBlocksMX allay module loaded");
    }

    @Override
    public void onEnable() {
        this.config = this.loadConfig();
        this.bounceRules = new BounceRules(this.config.toLoadResult().registry(), this.config.getTriggerBlocks());
        this.protectionTracker = new BounceProtectionTracker<>();
        this.listener = new AllayBounceListener(this.bounceRules, this.protectionTracker);

        this.config.toLoadResult().invalidEntries().forEach(entry -> this.pluginLogger.warn("Invalid bouncy block entry: {}", entry));

        Server.getInstance().getEventBus().registerListener(this.listener);
        this.pluginLogger.info("BouncyBlocksMX allay module enabled");
    }

    @Override
    public void onDisable() {
        if (this.listener != null) {
            Server.getInstance().getEventBus().unregisterListener(this.listener);
        }
        this.pluginLogger.info("BouncyBlocksMX allay module disabled");
    }

    private AllayBounceConfig loadConfig() {
        try {
            Files.createDirectories(this.getPluginContainer().dataFolder());
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot create Allay config directory", exception);
        }

        return (AllayBounceConfig) ConfigManager.create(AllayBounceConfig.class, it -> {
            it.withConfigurer(new YamlSnakeYamlConfigurer());
            it.withBindFile(this.getPluginContainer().dataFolder().resolve("config.yml"));
            it.saveDefaults();
            it.load(true);
        });
    }
}
