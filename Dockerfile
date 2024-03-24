FROM ubuntu:23.10

ENV MAVEN_VERSION "3.3.9"
ENV MAVEN_HOME "/usr/share/maven"
ENV MAVEN_PREFIX "http://archive.apache.org/dist/maven/maven-3"
ENV INSTALL_PREFIX "apt-get install -y --no-install-recommends"

# update
RUN apt update

# Install necessary tools and utilities
RUN $INSTALL_PREFIX zip bzip2 fontconfig curl supervisor nginx wget unzip

# install java
RUN $INSTALL_PREFIX openjdk-21-jdk

# install maven
RUN curl -fsSL $MAVEN_PREFIX/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
  | tar xzf - -C /usr/share \
  && mv /usr/share/apache-maven-$MAVEN_VERSION /usr/share/maven \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# copy files from cwd to docker
RUN wget https://github.com/harpoon4530/Directory/archive/refs/heads/main.zip \
    && unzip main.zip  \
    && cd Directory-main  \
    && mvn clean package

RUN cd /Directory-main/src/main/resources \
    && mv docker.properties db.properties

#EXPOSE
#ENTRYPOINT

CMD ["/usr/bin/supervisord","-n"]
