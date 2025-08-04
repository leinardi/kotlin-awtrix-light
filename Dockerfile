ARG KAL_VERSION=unspecified
FROM gcr.io/distroless/java17-debian12:nonroot AS kal

ARG KAL_VERSION

WORKDIR /opt/kal

COPY kal/build/libs/kal-fat-${KAL_VERSION}.jar ./kal.jar

EXPOSE 1883

ENTRYPOINT ["java","-jar","/opt/kal/kal.jar"]
