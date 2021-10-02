# 如果版本不是releases,from的时候要加上版本，具体如下所示
FROM centos_parent:1.0
MAINTAINER beatshadow
ENV mypath /tmp
WORKDIR $mypath
EXPOSE 80
ENTRYPOINT ["curl","-s","http://www.baidu.com"]
# docker build -f /Users/gnehcgnaw/WorkSpaces/IntelliJIdeaPorjects/mall/mall-third-party/dockerfile/Dockerfile4_son.dockerfile -t centos_son:1.0 .