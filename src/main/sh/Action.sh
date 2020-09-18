docker pull hanyao94/event:0.0.1-SNAPSHOT
docker rm -f event-server
docker run -d --restart=always \
           --env-file ./envfile \
           --log-opt max-size=50m \
           -p 8056:8080 \
           -p 9003:9003 \
           -e JAVA_JAR="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9003 -Dcom.sun.management.jmxremote.rmi.port=9003 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=192.168.1.100" \
           -e Xmx=2g  \
           -e Xms=256m  \
           -e Xmn=512m  \
           -e Xml=128m \
           --restart=on-failure:3 \
           -v "E:\docker\log\event-test":/apache-tomcat/logs  \
           --name event-server hanyao94/event:0.0.1-SNAPSHOT
