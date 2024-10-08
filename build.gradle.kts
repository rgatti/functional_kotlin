import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`

    alias(libs.plugins.versions)
    alias(libs.plugins.version.catalog.update)

    alias(libs.plugins.spotless)
}

spotless {
    format("misc") {
        target("*.gradle", ".gitattributes", ".gitignore")

        trimTrailingWhitespace()
        indentWithTabs()
        endWithNewline()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.guava)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<KotlinCompile>("compileKotlin") {
    dependsOn("spotlessApply")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
