package configsources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.dsxt.rhea.PropertyTypesKt;
import uk.dsxt.rhea.ReactiveConfig;
import uk.dsxt.rhea.Reloadable;
import uk.dsxt.rhea.configsources.PropertiesConfigSource;
import uk.dsxt.rhea.interfaces.ConfigSource;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PropertiesConfigSourceJavaTest {
    private ConfigSource propertiesSource;
    private ReactiveConfig config;

    @BeforeEach
    void init(){
        propertiesSource =
                new PropertiesConfigSource(
                        Paths.get("src" + File.separator + "test" + File.separator + "resources"),
                        "propertiesSource.properties"
                );

        config = new ReactiveConfig.Builder()
                .addSource("propertiesConfig", propertiesSource)
                .build();
    }

    @Test
    void intTypeTest(){
        Reloadable<String> name = config.get("login", PropertyTypesKt.stringType);
        assertEquals("Felix", name.get());
    }

    @Test
    void stringTypeTest(){
        Reloadable<Integer> number = config.get("password", PropertyTypesKt.intType);
        assertEquals(1234, number.get());
    }
}
