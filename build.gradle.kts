import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("cc.mewcraft.repo-conventions")
    id("cc.mewcraft.java-conventions")
    id("cc.mewcraft.guice-conventions")
    id("cc.mewcraft.deploy-conventions")
    alias(libs.plugins.pluginyml.paper)
}

project.ext.set("name", "PickaxePower")

group = "cc.mewcraft.pickaxepower"
version = "1.0.1"
description = "Add pickaxe power system to your pickaxes!"

dependencies {
    // server
    compileOnly(libs.server.paper)

    // standalone plugins
    compileOnly(libs.helper) { isTransitive = false }
    compileOnly(libs.protocollib)
    compileOnly(libs.itemsadder)

    // internal
    implementation(project(":spatula:bukkit:command"))
    implementation(project(":spatula:bukkit:message"))
    implementation(project(":spatula:bukkit:utils"))
}

paper {
    main = "cc.mewcraft.pickaxepower.PickaxePower"
    name = project.ext.get("name") as String
    version = "${project.version}"
    description = project.description
    apiVersion = "1.19"
    authors = listOf("Nailm")
    serverDependencies {
        register("helper") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
        register("ProtocolLib") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
        register("ItemsAdder") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
}
