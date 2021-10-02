FROM centos
MAINTAINER beatshadow
ENV mypath /tmp
WORKDIR $mypath
EXPOSE 80
ENTRYPOINT ["curl","-s","http://www.baidu.com"]
ONBUILD RUN echo 'farent onbuild '
# docker build -f /Users/gnehcgnaw/WorkSpaces/IntelliJIdeaPorjects/mall/mall-third-party/dockerfile/Dockerfile4_parent.dockerfile -t centos_parent:1.0 .