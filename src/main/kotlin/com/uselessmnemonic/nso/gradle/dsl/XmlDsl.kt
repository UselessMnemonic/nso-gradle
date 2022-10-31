package com.uselessmnemonic.nso.gradle.dsl

import org.w3c.dom.Document
import org.w3c.dom.Element

/**
 * Creates a child in this [document][Document].
 * @receiver The Document in which to create the child.
 * @param name The name of the child element.
 * @param block A configuration block.
 * @return The new child element.
 */
internal fun Document.node(name: String, block: (Element) -> Unit): Element {
    val new = this.createElement(name)
    this.appendChild(new)
    block(new)
    return new
}

/**
 * Creates a child in this [document][Document] with a namespace.
 * @receiver The Document in which to create the child.
 * @param name The name of the child element.
 * @param uri The namespace for the child element.
 * @param block A configuration block.
 * @return The new child element.
 */
internal fun Document.node(name: String, uri: String, block: (Element) -> Unit): Element {
    val new = this.createElementNS(uri, name)
    this.appendChild(new)
    block(new)
    return new
}

/**
 * Creates a child in this [element][Element].
 * @receiver The Element in which to create the child.
 * @param name The name of the child element.
 * @param block A configuration block.
 * @return The new child element.
 */
internal fun Element.node(name: String, block: (Element) -> Unit): Element {
    val new = this.ownerDocument.createElement(name)
    this.appendChild(new)
    block(new)
    return new
}

/**
 * Creates a child in this [element][Element] with a namespace.
 * @receiver The Element in which to create the child.
 * @param name The name of the child element.
 * @param uri The namespace for the child element.
 * @param block A configuration block.
 * @return The new child element.
 */
internal fun Element.node(name: String, uri: String, block: (Element) -> Unit): Element {
    val new = this.ownerDocument.createElementNS(uri, name)
    this.appendChild(new)
    block(new)
    return new
}

/**
 * Creates a child in this [element][Element], but only if the child itself is not empty.
 * @receiver The Element in which to create the child.
 * @param name The name of the child element.
 * @param block A configuration block.
 * @return The new child element, or null if the child itself was empty.
 */
internal fun Element.nodeNotEmpty(name: String, block: (Element) -> Unit): Element? {
    val new = this.ownerDocument.createElement(name)
    this.appendChild(new)
    block(new)
    if (new.hasChildNodes()) return new
    this.removeChild(new)
    return null
}

/**
 * Creates a node with text in this [element][Element].
 * @receiver The Element in which to create the child.
 * @param name The name of the child element.
 * @param value The text data for the child element.
 * @return The new child element.
 */
internal fun Element.textNode(name: String, value: String): Element {
    val new = this.ownerDocument.createElement(name)
    this.appendChild(new)
    new.textContent = value
    return new
}

/**
 * Creates a node with text in this [element][Element], but only if the supplied data is not null.
 * @receiver The Element in which to create the child.
 * @param name The name of the child element.
 * @param value The text data for the child element.
 * @return The new child element, or null if `value` was null.
 */
internal fun Element.maybeTextNode(name: String, value: String?): Element? {
    if (value !== null) return textNode(name, value)
    return null
}
