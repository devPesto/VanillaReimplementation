plugins {
    java
}

version = "unspecified"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(project(":Core"))
    implementation(project(":WorldGeneration"))
    implementation(project(":InstanceMeta"))
    implementation(project(":Commands"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}