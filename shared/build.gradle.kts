
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    
}

kotlin {
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            
            // https://mvnrepository.com/artifact/com.unboundid/unboundid-ldapsdk
            implementation("com.unboundid:unboundid-ldapsdk:6.0.11")

            // https://mvnrepository.com/artifact/com.mysql/mysql-connector-j
            implementation("com.mysql:mysql-connector-j:8.3.0")
        }
    }
}

