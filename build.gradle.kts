import io.papermc.paperweight.userdev.ReobfArtifactConfiguration

plugins {
    id("java")
    id("java-base")
    id("java-library")
    id("maven-publish")
    id("io.papermc.paperweight.userdev") version "1.7.2"
    id("xyz.jpenilla.run-paper") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}


group = "de.timesnake"
version = "5.0.0"
var projectId = 52

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://git.timesnake.de/api/v4/groups/7/-/packages/maven")
        name = "timesnake"
        credentials(PasswordCredentials::class)
    }
}

val pluginImplementation: Configuration by configurations.creating
val pluginFile = layout.buildDirectory.file("libs/${project.name}-${project.version}-plugin.jar")
val pluginArtifact = artifacts.add("pluginImplementation", pluginFile.get().asFile) {
    builtBy("pluginJar")
}

dependencies {
    pluginImplementation("de.timesnake:library-entities:4.+") { isTransitive = false }
    pluginImplementation("de.timesnake:library-packets:4.+") { isTransitive = false }

    pluginImplementation("de.timesnake:library-network:3.+") { isTransitive = false }
    pluginImplementation("org.freemarker:freemarker:2.3.31")
    pluginImplementation("commons-io:commons-io:2.14.0")

    pluginImplementation("de.timesnake:library-commands:3.+") { isTransitive = false }
    pluginImplementation("de.timesnake:library-permissions:3.+") { isTransitive = false }
    pluginImplementation("de.timesnake:library-basic:3.+") { isTransitive = false }

    pluginImplementation("de.timesnake:library-chat:3.+") { isTransitive = false }
    pluginImplementation("net.kyori:adventure-api:4.11.0")
    pluginImplementation("net.kyori:adventure-text-serializer-legacy:4.12.0")
    pluginImplementation("net.kyori:adventure-text-serializer-plain:4.12.0")

    pluginImplementation("com.moandjiezana.toml:toml4j:0.7.3-SNAPSHOT")
    pluginImplementation("com.google.code.gson:gson:2.10.1")

    api("de.timesnake:library-entities:4.+")
    api("de.timesnake:library-packets:4.+")

    api("de.timesnake:library-network:3.+")
    api("de.timesnake:library-commands:3.+")
    api("de.timesnake:library-permissions:3.+")
    api("de.timesnake:library-basic:3.+")
    api("de.timesnake:library-chat:3.+")

    api("com.moandjiezana.toml:toml4j:0.7.3-SNAPSHOT")
    api("com.google.code.gson:gson:2.10.1")

    api("de.timesnake:database-bukkit:5.+")
    api("de.timesnake:channel-bukkit:6.+")

    api("org.freemarker:freemarker:2.3.31")

    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("de.timesnake:channel-bukkit:6.+")
}

configurations.all {
    resolutionStrategy.dependencySubstitution.all {
        requested.let {
            if (it is ModuleComponentSelector && it.group == "de.timesnake") {
                val targetProject = findProject(":${it.module}")
                if (targetProject != null) {
                    useTarget(targetProject)
                }
            }
        }
    }
}

publishing {
    repositories {
        maven {
            url = uri("https://git.timesnake.de/api/v4/projects/$projectId/packages/maven")
            name = "timesnake"
            credentials(PasswordCredentials::class)
        }
    }

    publications {
        create<MavenPublication>("maven") {
            artifacts {
                from(components["java"])
                artifact(pluginArtifact)
            }
        }
    }
}

tasks.register<Jar>("pluginJar") {
    from(pluginImplementation.map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
    archiveClassifier = "plugin"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    dependsOn("shadowJar", "assemble")
}

tasks.register<Copy>("exportPluginJar") {
    from(pluginFile)
    into(findProperty("timesnakePluginDir") ?: "")
    dependsOn("pluginJar")
}

tasks.withType<PublishToMavenRepository> {
    dependsOn("shadowJar", "pluginJar")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release = 21
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("plugin.yml") {
            expand(mapOf(Pair("version", project.version)))
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withSourcesJar()
}

paperweight.reobfArtifactConfiguration = ReobfArtifactConfiguration.MOJANG_PRODUCTION

tasks.test {
    useJUnitPlatform()
}
