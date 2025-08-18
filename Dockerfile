###############################################
# OPTIONAL DEBUG IMAGE (tools + shell + JDK)  #
###############################################
#FROM eclipse-temurin:17-jdk AS kal-debug
#ARG KAL_VERSION
#ENV KAL_VERSION=${KAL_VERSION}
#
#WORKDIR /opt/kal
#
## Minimal tools so you can shell in and run jcmd/jmap if needed
#RUN apt-get update && apt-get install -y --no-install-recommends \
#    bash curl procps iproute2 net-tools netcat-traditional \
# && rm -rf /var/lib/apt/lists/*
#
## Jar is built outside and copied in (unchanged)
#COPY kal/build/libs/kal-fat-${KAL_VERSION}.jar /opt/kal/kal.jar
#
## Same memory profile you validated
#ENV JAVA_TOOL_OPTIONS="\
# -Xms32m -Xmx128m \
# -Xss256k \
# -XX:MaxDirectMemorySize=64m \
# -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp \
# -Xlog:gc=info \
# -Djava.awt.headless=true \
#"
#
#EXPOSE 1883
#
## Use a shell here so JAVA_TOOL_OPTIONS expands (debug-only)
#ENTRYPOINT ["bash","-lc","exec java ${JAVA_TOOL_OPTIONS} -XX:+UseSerialGC -jar /opt/kal/kal.jar"]
#
###############################################
# RELEASE IMAGE (distroless, minimal runtime) #
###############################################
FROM gcr.io/distroless/java17-debian12:nonroot AS kal
ARG KAL_VERSION

WORKDIR /opt/kal

# Jar is built outside and copied in (unchanged)
COPY kal/build/libs/kal-fat-${KAL_VERSION}.jar /opt/kal/kal.jar

# Memory-tuned defaults carried over from the debug run.
# NOTE: dumps go to /tmp (writable for nonroot in distroless).
ENV JAVA_TOOL_OPTIONS="\
 -Xms32m -Xmx128m \
 -Xss256k \
 -XX:MaxDirectMemorySize=64m \
 -XX:+UseSerialGC \
 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp \
 -Xlog:gc=info \
 -Djava.awt.headless=true \
"

EXPOSE 1883
ENTRYPOINT ["java","-jar","/opt/kal/kal.jar"]
