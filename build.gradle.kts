plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.16.0"
    id("org.jetbrains.kotlin.jvm") version "1.9.20"
}

group = "com.advancedtools.cpp"
version = "1.0"

repositories {
    // 改为阿里云的镜像地址
    maven { setUrl("https://maven.aliyun.com/repository/central") }
    maven { setUrl("https://maven.aliyun.com/repository/jcenter") }
    maven { setUrl("https://maven.aliyun.com/repository/google") }
    maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
    maven { setUrl("https://maven.aliyun.com/repository/public") }
    maven { setUrl("https://jitpack.io") }
    gradlePluginPortal()
    google()
    mavenCentral()
}
dependencies {
    implementation("org.jetbrains:annotations:20.1.0")
    // https://mvnrepository.com/artifact/com.thoughtworks.xstream/xstream这些标记，升级方案
    implementation("com.thoughtworks.xstream:xstream:1.4.20")
    implementation ("cn.hutool:hutool-all:5.8.16")
    implementation ("com.alibaba.fastjson2:fastjson2:2.0.43")


}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {

    version.set("2023.2.1.11")
    type.set("AI")
    plugins.set(listOf())
//    //localPath.set("E:/Android Studio")
//    plugins.set(listOf())
}



sourceSets {
    main {
        java {
            srcDirs("src/main/gen")
        }
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    buildSearchableOptions {
        enabled = false
    }

    patchPluginXml {
        sinceBuild.set("223")
        //这里untilBuild  其实是在
        untilBuild.set("243.*")
        val descriptionFile = "parts/pluginDescription.html"
        val changesFile = "parts/pluginChanges.html"
        pluginDescription = file(descriptionFile).readText()
        changeNotes = file(changesFile).readText()
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
