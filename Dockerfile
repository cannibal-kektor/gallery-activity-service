FROM gradle:jdk25-alpine AS builder

WORKDIR /opt/build/app/activity-service

COPY gradlew gradlew.bat ./
COPY gradle/wrapper gradle/wrapper
COPY build.gradle.kts settings.gradle.kts ./

RUN --mount=type=cache,target=/home/gradle/.gradle \
    ./gradlew dependencies --no-daemon

COPY src src

RUN --mount=type=cache,target=/home/gradle/.gradle \
    ./gradlew --no-daemon -Dorg.gradle.caching=true -Dorg.gradle.parallel=true \
    bootJar -x test

FROM eclipse-temurin:25-jdk-alpine AS optimizer
WORKDIR /opt/build
#COPY --from=builder /opt/build/app/image-service/target/*.jar app.jar
COPY --from=builder /opt/build/app/activity-service/build/libs/*.jar app.jar

RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted

# Find modules, used in project
RUN jdeps --ignore-missing-deps -q \
    --recursive  \
    --multi-release 25  \
    --print-module-deps  \
    --class-path 'extracted/dependencies/lib/*':'extracted/snapshot-dependencies/lib/*' \
    app.jar > deps.info

# Generating optimized JRE
RUN set -e; \
    MODULES=$(cat deps.info | tr -d '\n'); \
    jlink \
        --add-modules $MODULES,jdk.jdwp.agent \
        --strip-debug \
        --compress 2 \
        --no-header-files \
        --no-man-pages \
        --output optimizedJRE25

# Generate CDS-archive for the core classes in created jre
RUN optimizedJRE25/bin/java -Xshare:dump

FROM alpine:3.22

ENV JAVA_HOME /opt/java/optimizedJRE25
ENV PATH $JAVA_HOME/bin:$PATH
ARG BUILD_PATH=/opt/build
ARG BUILD_EXTRACTED=$BUILD_PATH/extracted

WORKDIR /app

RUN addgroup -S --gid 1000 springApp \
   && adduser -S -G springApp --uid 1000 springApp \
    && chown springApp:springApp /app

COPY --from=optimizer $BUILD_PATH/optimizedJRE25 $JAVA_HOME

COPY --chown=springApp:springApp --from=optimizer $BUILD_EXTRACTED/spring-boot-loader/ ./
COPY --chown=springApp:springApp --from=optimizer $BUILD_EXTRACTED/dependencies/ ./
COPY --chown=springApp:springApp --from=optimizer $BUILD_EXTRACTED/snapshot-dependencies/ ./
COPY --chown=springApp:springApp --from=optimizer $BUILD_EXTRACTED/application/ ./

USER springApp

# Execute the CDS training run
RUN $JAVA_HOME/bin/java \
      -XX:ArchiveClassesAtExit=app.jsa \
      -Dspring.context.exit=onRefresh \
      -jar app.jar \
      --spring.profiles.active=smoke

# Start the application jar with CDS enabled
ENTRYPOINT ["java", "-XX:SharedArchiveFile=app.jsa", "-jar", "app.jar"]
