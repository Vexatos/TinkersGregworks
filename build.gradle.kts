import net.minecraftforge.gradle.user.UserExtension

buildscript {
    repositories {
        mavenCentral()
        maven("http://files.minecraftforge.net/maven")
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.github.GTNH2:ForgeGradle:FG_1.2-SNAPSHOT")
    }
}

plugins {
    idea
    java
    id("org.ajoberstar.grgit") version("3.1.1")
}

apply(plugin = "forge")

idea {
    module {
        this.isDownloadJavadoc = true
        this.isDownloadSources = true
        outputDir = file("build/classes/java/main")
    }
}

java {
    this.sourceCompatibility = JavaVersion.VERSION_1_8
    this.targetCompatibility = JavaVersion.VERSION_1_8
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
    isTransitive = false
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

val tgregworksversion: String by project
val minecraftversion: String by project
val forgeversion: String by project

val Project.minecraft: UserExtension
    get() = extensions.getByName<UserExtension>("minecraft")

minecraft.version = forgeversion
version = "${minecraftversion}-${tgregworksversion}"
group= "vexatos.tgregworks" // http://maven.apache.org/guides/mini/guide-naming-conventions.html

repositories {
    mavenLocal()
    maven("https://gregtech.overminddl1.com/") { this.name = "GT6Maven" }
    maven("http://maven.ic2.player.to/") { this.name = "ic2" }
    maven("http://jenkins.usrv.eu:8081/nexus/content/repositories/releases/") { this.name = "UsrvDE/GTNH" }
    ivy {
        this.name = "gtnh_download_source_stupid_underscore_typo"
        this.artifactPattern("http://downloads.gtnewhorizons.com/Mods_for_Jenkins/[module]_[revision].[ext]")
    }
    ivy {
        this.name = "gtnh_download_source"
        this.artifactPattern("http://downloads.gtnewhorizons.com/Mods_for_Jenkins/[module]-[revision].[ext]")
    }
    ivy {
        this.name = "gtnh_download_source_subfolder"
        this.artifactPattern("http://downloads.gtnewhorizons.com/Mods_for_Jenkins/[organisation]/[module]/[revision]/[module]-[revision]-[classifier].[ext]")
    }
    ivy {
        this.name = "BuildCraft"
        this.artifactPattern("http://www.mod-buildcraft.com/releases/BuildCraft/[revision]/[module]-[revision](-[classifier]).[ext]")
    }
    maven("http://maven.cil.li/") { this.name = "OpenComputers" }
    maven("http://default.mobiusstrip.eu/maven") { this.name = "Jabba" }
    maven("http://chickenbones.net/maven/") { this.name = "CodeChicken" }
    maven("http://www.ryanliptak.com/maven/") { this.name = "appleCore" }
    maven("http://maven.tterrag.com") { this.name = "tterrag Repo" }
    maven("https://jitpack.io")
}

dependencies {
    //jitpack
    compile("com.github.GTNewHorizons:GT5-Unofficial:experimental-SNAPSHOT:dev") {
        this.isChanging = true
    }
    compile("com.github.GTNewHorizons:TinkersConstruct:master-SNAPSHOT") {
        this.isChanging = true
    }

    compileOnly("cofh:CoFHCore:3.1.6")
    compile("mantle:Mantle:1.7.10-0.3.2.jenkins187-deobf:dev")
    compileOnly(fileTree("libs") { this.include("*.jar") })

    //NEI
    compile("codechicken:CodeChickenLib:1.7.10-1.1.3.140:dev")
    compile("codechicken:CodeChickenCore:1.7.10-1.0.7.47:dev")
    compile("codechicken:NotEnoughItems:1.7.10-1.0.5.120:dev")
}

configure<UserExtension> {
    runDir = "run"

    this.includes.add("vexatos/tgregworks/TGregworks.java")
    this.replacements["@VERSION@"] = tgregworksversion
}

tasks.withType<Jar> {
    // this will ensure that this task is redone when the versions change.
    this.inputs.properties += "version" to project.version
    this.inputs.properties += "mcversion" to project.minecraft.version
    this.archiveBaseName.set("TGregworks-[${getVersionAppendage()}]")

    // replace stuff in mcmod.info, nothing else
    this.filesMatching("/mcmod.info") {
        this.expand(
                mapOf(
                        "version" to project.version,
                        "mcversion" to project.minecraft.version
                )
        )
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    this.from(sourceSets.main.get().allSource)
    this.archiveClassifier.set("sources")
}

val devJar by tasks.creating(Jar::class) {
    this.from(sourceSets.main.get().output)
    this.archiveClassifier.set("dev")
}

val apiJar by tasks.creating(Jar::class){
    this.from(sourceSets.main.get().allSource){
        this.include("vexatos/**/api/**")
    }
    this.archiveClassifier.set("api")
}

artifacts {
    this.archives(sourcesJar)
    this.archives(devJar)
    this.archives(apiJar)
}

fun getVersionAppendage() : String {
    return org.ajoberstar.grgit.Grgit.open(mapOf("currentDir" to project.rootDir)).log().last().abbreviatedId
}