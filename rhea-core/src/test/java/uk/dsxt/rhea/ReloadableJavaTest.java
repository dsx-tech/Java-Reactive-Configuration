package uk.dsxt.rhea;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReloadableJavaTest {
    private ReactiveConfig config;
    private ConfigMock source;

    @BeforeEach
    void init() {
        source = new ConfigMock();
        config = new ReactiveConfig.Builder()
                .addSource("configMock", source)
                .build();
    }

    @Test
    void onChangeTest() throws InterruptedException {
        source.addToMap("number", 5);
        Reloadable<Integer> number = config.get("number", PropertyTypesKt.intType);
        Thread.sleep(20);
        AtomicReference<Integer> refSum = new AtomicReference<>(0);
        number.onChange((Integer value) -> {
            refSum.updateAndGet(v -> v + value);
            return null;
        });
        source.pushChanges("number", 10);
        Thread.sleep(50);
        assertEquals(10, refSum.get());
    }

    @Test
    void mapTest() {
        source.addToMap("number", 14);
        Reloadable<Integer> property = config.get("number", PropertyTypesKt.intType);
        Reloadable<String> mappedProperty = property.map(value -> value.toString());
        assertEquals(14, property.get());
        assertEquals("14", mappedProperty.get());
    }

    @Test
    void combineTest() throws InterruptedException {
        source.addToMap("number1", 0);
        source.addToMap("number2", 0);

        Reloadable<Integer> property1 = config.get("number1", PropertyTypesKt.intType);
        Reloadable<Integer> property2 = config.get("number2", PropertyTypesKt.intType);
        Reloadable<Integer> combinedProperty = property1.combine(property2,
                (i1, i2) -> i1 + i2);

        Thread.sleep(100);
        source.pushChanges("number1", 11);
        source.pushChanges("number2", 16);
        Thread.sleep(100);

        assertEquals(27, combinedProperty.get());
    }
}
