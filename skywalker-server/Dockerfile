FROM java:8
MAINTAINER WEIZH

RUN mkdir -p /usr/local/skywalker
RUN echo $JAVA_HOME

ADD  ./target/skywalker-server-0.0.1-SNAPSHOT.jar /usr/local/skywalker/skywalker.jar
ADD run.sh run.sh

RUN chmod +x run.sh
CMD ./run.sh

