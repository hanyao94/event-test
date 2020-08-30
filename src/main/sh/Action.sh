docker build -t event:0.0.2 .
docker rm -f event-server
docker run -d --restart=always \
           --env-file ./envfile \
           --log-opt max-size=50m \
           -p 8056:8080 \
           -e Xmx=2g  \
           -e Xms=256m  \
           -e Xmn=512m  \
           -e Xml=128m \
           --restart=on-failure:3 \
           -v "E:\docker\log\event-test":/apache-tomcat/logs  \
           --name event-server event:0.0.2
