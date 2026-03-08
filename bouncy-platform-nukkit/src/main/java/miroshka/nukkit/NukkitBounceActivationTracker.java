package miroshka.nukkit;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class NukkitBounceActivationTracker<T> {

    private final Set<T> activeActors = ConcurrentHashMap.newKeySet();

    public boolean activate(T actorId) {
        return this.activeActors.add(actorId);
    }

    public void deactivate(T actorId) {
        this.activeActors.remove(actorId);
    }

    public void clear(T actorId) {
        this.activeActors.remove(actorId);
    }
}
