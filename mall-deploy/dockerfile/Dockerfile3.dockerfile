FROM centos
MAINTAINER beatshadow
ENV mypath /tmp
WORKDIR $mypath
EXPOSE 80
ENTRYPOINT ["curl","-s","http://www.baidu.com"]
# ENTRYPOINT 追加命令参数
# docker run -it 743ce2faac70 -i