import com.google.protobuf.gradle.*

val grpcVersion = "1.37.0"
val protobufVersion = "3.15.8"
val protocVersion = protobufVersion

plugins {
    id("application")
    id("com.google.protobuf") version "0.8.8"
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClass.set("io.dfuse.example.graphql.App")
}

sourceSets {
    main {
        java {
            java.srcDir("build/generated/source/proto/main/grpc")
            java.srcDir("build/generated/source/proto/main/java")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${protocVersion}"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
    }

    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}

dependencies {
	implementation("com.fasterxml.jackson.core:jackson-core:2.12.2")
	implementation("com.fasterxml.jackson.core:jackson-annotations:2.12.2")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.12.2")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.2")
    implementation("com.google.protobuf:protobuf-java-util:${protobufVersion}")
    implementation("io.grpc:grpc-auth:${grpcVersion}")
    implementation("io.grpc:grpc-stub:${grpcVersion}")
    implementation("io.grpc:grpc-protobuf:${grpcVersion}")
	implementation("org.apache.httpcomponents.client5:httpclient5-fluent:5.0.3")

    compileOnly("org.apache.tomcat:annotations-api:6.0.53")

	runtimeOnly("org.slf4j:slf4j-nop:1.7.28")
    runtimeOnly("io.grpc:grpc-netty-shaded:${grpcVersion}")
}
