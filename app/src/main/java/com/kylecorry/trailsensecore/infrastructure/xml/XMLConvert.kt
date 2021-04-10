package com.kylecorry.trailsensecore.infrastructure.xml

import android.util.Xml
import org.xmlpull.v1.XmlPullParser

object XMLConvert {

    fun toString(xml: XMLNode, isRoot: Boolean = true): String {
        val children = xml.children.joinToString("") { toString(it, false) }
        val attributes = if (xml.attributes.isEmpty()) "" else " " + xml.attributes.toList().joinToString(" ") { "${it.first}=\"${it.second}\"" }
        val rootTag = if (isRoot) "<?xml version=\"1.0\"?>" else ""
        return "${rootTag}<${xml.tag}${attributes}>${xml.text ?: ""}${children}</${xml.tag}>"
    }


    fun parse(xml: String): XMLNode {
        val inputStream = xml.byteInputStream()
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return getXMLTree(parser)
        }
    }

    private fun getXMLTree(parser: XmlPullParser): XMLNode {
        val root = parser.name
        val attributes = mutableMapOf<String, String>()
        for (i in 0 until parser.attributeCount){
            attributes[parser.getAttributeName(i)] = parser.getAttributeValue(i)
        }
        var text: String? = null
        val children = mutableListOf<XMLNode>()
        while (parser.next() != XmlPullParser.END_DOCUMENT){
            if (parser.eventType == XmlPullParser.TEXT) {
                text = parser.text
                if (text.isNullOrBlank()){
                    text = null
                }
            }
            if (parser.eventType == XmlPullParser.START_TAG){
                children.add(getXMLTree(parser, parser.name))
            }
        }
        return XMLNode(root, attributes, text, children)
    }


    private fun getXMLTree(parser: XmlPullParser, tag: String): XMLNode {
        val children = mutableListOf<XMLNode>()
        var text: String? = null
        var attributes = mutableMapOf<String, String>()
        for (i in 0 until parser.attributeCount){
            attributes[parser.getAttributeName(i)] = parser.getAttributeValue(i)
        }
        while (!(parser.next() == XmlPullParser.END_TAG && parser.name == tag)){
            if (parser.eventType == XmlPullParser.TEXT) {
                text = parser.text
                if (text.isNullOrBlank()){
                    text = null
                }
            }
            if (parser.eventType == XmlPullParser.START_TAG){
                children.add(getXMLTree(parser, parser.name))
            }
        }
        return XMLNode(tag, attributes, text, children)
    }


}

data class XMLNode(
    val tag: String,
    val attributes: Map<String, String>,
    val text: String?,
    val children: List<XMLNode>
) {
    companion object {
        fun text(tag: String, text: String?): XMLNode {
            return XMLNode(tag, mapOf(), text, listOf())
        }
    }
}