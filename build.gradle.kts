import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// 루트 프로젝트는 소스코드를 관리하지 않으므로 apply false를 통해서 적용하지 않도록 일단 설정합니다.
plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25" apply false
//    kotlin("plugin.jpa") version "1.9.25" apply false
    id("org.springframework.boot") version "3.3.5" apply false
    id("io.spring.dependency-management") version "1.1.6" apply false
}

/**
 * Gradle 설정은 모든 프로젝트에 공통으로 적용되는 구성을 정의하는 기능을 제공
 *
 * 구성은 크게 3 파트
 * 프로젝트 의존성을 해결하기 위한 Maven 리포지토리 설정, Java 및 코틀린 컴파일 설정, 마지막으로 테스트 설정
 */
allprojects {
    group = "study"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile>{
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

/**
 * 하위 모듈들의 설정
 *
 * 여기서 정의한 하위 모듈 Build 과정은 settings.gradle.kts 파일에서 include 처리한 모듈들에 한해서 적용됩니다.
 */
subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
//    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")

    dependencies {
        //Spring Web 의존성
        implementation("org.springframework.boot:spring-boot-starter-web")

        //공통사용
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
//        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
}