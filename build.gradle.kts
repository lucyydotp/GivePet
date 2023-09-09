plugins {
    java
    `maven-publish`
    id("xyz.jpenilla.run-paper") version "2.1.0"
}

group = "me.lucyydotp"
version = "1.1.0"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

tasks.runServer {
    minecraftVersion("1.20.1")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
}
