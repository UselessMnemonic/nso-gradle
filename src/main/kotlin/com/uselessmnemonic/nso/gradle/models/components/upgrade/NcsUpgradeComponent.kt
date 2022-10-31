package com.uselessmnemonic.nso.gradle.models.components.upgrade

import com.uselessmnemonic.nso.gradle.models.components.NcsPackageComponent

/**
 * An upgrade component is used to migrate data for packages where the YANG
 * data model has changed and the automatic CDB upgrade is not sufficient.
 *
 * The upgrade component consists of either a Java class with a main method
 * or a Python class with an upgrade method, that is expected to run one time only.
 */
sealed interface NcsUpgradeComponent : NcsPackageComponent
