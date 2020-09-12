#### 目录：
- docker  -docker文件
- java    -源码文件
- resources -配置文件
- sh        -部署用的脚本文件和环境变量 

#### 手动制作springboot项目阶段一：
- 编写Dockerfile
```
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
```
>1. 使用镜像为docker仓库的alpine-jre-8 带有jre8环境的alpine(微型linux)环境
>2. WORKDIR 工作目录为容器的工作目录
>3. `ENV LC_ALL en_US.UTF-8` --容器字体包：['en_US.UTF-8','zh_CN.UTF-8']
>4. 编写并复制`docker-entrypoint.sh` docker启动脚本
>5. `chmod +x docker-entrypoint.sh` -- linux更改脚本的执行权限
>6. 暴露端口：8080 8443 为tomcat 服务器端口(生产环境配置端口：8080)  9003->监控服务端口


- 编写 `docker-entrypoint.sh` docker容器启动脚本
```
#!/bin/sh
java -server -Xmx${Xmx} -Xms${Xms} -Xmn${Xmn} \
     -XX:ErrorFile=/apache-tomcat/logs/hs_error%p.log \
     -Dfile.encoding=UTF-8  \
     -Dlogging.path=/apache-tomcat/logs \
     -Dlogging.config=classpath:logback-prd.xml \
     -jar /opt/app/event.jar
```
>1. `#!/bin/sh` 脚本启动必写,alpine使用`#!/bin/sh`,ubuntu(centos): `#!/bin/bash`
>2. `-server` 以server模式启动（另一种是client模式）
>3. `-Xmx | -Xms | -Xmn` 设置最大堆内存大小1024倍数 | 设置初始化堆内存大小 1024倍数 | 设置初始和最大的年轻代大小 |
>4. `-XX` JVM 配置，具体参考：[https://app.yinxiang.com/fx/064cea46-7857-4d51-8c4f-96e761ab3ee1](https://app.yinxiang.com/fx/064cea46-7857-4d51-8c4f-96e761ab3ee1)
>5. `-D `开头的是系统变量参数：` -Dfile.encoding`:文件编码 | `-Dlogging.path`: springboot日志路径(prd.yml配置文件有体现) | `-Dlogging.config`:springboot日志的配置文件
>6. `-jar /opt/app/event.jar` java 以jar包的方式启动一个jar


- 编写envfile 环境变量(最终会覆盖yml的配置)
```
spring.datasource.url=jdbc:mysql://192.168.1.100:3306/tms?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=no
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.druid.max-active=100
logging.level.com.seven.event=INFO
```
> 指定数据库相关配置以及日志的级别

- 编写启动脚本: `Action.sh`
```
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
```
>1. `docker build -t event:0.0.2 .` 本地先build出镜像（阶段二会使用maven组件，install直接push到镜像仓库，这边会换成pull指令拉取镜像）
>2. `docker rm -f event-server` 删除容器
>3. `docker run 启动容器`配置参数：`-d --restart=always`: 后台启动并且失败重启 | `--restart=on-failure:3`:重启失败次数：3 |
>`--env-file ./envfile`: 环境变量文件 | `--log-opt max-size=50m` 日志配置选项，单文件最大值50m | `-p 8056:8080` 映射容易端口为8056(容器内程序的端口是8080) |
>`-e Xmx=2g  -e Xms=256m -e Xmn=512m -e Xml=128m`: JVM堆栈配置 | `-v "E:\docker\log\event-test":/apache-tomcat/logs`: 挂在日志文件到宿主主机目录（主机为windows需要加双引号） |
>`--name event-server event:0.0.2`: 容器名称已经要启动的镜像
> 关于JVM 堆栈配置请参考：[https://app.yinxiang.com/fx/064cea46-7857-4d51-8c4f-96e761ab3ee1](https://app.yinxiang.com/fx/064cea46-7857-4d51-8c4f-96e761ab3ee1)

- 阶段一启动方式：
1. 配置好prd生产环境配置
2. install 工程，得到 jar文件
3. 将`Dockerfile` `docker-entrypoint.sh` `xx.jar` `envfile` `Action.sh` 放到同一目录下
4. git bash 窗口，运行 sh action.sh 变运行起来了
5. Windows下制定日志窗口docker会弹出是否share it 操作，点击确认即可

#### 手动制作springboot项目阶段二：
- 引入docker-maven 插件在install 之后上传到镜像仓库


- 调整`Action.sh`启动脚本，
 1. 从镜像仓库拉取
 2. 引入jmxremote 监控

- 用docker 部署mysql，调整应用envfile的数据库配置指向部署的mysql

- 调整`docker-entrypoint.sh`配置，加入JVM相关配置，加入jmxremote监控


