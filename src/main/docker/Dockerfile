FROM jolokia/alpine-jre-8

MAINTAINER hanyao.huang@foxmail.com

LABEL PROJECT="event-test" \
      AUTHOR="huanghanyao" \
      COMPANY="Ltd."

WORKDIR /

ADD event*.jar event.jar

EXPOSE 8080 8056 9003

CMD ["java","-jar","event.jar"]