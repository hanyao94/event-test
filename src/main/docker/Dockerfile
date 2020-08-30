FROM jolokia/alpine-jre-8

MAINTAINER hanyao.huang@foxmail.com

LABEL PROJECT="event-test" \
      AUTHOR="huanghanyao" \
      COMPANY="Ltd."

WORKDIR /opt/app

ENV LC_ALL en_US.UTF-8
COPY docker-entrypoint.sh /opt/app
RUN chmod +x /opt/app/docker-entrypoint.sh
ADD event*.jar event.jar

EXPOSE 8080 8443 9003

CMD ["/opt/app/docker-entrypoint.sh"]