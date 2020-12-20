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
- 引入docker-maven 插件在deploy(本地测试的时候可以使用install指令) 之后上传到镜像仓库
```
  <properties>
    <!-- 插件版本 -->
    <docker-maven-plugin.version>0.4.13</docker-maven-plugin.version>
    <!-- docker 仓库 -->
    <docker.registry>hanyao94</docker.registry>
    <!-- docker 仓库地址 -->
    <docker.registry.url>hub.docker.com/repositories</docker.registry.url>
  </properties>

  <distributionManagement>
    <!-- maven 仓库配置，deploy 使用 -->
    <repository>
      <id>releases</id>
      <url>http://xxx.xxx.com:8036/repository/maven-releases</url>
    </repository>
   <!-- maven 仓库配置，deploy 使用 -->
    <snapshotRepository>
      <id>snapshots</id>
      <url>http://xxx.xxx.com:8036/repository/maven-snapshots</url>
    </snapshotRepository>
  </distributionManagement>

  <profiles>
  <!-- deploy 插件，部署的时候将打包到maven仓库，同时这个指令执行的时候把镜像推送到镜像仓库-->
    <profile>
      <id>deploy</id>
      <activation>
        <property>
          <name>deploy</name>
        </property>
      </activation>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-deploy-plugin</artifactId>
            <configuration>
              <skip>false</skip>
            </configuration>
          </plugin>
        </plugins>
      </build>
    </profile>

   <!-- docker maven插件，可以根据maven执行进行docker 的操作，比如下面：package 的时候build镜像；deploy的时候推送镜像 -->
    <profile>
      <id>docker</id>
      <activation>
        <property>
          <name>docker</name>
        </property>
      </activation>
      <build>
        <plugins>
          <plugin>
            <groupId>com.spotify</groupId>
            <artifactId>docker-maven-plugin</artifactId>
            <version>${docker-maven-plugin.version}</version>
            <configuration>
              <serverId>hanyao94-dockerhub</serverId>
              <registryUrl>https://${docker.registry.url}</registryUrl>
            </configuration>
            <executions>
              <execution>
                <id>build-image</id>
                <phase>package</phase>
                <goals>
                  <goal>build</goal>
                </goals>
                <configuration>
                  <dockerDirectory>${project.basedir}/src/main/docker</dockerDirectory>  <!-- dockerfile 文件路径 -->
                  <imageName>${docker.registry}/${project.artifactId}</imageName> <!-- 镜像名 -->
                  <imageTags>
                    <imageTag>${project.version}</imageTag>  <!-- 镜像tag，支持多个tag -->
                  </imageTags>
                  <forceTags>true</forceTags>
                  <labels>
                    <label>VERSION=${project.version}</label>
                  </labels>
                  <resources>
                    <resource>
                      <targetPath>/</targetPath>
                      <directory>${project.build.directory}</directory>
                      <include>${project.build.finalName}.jar
                      </include> <!-- 将制品添加到docker build context -->
                    </resource>
                  </resources>
                </configuration>
              </execution>
              <execution>
                <id>push-image</id>
                <phase>deploy</phase>
                <goals>
                  <goal>push</goal>
                </goals>
                <configuration>
                  <serverId>hanyao94-dockerhub</serverId>
                  <imageName>${docker.registry}/${project.artifactId}:${project.version}</imageName><!-- 需要push到仓库的镜像，目前只支持一个 -->
                  <registryUrl>https://${docker.registry.url}</registryUrl>
                  <retryPushCount>3</retryPushCount>
                  <retryPushTimeout>60</retryPushTimeout>
                </configuration>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>
```
> 1. 引入`deploy` 插件和`docker-maven` 插件
> 2. 配置`deploy`插件需要推送的maven库， <distributionManagement>标签底下
> 3. 配置`docker-maven` 插件`build`和`push`指令跟随maven的`package`和`deploy`执行，配置仓库地址、仓库（我使用docker的，可配置自己搭建的仓库），
> 注意：`<serverId>hanyao94-dockerhub</serverId>`配置要和maven的setting.xml文件中的配置的server id 一致
```
	<server>
		<id>hanyao94-dockerhub</id>
		<username>xxx</username>
		<password>xxx</password>
		<configuration>
			<email>xxx.com</email>
		</configuration>
	</server>
```
> 4. 本地deploy或者使用install 一下，查看是否生成镜像且是否推送，值得注意的是插件是放在`<profiles>`标签中，因此执行maven指令的时候记得指定配置：`maven指令 -P docker`，IDEA执行要记得profiles 打勾
> `install(deploy) -Dmaven.test.skip=true -P docker`

