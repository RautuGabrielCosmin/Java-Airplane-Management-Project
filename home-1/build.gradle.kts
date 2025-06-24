import org.gradle.api.tasks.JavaExec

plugins {
    id("java")
    application
}



group = "org.example"
version = "1.0-SNAPSHOT"

application {
    // change this to the fully‑qualified name of your Application subclass
    mainClass.set("serverGui.ServerDashboardApplication")
    mainClass.set("serverGui.ServerDashboardFrame")
}

repositories {
    mavenCentral()
    flatDir {
        dirs("C:/Users/Rautu/Downloads")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}



dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Maven dependencies
    implementation("org.xerial:sqlite-jdbc:3.49.1.0")
    implementation("org.apache.logging.log4j:log4j-core:2.23.0")
    implementation("org.apache.logging.log4j:log4j-api:2.23.0")
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.16")
    implementation("com.miglayout:miglayout-swing:11.4.2")
    implementation("com.miglayout:miglayout-core:11.4.2")
    runtimeOnly("com.github.weisj:jsvg:1.7.1")
    implementation("net.java.dev.jna:jna:5.13.0")
    implementation("com.kenai.nbpwr:org-jdesktop-animation-timing:1.0-201002281504")
    implementation("com.formdev:flatlaf:3.5.4")
    implementation("com.formdev:flatlaf-extras:3.5.4")
    implementation("com.formdev:flatlaf-fonts-roboto:2.137")
    implementation("org.apache.commons:commons-dbcp2:2.12.0")
    // https://mvnrepository.com/artifact/commons-logging/commons-logging
    implementation("commons-logging:commons-logging:1.3.5")
    // https://mvnrepository.com/artifact/org.apache.commons/commons-pool2
    implementation("org.apache.commons:commons-pool2:2.12.1")

    implementation("org.controlsfx:controlsfx:11.2.1")

    implementation("com.dlsc.formsfx:formsfx-core:11.6.0") {
        exclude(group = "org.openjfx")
    }
    implementation("net.synedra:validatorfx:0.5.0") {
        exclude(group = "org.openjfx")
    }
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.bootstrapfx:bootstrapfx-core:0.4.0")

    implementation("eu.hansolo:tilesfx:21.0.3") {
        exclude(group = "org.openjfx")
    }
    implementation("com.github.almasb:fxgl:17.3") {
        exclude(group = "org.openjfx")
        exclude(group = "org.jetbrains.kotlin")
    }

    implementation(files("C://Users//Rautu//Desktop//MPP//Homework-1//home-1//lib//swing-glasspane-popup-1.5.1.jar"))
    implementation(files("C://Users//Rautu//Desktop//MPP//Homework-1//home-1//lib//picture-box-1.2.jar"))
    implementation(files("C://Users//Rautu//Desktop//MPP//Homework-1//home-1//lib//swing-datetime-picker-1.2.0.jar"))
    implementation(files("C://Users//Rautu//Desktop//MPP//Homework-1//home-1//lib//swing-jnafilechooser.jar"))
    implementation(files("C://Users//Rautu//Desktop//MPP//Homework-1//home-1//lib//swing-toast-notifications-1.0.2.jar"))
    implementation(files("C://Users//Rautu//Desktop//MPP//Homework-1//home-1//lib//thumbnailator-0.4.20.jar"))
    implementation(files("C://Users//Rautu//Desktop//MPP//Homework-1//home-1//lib//modal-dialog-2.4.0-sources.jar"))
    implementation(files("C://Users//Rautu//Desktop//MPP//Homework-1//home-1//lib//modal-dialog-2.4.0.jar"))
    implementation(files("C://Users//Rautu//Desktop//MPP//Homework-1//home-1//lib//modal-dialog-2.4.0-javadoc.jar"))
    implementation(files("C://Users//Rautu//Desktop//MPP//Homework-1//home-1//src//main//resources//libs//swing-toast-notifications-1.0.2.jar"))

// https://mvnrepository.com/artifact/com.formdev/svgSalamander
    runtimeOnly("com.formdev:svgSalamander:1.1.4")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.21")
}

tasks.test {
    useJUnitPlatform()
}


