package com.kylecorry.trailsensecore.infrastructure.xml

import org.junit.Assert.*
import org.junit.Test

class XMLConvertTest {

    @Test
    fun canConvertXMLNodesToString(){
        val xml = """<?xml version="1.0"?><gpx version="1.1" creator="Trail Sense" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.topografix.com/GPX/1/1" xmlns:trailsense="https://kylecorry.com/Trail-Sense" xsi:schemaLocation="http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd https://kylecorry.com/Trail-Sense https://kylecorry.com/Trail-Sense/trailsense.xsd"><wpt lat="37.778259000" lon="-122.391386000"><ele>3.4</ele><name>Beacon 1</name><desc>A test comment</desc><extensions><trailsense:group>Test Group</trailsense:group></extensions></wpt><wpt lat="31" lon="100"><name>Beacon 2</name></wpt></gpx>"""

        val parsed = XMLConvert.parse(xml)
        val str = XMLConvert.toString(parsed)

        assertEquals(xml, str)
    }

    @Test
    fun canParseXML(){
        val xml = """<?xml version="1.0"?>
<gpx version="1.1" creator="Trail Sense">
    <wpt lat="37.778259000" lon="-122.391386000">
        <ele>3.4</ele>
        <name>Beacon 1</name>
        <desc>A test comment</desc>
        <extensions>
            <trailsense:group>Test Group</trailsense:group>
        </extensions>
    </wpt>
    <wpt lat="31" lon="100">
        <name>Beacon 2</name>
    </wpt>
</gpx>"""
        val parsed = XMLConvert.parse(xml)

        val expected = XMLNode("gpx", mapOf("version" to "1.1", "creator" to "Trail Sense"), null, listOf(
            XMLNode("wpt", mapOf("lat" to "37.778259000", "lon" to "-122.391386000"), null, listOf(
                XMLNode("ele", mapOf(), "3.4", listOf()),
                XMLNode("name", mapOf(), "Beacon 1", listOf()),
                XMLNode("desc", mapOf(), "A test comment", listOf()),
                XMLNode("extensions", mapOf(), null, listOf(
                    XMLNode("trailsense:group", mapOf(), "Test Group", listOf())
                )),
            )),
            XMLNode("wpt", mapOf("lat" to "31", "lon" to "100"), null, listOf(
                XMLNode("name", mapOf(), "Beacon 2", listOf())
            ))
        ))

        assertEquals(expected, parsed)
    }
}