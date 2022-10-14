package com.rsq.clinic

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.spring.SpringAutowireConstructorExtension

class ProjectConfig : AbstractProjectConfig() {
    override val isolationMode: IsolationMode
        get() = IsolationMode.InstancePerLeaf

    override fun extensions(): List<Extension> = listOf(SpringAutowireConstructorExtension)
}