- 调整`Action.sh`启动脚本，
 1. 从镜像仓库拉取: `docker pull hanyao94/event:0.0.1-SNAPSHOT`替换`docker build -t event:0.0.2 .`,这边插件已经帮我们打包好制品并推送，我们只需要拉取即可
 2. 引入jmxremote 监控
    1. 映射jmx监听端口： `-p 9003:9003 \`-这两个端口可以保持一致，避免jvisualvm连不上
    2. 添加配置jmx配置: `-e JAVA_JAR="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9003 -Dcom.sun.management.jmxremote.rmi.port=9003 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=192.168.1.100" \`
```
-- 配置详解：

-Djava.rmi.server.hostname指定宿主机的公网ip
-Dcom.sun.management.jmxremote.port用于Java VisualVM远程监控的端口
-Dcom.sun.management.jmxremote.rmi.port指定“用于Java VisualVM远程监控的端口”需要挂载到宿主机的哪个端口
-Dcom.sun.management.jmxremote.authenticate配置是否需要验证，如果true，则在使用Java VisualVM连接的时候需要你认证账号密码
-Dcom.sun.management.jmxremote.ssl不指定ssl
```    
 链接：[https://app.yinxiang.com/fx/d21b1681-da52-4e0a-b83c-3899c5a0c8de](https://app.yinxiang.com/fx/d21b1681-da52-4e0a-b83c-3899c5a0c8de)
 
- 调整`docker-entrypoint.sh`配置，加入JVM相关配置，加入jmxremote监控
1. JVM 性能优化配置：
```
 -Xss1024k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC \
 -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection  \
 -XX:LargePageSizeInBytes=${Xml} -XX:+UseFastAccessorMethods  \
 -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 \
 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/apache-tomcat/logs/hs-dump.hprof \
 -XX:ErrorFile=/apache-tomcat/logs/hs_error%p.log \
```
配置参考链接：[https://app.yinxiang.com/fx/5435f5a1-62b6-45ef-b7a8-e96895e93dde](https://app.yinxiang.com/fx/5435f5a1-62b6-45ef-b7a8-e96895e93dde)

2. 添加jmxremote监控变量：`java $JAVA_JAR  ....`
> 使用 $JAVA_JAR 取到传入的jmxremote配置变量

- 用docker 部署mysql，调整应用envfile的数据库配置指向部署的mysql
1. 使用docker 搭建mysql，记得挂载配置文件和数据库数据的路径到宿主主机，防止重启后数据丢失
> 搭建过程：[https://app.yinxiang.com/fx/104a74fb-9cc4-4f78-a70f-6b28e4dfa23b](https://app.yinxiang.com/fx/104a74fb-9cc4-4f78-a70f-6b28e4dfa23b)

2. 搭建成功后启动，修改envfile 数据源配置为搭建好的数据库:端口为13306
> `spring.datasource.url=jdbc:mysql://192.168.1.100:13306/tms?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=no`

- 阶段二启动方式：
1. 配置好prd生产环境配置：tomcat配置、日志格式输出等
2. 讲Action.sh 和 envfile 文件放到服务器（本地）上同一目录下
3. 服务器上进入这个目录，运行 sh action.sh 变运行起来了；本地使用git bash 窗口找到这个目录运行 sh Action.sh

