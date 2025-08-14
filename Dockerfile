# ========= ORIGINAL (distroless) =========
# ARG KAL_VERSION=unspecified
# FROM gcr.io/distroless/java17-debian12:nonroot AS kal
#
# ARG KAL_VERSION
# WORKDIR /opt/kal
# COPY kal/build/libs/kal-fat-${KAL_VERSION}.jar ./kal.jar
# EXPOSE 1883
# ENTRYPOINT ["java","-jar","/opt/kal/kal.jar"]

# ========= DEBUG-FRIENDLY IMAGE (with tools, optional JFR/JMX) =========
# Full JDK so we have jcmd/jmap/jstack/JFR + a shell
FROM eclipse-temurin:17-jdk AS kal

ARG KAL_VERSION
ENV KAL_VERSION=${KAL_VERSION}

WORKDIR /opt/kal

# Minimal debug tools (bash, curl, ps/top, etc.)
RUN apt-get update && apt-get install -y --no-install-recommends \
    bash curl procps iproute2 net-tools netcat-traditional \
 && rm -rf /var/lib/apt/lists/*

# (Optional) Prometheus JMX exporter – default DISABLED at runtime
ARG JMX_AGENT_VER=0.20.0
RUN mkdir -p /opt/jmx /opt/kal/dumps \
 && curl -fsSL -o /opt/jmx/jmx_prometheus_javaagent.jar \
    https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/${JMX_AGENT_VER}/jmx_prometheus_javaagent-${JMX_AGENT_VER}.jar

# JMX config lives here (present even if agent disabled)
COPY jmx-config.yml /opt/jmx/config.yml

# Your fat JAR is built outside and copied in
COPY kal/build/libs/kal-fat-${KAL_VERSION}.jar /opt/kal/kal.jar

# Non-root user
RUN useradd -r -u 10001 -g root kal \
 && mkdir -p /opt/kal/dumps \
 && chown -R kal:root /opt/kal /opt/jmx
USER kal

# Sensible JVM defaults; NO javaagent or JFR here so tools like jcmd don't crash
ENV JAVA_TOOL_OPTIONS="\
 -Xms64m -Xmx512m \
 -XX:MaxDirectMemorySize=128m \
 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/kal/dumps \
 -XX:NativeMemoryTracking=summary \
 -Xlog:gc*:stdout:time,level,tags \
"

# Runtime toggles (both off by default for prod)
ENV ENABLE_JFR=false \
    ENABLE_JMX_AGENT=false \
    JMX_PORT=9404

# Expose only the MQTT port by default (no Prometheus exposure in prod)
EXPOSE 1883

# Inline entrypoint so we can conditionally add JFR/JMX at runtime
ENTRYPOINT ["bash","-lc", "\
  OPTS=(); \
  if [ \"${ENABLE_JFR:-false}\" = \"true\" ]; then \
    mkdir -p /opt/kal/dumps; \
    OPTS+=(\"-XX:StartFlightRecording=filename=/opt/kal/dumps/kal.jfr,settings=profile,delay=10s,maxsize=256m,maxage=1d,dumponexit=true\"); \
  fi; \
  if [ \"${ENABLE_JMX_AGENT:-false}\" = \"true\" ]; then \
    OPTS+=(\"-javaagent:/opt/jmx/jmx_prometheus_javaagent.jar=${JMX_PORT:-9404}:/opt/jmx/config.yml\"); \
  fi; \
  exec java $JAVA_TOOL_OPTIONS \"${OPTS[@]}\" -jar /opt/kal/kal.jar \
"]
