# get base image
FROM openjdk:17-jdk-oraclelinux8

# Arguments
ARG PORT=7000

# Environment Variables
ENV QATCH_HOME=/qatch
ENV QSERVICE_HOME=/qservice

# set the maintainer
MAINTAINER "Isaac D. Griffith, Ph.D."

# create working directory
WORKDIR ./

# copy components
COPY ./build/distributions/qservice-2.0.0.zip .
COPY ../app/cli/build/distributions/qatch-2.0.0.zip .

# Install dependencies
RUN microdnf install zip unzip -y
# Install Maven and Gradle
RUN wget -0 https://gradle.org/next-steps/?version=7.5&format=bin
RUN mkdir -p /opt/gradle
RUN unzip -d /opt/gradle gradle-7.5-bin.zip

RUN wget -0 https://dlcdn.apache.org/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz
RUN mkdir -p /opt/maven
RUN unzip -d /opt/maven apache-maven-3.8.6-bin.zip

# Setup Maven Home and Gradle Home
RUN export PATH=$PATH:/opt/gradle/gradle-7.5/bin
RUN export GRADLE_HOME=/opt/gradle/gradle-7.5

RUN export PATH=$PATH:/opt/maven/apache-maven-3.8.6/bin
RUN export MAVEN_HOME=/opt/maven/apache-maven-3.8.6

# Need to install pylint, and enchant
RUN microdnf install python36
RUN pip3 install pylint

# install the components
RUN unzip qservice-2.0.0.zip
RUN mv qservice-2.0.0 qservice
RUN mkdir qservice/conf
RUN unzip qatch-2.0.0.zip
RUN mv qatch-2.0.0 qatch
RUN chmod +x qatch/tools/pmd-bin-6.47.0/bin/run.sh
RUN chmod +x qatch/tools/ckjm-ext/bin/ckjm-ext
RUN chmod +x qatch/tools/ckpm/ck4py

RUN export QATCH_HOME=/qatch
RUN export QSERVICE_HOME=/qservice

# Clean up
RUN rm *.zip

# expose the networking port
EXPOSE $PORT

# create the user so root is not running the app
RUN useradd -m appuser
USER appuser

$ Setup the entrypoint
ENTRYPOINT ./qservice/bin/server -p $PORT
