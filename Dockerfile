FROM ubuntu:23.10

ENV MAVEN_VERSION "3.3.9"
ENV MAVEN_HOME "/usr/share/maven"
ENV MAVEN_PREFIX "http://archive.apache.org/dist/maven/maven-3"
ENV INSTALL_PREFIX "apt-get install -y --no-install-recommends"

# update
RUN apt update

# Install necessary tools and utilities
RUN $INSTALL_PREFIX zip bzip2 fontconfig curl supervisor nginx wget unzip emacs

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
    && mv src/main/resources/prod.properties src/main/resources/db.properties \
    && mvn clean package

RUN mkdir -p /var/log/supervisor

# put in the correct config
#WORKDIR /Directory-main/target/classes
#RUN mv docker.properties db.properties

# Make port 8080 available to the world outside this container
EXPOSE 8080

#CMD ["/usr/bin/supervisord","-n"]
#CMD ["java", "-jar", "./target/PersonIo.jar >/tmp/log.txt  2>&1"]

WORKDIR /Directory-main
#CMD ["java",  "-jar", "./target/PersonIo.jar",  ">/tmp/log.txt 2>&1"]
#CMD ["java -jar ./target/PersonIo.jar >/tmp/log.txt 2>&1"]
CMD ["/usr/bin/supervisord","-n", "supervisord.conf"]