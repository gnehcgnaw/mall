FROM centos
MAINTAINER beatshadow
ENV mypath /tmp
WORKDIR $mypath
EXPOSE 80
RUN yum -y install vim
RUN yum -y install net-tools
CMD echo $mypath
CMD echo 'success ......ok'
CMD /bin/bash