package com.uselessmnemonic.nso.gradle.models.components.callback

import com.uselessmnemonic.nso.gradle.models.components.NcsPackageComponent
import org.gradle.api.provider.SetProperty

/**
 * Describes a callback component in the project.
 *
 * A data-provider can be used to implement NCS, services, external data providers, etc.
 */
interface JavaCallbackComponent : NcsPackageComponent {
    /**
     * The names of the Java classes that are annotated with one of the callback annotations:
     * ServiceCallback, ActionCallback, AuthCallback, DataCallback, DBCallback,
     * SnmpInformResponseCallback, TransCallback, TransValidateCallback, ValidateCallback
     */
    val javaClassNames: SetProperty<String>
}
