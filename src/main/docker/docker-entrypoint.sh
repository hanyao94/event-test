#!/bin/sh
java -server -Xmx${Xmx} -Xms${Xms} -Xmn${Xmn} \
     -XX:ErrorFile=/apache-tomcat/logs/hs_error%p.log \
     -Dfile.encoding=UTF-8  \
     -Dlogging.path=/apache-tomcat/logs \
     -Dlogging.config=classpath:logback-prd.xml \
     -Dspring.profiles.active=prd \
     -jar /opt/app/event.jar
