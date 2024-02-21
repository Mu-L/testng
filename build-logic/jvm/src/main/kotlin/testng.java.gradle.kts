import com.github.vlsi.gradle.dsl.configureEach

plugins {
    `java-base`
    id("build-logic.build-params")
    id("testng.versioning")
    id("testng.style")
    id("testng.repositories")
    // Improves Gradle Test logging
    // See https://github.com/vlsi/vlsi-release-plugins/tree/master/plugins/gradle-extensions-plugin
    id("com.github.vlsi.gradle-extensions")
}

java {
    toolchain {
        configureToolchain(buildParameters.buildJdk)
    }
}

tasks.configureEach<JavaCompile> {
    // Use --release=<targetJavaVersion> for javac so the generated bytecode does not include methods introduced in
    // next Java releases
    options.release.set(buildParameters.targetJavaVersion)
}

tasks.configureEach<JavaExec> {
    buildParameters.testJdk?.let {
        javaLauncher.convention(javaToolchains.launcherFor(it))
    }
}

tasks.configureEach<JavaCompile> {
    inputs.property("java.version", System.getProperty("java.version"))
    inputs.property("java.vendor", System.getProperty("java.vendor"))
    inputs.property("java.vm.version", System.getProperty("java.vm.version"))
    inputs.property("java.vm.vendor", System.getProperty("java.vm.vendor"))
}

tasks.configureEach<Test> {
    inputs.property("java.version", System.getProperty("java.version"))
    inputs.property("java.vendor", System.getProperty("java.vendor"))
    inputs.property("java.vm.version", System.getProperty("java.vm.version"))
    inputs.property("java.vm.vendor", System.getProperty("java.vm.vendor"))
}

if (!buildParameters.skipErrorProne) {
    apply(plugin = "testng.errorprone")
}
