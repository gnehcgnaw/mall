FROM centos
MAINTAINER beatshadow
ENV mypath /tmp
WORKDIR $mypath
EXPOSE 80
CMD ["curl","-s","http://www.baidu.com"]

# docker run -it 22eb6f662929 -i
  #docker: Error response from daemon: OCI runtime create failed: container_linux.go:380: starting container process caused: exec: "-i": executable file not found in $PATH: unknown.