# ========= ORIGINAL (distroless) =========
# ARG KAL_VERSION=unspecified
# FROM gcr.io/distroless/java17-debian12:nonroot AS kal
#
# ARG KAL_VERSION
# WORKDIR /opt/kal
# COPY kal/build/libs/kal-fat-${KAL_VERSION}.jar ./kal.jar
# EXPOSE 1883
# ENTRYPOINT ["java","-jar","/opt/kal/kal.jar"]

# ========= DEBUG-FRIENDLY IMAGE (memory-tuned defaults; optional JFR/JMX/NMT) =========
FROM eclipse-temurin:17-jdk AS kal

ARG KAL_VERSION
ENV KAL_VERSION=${KAL_VERSION}

WORKDIR /opt/kal

# Minimal debug tools
RUN apt-get update && apt-get install -y --no-install-recommends \
    bash curl procps iproute2 net-tools netcat-traditional \
 && rm -rf /var/lib/apt/lists/*

# (Optional) Prometheus JMX exporter – present but disabled by default
ARG JMX_AGENT_VER=0.20.0
RUN mkdir -p /opt/jmx /opt/kal/dumps \
 && curl -fsSL -o /opt/jmx/jmx_prometheus_javaagent.jar \
    https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/${JMX_AGENT_VER}/jmx_prometheus_javaagent-${JMX_AGENT_VER}.jar

# JMX config (even if agent disabled)
COPY jmx-config.yml /opt/jmx/config.yml

# Your fat JAR is built outside and copied in
COPY kal/build/libs/kal-fat-${KAL_VERSION}.jar /opt/kal/kal.jar

# Non-root user
RUN useradd -r -u 10001 -g root kal \
 && mkdir -p /opt/kal/dumps \
 && chown -R kal:root /opt/kal /opt/jmx
USER kal

# ===== Memory-tuned JVM defaults (no agents here) =====
# - Small heap with headroom, small stacks, CDS off, smaller direct buffers.
# - No NMT/JFR/JMX by default (can be toggled at runtime).
ENV JAVA_TOOL_OPTIONS="\
 -Xms32m -Xmx128m \
 -Xss256k \
 -XX:MaxDirectMemorySize=64m \
 -Xshare:off \
 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/kal/dumps \
 -Xlog:gc=info \
 -Djava.awt.headless=true \
"

# Runtime toggles (all off by default)
# GC: 'serial' (default) or 'g1'
ENV GC=serial \
    ENABLE_JFR=false \
    ENABLE_JMX_AGENT=false \
    ENABLE_NMT=false \
    JMX_PORT=9404

# Only expose MQTT by default
EXPOSE 1883

# Inline entrypoint to compose flags safely (keeps tools like jcmd happy)
ENTRYPOINT ["bash","-lc", "\
  set -euo pipefail; \
  OPTS=(); \
  # GC selection (default Serial to cut baseline RSS)
  case \"${GC:-serial}\" in \
    serial|'') OPTS+=(\"-XX:+UseSerialGC\");; \
    g1)        OPTS+=(\"-XX:+UseG1GC\");; \
    *)         echo \"Unknown GC=${GC}, expected 'serial' or 'g1'\" >&2; exit 1;; \
  esac; \
  # Optional diagnostics
  if [ \"${ENABLE_NMT:-false}\" = \"true\" ]; then \
    OPTS+=(\"-XX:NativeMemoryTracking=summary\"); \
  fi; \
  if [ \"${ENABLE_JFR:-false}\" = \"true\" ]; then \
    mkdir -p /opt/kal/dumps; \
    OPTS+=(\"-XX:StartFlightRecording=filename=/opt/kal/dumps/kal.jfr,settings=profile,delay=10s,maxsize=256m,maxage=1d,dumponexit=true\"); \
  fi; \
  if [ \"${ENABLE_JMX_AGENT:-false}\" = \"true\" ]; then \
    OPTS+=(\"-javaagent:/opt/jmx/jmx_prometheus_javaagent.jar=${JMX_PORT:-9404}:/opt/jmx/config.yml\"); \
  fi; \
  exec java ${JAVA_TOOL_OPTIONS} \"${OPTS[@]}\" -jar /opt/kal/kal.jar \
"]
