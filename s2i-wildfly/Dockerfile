# This image provides a base for building and running WildFly applications.
# It builds using maven and runs the resulting artifacts on WildFly 15.0.1 Final

FROM wildfly-s2i-m2

MAINTAINER Ben Parees <bparees@redhat.com>

EXPOSE 8080

ENV WILDFLY_VERSION=16.0.0.Final \
    MAVEN_VERSION=3.6.0 \
    GALLEON_VERSION=4.0.0.Alpha1-SNAPSHOT

LABEL io.k8s.description="Platform for building and running JEE applications on WildFly 16.0.0.Final" \
      io.k8s.display-name="WildFly 16.0.0.Final" \
      io.openshift.expose-services="8080:http" \
      io.openshift.tags="builder,wildfly,wildfly15" \
      io.openshift.s2i.assemble-input-files="/wildfly/standalone/deployments;/wildfly/standalone/configuration;/wildfly/provided_modules" \
      io.openshift.s2i.destination="/opt/s2i/destination" \
      com.redhat.deployments-dir="/wildfly/standalone/deployments" \
      maintainer="Ben Parees <bparees@redhat.com>"

# Install Maven, Wildfly
RUN INSTALL_PKGS="tar unzip bc which lsof java-11-openjdk java-11-openjdk-devel" && \
    yum install -y --enablerepo=centosplus $INSTALL_PKGS && \
    rpm -V $INSTALL_PKGS && \
    yum clean all -y && \
    (curl -v https://www.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | \
    tar -zx -C /usr/local) && \
    ln -sf /usr/local/apache-maven-$MAVEN_VERSION/bin/mvn /usr/local/bin/mvn && \
    mkdir -p $HOME/.m2 && \
    mkdir -p /opt/s2i/destination

RUN curl -sL -0 https://github.com/jfdenise/galleon-openshift/releases/download/1.0/galleon-${GALLEON_VERSION}.zip -o /tmp/galleon-${GALLEON_VERSION}.zip && \
    unzip /tmp/galleon-${GALLEON_VERSION}.zip -d /usr/local/ && \
    rm /tmp/galleon-${GALLEON_VERSION}.zip && \
    mv /usr/local/galleon-${GALLEON_VERSION} /usr/local/galleon && \
    ln -sf /usr/local/galleon/bin/galleon.sh /usr/local/galleon/bin/galleon

ENV PATH=/usr/local/galleon/bin:$PATH

# Build WildFly server
COPY ./galleon /tmp/galleon
RUN mvn clean install -f /tmp/galleon/pom.xml -Dmaven.repo.local=$HOME/.m2/repository && \
    cp -r /tmp/galleon/build/target/wildfly-thin /wildfly && rm -rf /tmp/galleon

# Add s2i wildfly customizations
ADD ./contrib/wfbin/standalone.conf /wildfly/bin/standalone.conf
ADD ./contrib/settings.xml $HOME/.m2/

# COPY Additional files,configurations that we want to ship by default, like a default setting.xml
# s2i path set in base image via LABEL: /usr/local/s2i
# Copy s2i assembly scripts that is called by galleon s2i.
# Copy the S2I scripts from the specific language image to $STI_SCRIPTS_PATH
COPY ./s2i/bin/ $STI_SCRIPTS_PATH
RUN cp -prf $STI_SCRIPTS_PATH /usr/local/s2i-original
COPY ./s2i/bin-galleon/ $STI_SCRIPTS_PATH
# Outputs will be left under this directory
RUN mkdir -p /output/deployments && mkdir -p /output/wildfly && \
    chown -R 1001:0 /output && chmod -R ug+rwX /output
RUN chown -R 1001:0 /wildfly && chown -R 1001:0 $HOME && \
    chmod -R ug+rwX /wildfly && \
    chmod -R g+rw /opt/s2i/destination

USER 1001
