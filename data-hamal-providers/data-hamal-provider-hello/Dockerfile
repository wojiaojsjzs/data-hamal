FROM striveonger/java:17
LABEL maintainer="striveonger@163.com"
LABEL version="0.0.1"

# 创建应用目录
RUN mkdir /opt/app/

COPY target/data-hamal-provider-hello.jar /opt/app/

CMD java -Xms512m -Xmx512m -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=1024m \
         -XX:ParallelGCThreads=4 -XX:+HeapDumpOnOutOfMemoryError \
         -Dfile.encoding=utf-8 \
         -Dspring.profiles.active=docker \
         -jar /opt/app/data-hamal-provider-hello.jar \
 && tail -f logs/console.log


