import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    application
}

group = "com.shareinstituto"
version = "1.0"

repositories {
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("io.javalin:javalin:3.5.0")
    compile("ch.qos.logback:logback-classic:1.2.3")
    compile("org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.12")
    compile("org.kodein.di:kodein-di-generic-jvm:6.4.0")
    compile("com.auth0:java-jwt:3.8.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "com.shareinstituto.PaginaInicialKt"
}