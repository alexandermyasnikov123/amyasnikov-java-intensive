package net.dunice.intensive.spring_boot.tasks;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * Works exactly the same as the CachedThreadPool except that it's manipulating with beans instead of threads.
 * The default time to live is {@link #INITIAL_TTL} seconds.
 */
@RequiredArgsConstructor
public class CachedPoolScope implements Scope, AutoCloseable {
    private static final int INITIAL_IN_POOL = 1;

    private static final int INITIAL_TTL = 60;

    private static final int SCHEDULED_TIMEOUT = 1;

    private final ConcurrentMap<String, PoolEntry> beans = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, Runnable> destructionCallbacks = new ConcurrentHashMap<>();

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private final AtomicBoolean isInitialized = new AtomicBoolean();

    private final int poolSize;

    private final int timeToLive;

    public CachedPoolScope(int poolSize) {
        this(poolSize, INITIAL_TTL);
    }

    @NotNull
    @Override
    public Object get(@NotNull String name, @NotNull ObjectFactory<?> objectFactory) {
        initializeIfAbsent();
        return beans.compute(name, (key, value) -> switch (value) {
            case PoolEntry entry when entry.inPool() < poolSize -> entry.withIncrementedInPool();
            case PoolEntry entry when entry.inPool() == poolSize -> entry;
            case null -> new PoolEntry(objectFactory.getObject(), INITIAL_IN_POOL, new Date());
            default -> throw new IllegalStateException("Unexpected value: " + value);
        });
    }

    @Override
    public Object remove(@NotNull String name) {
        final var result = beans.computeIfPresent(name, (key, value) -> value.withDecrementedInPool());
        if (result != null && destructionCallbacks.containsKey(name)) {
            final Function<String, Runnable> callback = result.inPool() > 0 ?
                    destructionCallbacks::get :
                    destructionCallbacks::remove;

            callback.apply(name).run();
        }
        return result;
    }

    @Override
    public void registerDestructionCallback(@NotNull String name, @NotNull Runnable callback) {
        destructionCallbacks.put(name, callback);
    }

    @Override
    public Object resolveContextualObject(@NotNull String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return "cached-pool";
    }

    @Override
    public void close() {
        beans.clear();
        destructionCallbacks.values().forEach(Runnable::run);
        destructionCallbacks.clear();
        executor.close();
    }

    private void initializeIfAbsent() {
        if (isInitialized.compareAndSet(false, true)) {
            executor.scheduleAtFixedRate(() -> beans.forEach((name, entry) -> {
                final var expiredAt = entry.creationDate().getTime() + TimeUnit.SECONDS.toMillis(timeToLive);
                final var currentTime = System.currentTimeMillis();

                if (expiredAt >= currentTime) {
                    remove(name);
                }
            }), SCHEDULED_TIMEOUT, SCHEDULED_TIMEOUT, TimeUnit.SECONDS);
        }
    }

    private record PoolEntry(Object value, int inPool, Date creationDate) {
        public PoolEntry withIncrementedInPool() {
            return new PoolEntry(value, inPool + 1, new Date());
        }

        public PoolEntry withDecrementedInPool() {
            return new PoolEntry(value, inPool - 1, new Date());
        }
    }
}
