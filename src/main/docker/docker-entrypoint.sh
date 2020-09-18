#!/bin/sh
java $JAVA_JAR -server -Xmx${Xmx} -Xms${Xms} -Xmn${Xmn} \
     -Xss1024k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC \
     -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection  \
     -XX:LargePageSizeInBytes=${Xml} -XX:+UseFastAccessorMethods  \
     -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 \
     -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/apache-tomcat/logs/hs-dump.hprof \
     -XX:ErrorFile=/apache-tomcat/logs/hs_error%p.log \
     -Dfile.encoding=UTF-8  \
     -Dlogging.path=/apache-tomcat/logs \
     -Dlogging.config=classpath:logback-prd.xml \
     -Dspring.profiles.active=prd \
     -jar /opt/app/event.jar
