plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.nanoit.agent"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
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

    // Netty 의존성 추가
    implementation("io.netty:netty-all:4.1.71.Final")
 //   implementation("org.slf4j:slf4j-api:1.7.32")
  //  implementation("ch.qos.logback:logback-classic:1.5.6")
    
}

tasks.withType<Test> {
    useJUnitPlatform()
}

