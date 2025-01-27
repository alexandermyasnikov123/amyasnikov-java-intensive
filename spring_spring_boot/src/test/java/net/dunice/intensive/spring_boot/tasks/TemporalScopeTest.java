package net.dunice.intensive.spring_boot.tasks;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

public class TemporalScopeTest {

    @Test
    public void cantCreateScopeWithPoolSizeOrTtlLessThan1() {
        final var expectedMessage = "Pool size must be greater or equal to zero";
        final var constructorArguments = List.of(0, -1, -123213);

        constructorArguments.forEach(argument -> {
            final var exception = assertThrowsExactly(IllegalArgumentException.class, () -> new TemporalScope(argument));
            assertEquals(expectedMessage, exception.getMessage());
        });
        assertDoesNotThrow(() -> new TemporalScope(1));
        assertDoesNotThrow(() -> new TemporalScope(1123213));
    }

    @Test
    public void getCreatesNewObjectIfFirstEntryAndLaunchesExecutor() {
        withMockedValues(executor -> {
            final var scope = new TemporalScope(3);
            final var isInitializedField = (AtomicBoolean) ReflectionTestUtils.getField(scope, "isInitialized");
            var executorField = (ScheduledExecutorService) ReflectionTestUtils.getField(scope, "executor");

            assertFalse(isInitializedField.get());
            assertNull(executorField);

            scope.get("something", Object::new);

            executorField = (ScheduledExecutorService) ReflectionTestUtils.getField(scope, "executor");
            assertTrue(isInitializedField.get());
            assertEquals(executor, executorField);
        });
    }

    @Test
    public void getCreatesNewObjectOnlyIfNotExistsYet() {
        withMockedValues(executor -> {
            final var sameName = "something";
            final var scope = new TemporalScope(3);

            final var first = scope.get(sameName, Object::new);
            final var second = scope.get(sameName, Object::new);

            assertSame(first, second);
        });
    }

    @Test
    public void closeExecutesAllDestructionCallbacks_ClosesExecutor_And_ClearsCallbacksAndBeans() {
        withMockedValues(executor -> {
            final var scope = new TemporalScope(3);
            final var callbackWasCalled = new AtomicBoolean();

            //initializing scope
            scope.get("something", Object::new);
            scope.registerDestructionCallback("other", () -> callbackWasCalled.set(true));

            scope.close();

            final var beans = (Map<?, ?>) ReflectionTestUtils.getField(scope, "beans");
            final var destructionCallbacks = (Map<?, ?>) ReflectionTestUtils.getField(scope, "destructionCallbacks");

            verify(executor).close();
            assertTrue(callbackWasCalled.get());
            assertTrue(beans.isEmpty());
            assertTrue(destructionCallbacks.isEmpty());
        });
    }

    private void withMockedValues(Consumer<ScheduledExecutorService> consumer) {
        try (var staticMock = mockStatic(Executors.class)) {
            final var executorMock = mock(ScheduledExecutorService.class);
            staticMock.when(() -> Executors.newSingleThreadScheduledExecutor()).thenReturn(executorMock);

            consumer.accept(executorMock);
        }
    }
}