package com.uselessmnemonic.nso.gradle.plugin

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

/**
 * A container for Yang source sets.
 */
abstract class YangSourceSetContainer @Inject constructor(objects: ObjectFactory)
    : NamedDomainObjectContainer<YangSourceSet> by objects.domainObjectContainer(YangSourceSet::class.java)
