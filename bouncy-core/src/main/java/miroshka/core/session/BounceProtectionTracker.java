package miroshka.core.session;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class BounceProtectionTracker<T> {

    private final Set<T> protectedActors = ConcurrentHashMap.newKeySet();

    public void markProtected(T actorId) {
        this.protectedActors.add(actorId);
    }

    public boolean consumeProtection(T actorId) {
        return this.protectedActors.remove(actorId);
    }

    public void clearProtection(T actorId) {
        this.protectedActors.remove(actorId);
    }
}
