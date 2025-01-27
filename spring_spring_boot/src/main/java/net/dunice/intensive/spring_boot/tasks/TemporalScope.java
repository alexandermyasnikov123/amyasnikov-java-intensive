package net.dunice.intensive.spring_boot.tasks;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Auto closeable bean pool. Destroys himself and according beans after the ttl is reached.
 * The default ttl(time to live) is {@link #INITIAL_TTL} seconds.
 */
public class TemporalScope implements Scope, AutoCloseable {
    private static final int INITIAL_TTL = 60;

    private static final int SCHEDULING_INTERVAL = 1;

    private static final int SCHEDULING_PERIOD = 0;

    private final ConcurrentMap<String, PoolValue> beans = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, Runnable> destructionCallbacks = new ConcurrentHashMap<>();

    private final AtomicBoolean isInitialized = new AtomicBoolean();

    private final int timeToLive;

    private ScheduledExecutorService executor;

    public TemporalScope(int timeToLive) {
        if (timeToLive < 1) {
            throw new IllegalArgumentException("Pool size must be greater or equal to zero");
        }
        this.timeToLive = timeToLive;
    }

    public TemporalScope() {
        this(INITIAL_TTL);
    }

    @NotNull
    @Override
    public Object get(@NotNull String name, @NotNull ObjectFactory<?> objectFactory) {
        initializeIfAbsent();
        return Objects.requireNonNull(beans.compute(name, (key, value) -> value == null ?
                new PoolValue(objectFactory.getObject(), new Date()) :
                value)
        ).bean();
    }

    @Override
    public Object remove(@NotNull String name) {
        final var result = beans.remove(name);
        if (result != null) {
            destructionCallbacks.remove(name);
            return result.bean();
        }
        return null;
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
            synchronized (isInitialized) {
                executor = Executors.newSingleThreadScheduledExecutor();
            }
            executor.scheduleAtFixedRate(() -> beans.forEach((name, entry) -> {
                final var expiredAt = entry.createdAt().getTime() + TimeUnit.SECONDS.toMillis(timeToLive);
                final var currentTime = System.currentTimeMillis();

                if (expiredAt >= currentTime) {
                    remove(name);
                }
            }), SCHEDULING_INTERVAL, SCHEDULING_PERIOD, TimeUnit.SECONDS);
        }
    }

    private record PoolValue(Object bean, Date createdAt) {
    }
}
