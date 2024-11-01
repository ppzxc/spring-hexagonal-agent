plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.nanoit.agent"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")

    annotationProcessor("org.projectlombok:lombok")
    implementation("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("io.netty:netty-all:4.1.24.Final")
    implementation("io.github.ppzxc:crypto:0.0.42")
//    implementation("org.bouncycastle:bcprov-jdk15on:1.70")
    implementation ("org.bouncycastle:bcprov-jdk18on:1.79")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
