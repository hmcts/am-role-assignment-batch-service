# Keep hub.Dockerfile aligned to this file as far as possible
ARG JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"
ARG APP_INSIGHTS_AGENT_VERSION=2.5.1

# Application image

FROM hmctspublic.azurecr.io/base/java:openjdk-11-debug-distroless-1.4
LABEL maintainer="https://github.com/hmcts/role-assignment-batch-service"

COPY lib/AI-Agent.xml /opt/app/
COPY build/libs/am-role-assignment-batch-service.jar /opt/app/

EXPOSE 4099
CMD [ "am-role-assignment-batch-service.jar" ]
