/*
 * Copyright (c) 2022 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.
// TODO: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.anvil) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.dependencycheck) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.dependencygraph)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.kover)
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}

allprojects {
    // Detekt
    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }
    detekt {
        // preconfigure defaults
        buildUponDefaultConfig = true
        // activate all available (even unstable) rules.
        allRules = true
        // point to your custom config defining rules to run, overwriting default behavior
        config = files("$rootDir/tools/detekt/detekt.yml")
    }
    dependencies {
        detektPlugins("com.twitter.compose.rules:detekt:0.0.26")
    }

    // KtLint
    apply {
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    // See https://github.com/JLLeitschuh/ktlint-gradle#configuration
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        // See https://github.com/pinterest/ktlint/releases/
        // TODO 0.47.1 is available
        version.set("0.45.1")
        android.set(true)
        ignoreFailures.set(false)
        enableExperimentalRules.set(true)
        // display the corresponding rule
        verbose.set(true)
        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
            // To have XML report for Danger
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        }
        filter {
            exclude { element -> element.file.path.contains("$buildDir/generated/") }
        }
        disabledRules.set(
            setOf(
                // TODO Re-enable these 4 rules after reformatting project
                "indent",
                "experimental:argument-list-wrapping",
                "max-line-length",
                "parameter-list-wrapping",

                "spacing-between-declarations-with-comments",
                "no-multi-spaces",
                "experimental:spacing-between-declarations-with-annotations",
                "experimental:annotation",
                // - Missing newline after "("
                // - Missing newline before ")"
                "wrapping",
                // - Unnecessary trailing comma before ")"
                "experimental:trailing-comma",
                // - A block comment in between other elements on the same line is disallowed
                "experimental:comment-wrapping",
                // - A KDoc comment after any other element on the same line must be separated by a new line
                "experimental:kdoc-wrapping",
                // Ignore error "Redundant curly braces", since we use it to fix false positives, for instance in "elementLogs.${i}.txt"
                "string-template",
                // Not the same order than Android Studio formatter...
                "import-ordering",
            )
        )
    }
    // Dependency check
    apply {
        plugin("org.owasp.dependencycheck")
    }
}

// To run a sonar analysis:
// Run './gradlew sonar -Dsonar.login=<SONAR_LOGIN>'
// The SONAR_LOGIN is stored in passbolt as Token Sonar Cloud Bma
// Sonar result can be found here: https://sonarcloud.io/project/overview?id=vector-im_element-x-android
sonar {
    properties {
        property("sonar.projectName", "element-x-android")
        property("sonar.projectKey", "vector-im_element-x-android")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.projectVersion", "1.0") // TODO project(":app").android.defaultConfig.versionName)
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.links.homepage", "https://github.com/vector-im/element-x-android/")
        property("sonar.links.ci", "https://github.com/vector-im/element-x-android/actions")
        property("sonar.links.scm", "https://github.com/vector-im/element-x-android/")
        property("sonar.links.issue", "https://github.com/vector-im/element-x-android/issues")
        property("sonar.organization", "new_vector_ltd_organization")
        property("sonar.login", if (project.hasProperty("SONAR_LOGIN")) project.property("SONAR_LOGIN")!! else "invalid")

        // exclude source code from analyses separated by a colon (:)
        // Exclude Java source
        property("sonar.exclusions", "**/BugReporterMultipartBody.java")
    }
}

allprojects {
    val projectDir = projectDir.toString()
    sonar {
        properties {
            // Note: folders `kotlin` are not supported (yet), I asked on their side: https://community.sonarsource.com/t/82824
            // As a workaround provide the path in `sonar.sources` property.
            if (File("$projectDir/src/main/kotlin").exists()) {
                property("sonar.sources", "src/main/kotlin")
            }
            if (File("$projectDir/src/test/kotlin").exists()) {
                property("sonar.tests", "src/test/kotlin")
            }
        }
    }
}

allprojects {
    apply(plugin = "kover")
}

// Run `./gradlew koverMergedHtmlReport` to get report at ./build/reports/kover
// Run `./gradlew koverMergedReport` to also get XML report
koverMerged {
    enable()

    filters {
        classes {
            excludes.addAll(
                listOf(
                    /*
                    "*Fragment",
                    "*Fragment\$*",
                    "*Activity",
                    "*Activity\$*",
                     */
                )
            )
        }
    }
}
