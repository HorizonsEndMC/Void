plugins {
	id("fabric-loom")
	java
	kotlin("jvm") version "1.8.10"
}

group = property("maven_group")!!
version = property("mod_version")!!

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.
	maven(uri("https://maven.terraformersmc.com/")) // modmenu
	maven(uri("https://maven.gegy.dev"))
	maven(uri("https://maven.isxander.dev/releases"))  // yacl
	maven(uri("https://maven.shedaniel.me")) // REI
	maven(uri("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1"))
	mavenCentral()
}

dependencies {
	minecraft("com.mojang:minecraft:${property("minecraft_version")}")
	mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")


	modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
	modImplementation("com.terraformersmc:modmenu:${property("mod_menu_version")}")
	modImplementation("dev.isxander:yet-another-config-lib:${property("yacl_version")}")
	modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")

	modImplementation("me.shedaniel:RoughlyEnoughItems-api-fabric:10.0.592")
	modImplementation("net.fabricmc:fabric-language-kotlin:1.9.1+kotlin.1.8.10")

	modRuntimeOnly("me.shedaniel:RoughlyEnoughItems-fabric:10.0.592")
	modRuntimeOnly("me.djtheredstoner:DevAuth-fabric:1.1.0")
}


tasks {
	processResources {
		inputs.property("version", project.version)
		filesMatching("fabric.mod.json") {
			expand(mutableMapOf("version" to project.version))
		}
	}

	jar {
		from("LICENSE")
	}

	compileKotlin {
		kotlinOptions.jvmTarget = "17"
	}
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()
}