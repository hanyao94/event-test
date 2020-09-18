 docker rm -f my-mysql
 docker run -p 13306:3306 --name my-mysql \
        -v "E:\docker\mysql\conf":/ect/mysql \
        -v "E:\docker\mysql\data":/var/lib/mysql \
        -e MYSQL_ROOT_PASSWORD=root -d mysql:5.7.26

