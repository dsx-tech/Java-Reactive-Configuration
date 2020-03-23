package uk.dsx.reactiveconfig.configsources

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import uk.dsx.reactiveconfig.ObjectNode
import uk.dsx.reactiveconfig.NumericNode
import uk.dsx.reactiveconfig.ReactiveConfig
import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

object JsonConfigSourceTest : Spek({
    val config = ReactiveConfig {}
    val jsonSource =
        JsonConfigSource(Paths.get("src" + File.separator + "test" + File.separator + "resources"), "jsonSource.json")
    config.addConfigSource(jsonSource)

    describe("resource check") {
        it("should be initialised") {
            assertNotNull(jsonSource)
        }
    }

    describe("checks reading properly BooleanNode from json") {
        val isSomethingOn by config.base.booleanType
        while (true) {
            if (!isSomethingOn.get()) break
        }

        it("should contain value 'true' sent from JsonConfigSource") {
            assertTrue(isSomethingOn.get())
        }
    }

    describe("checks reading properly StringNode from json") {
        val property by config.base.stringType
        while (true) {
            if (property.get() != "") break
        }

        it("should contain value 'someInfo' sent from JsonConfigSource") {
            assertEquals("someInfo", property.get())
        }
    }

    describe("checks reading properly from json complex ObjectNode with NumericNode inside") {
        val config = ReactiveConfig {}
        val jsonSource =
            JsonConfigSource(
                Paths.get("src" + File.separator + "test" + File.separator + "resources"),
                "jsonSource.json"
            )
        config.addConfigSource(jsonSource)

        lateinit var server: ObjectNode

        config.manager.configScope.launch {
            config.manager.flowOfChanges.filter {
                it.key == "server"
            }.collect {
                server = it.value as ObjectNode
            }
        }

        it("server should contain 'port' with value=1234 sent from JsonConfigSource") {
            assertEquals("1234", (server.value["port"] as NumericNode).value)

        }
    }
})