package org.example;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 01.Windows安装VMware-14
 *     1.百度吧
 * 02.VMware安装CentOS-7
 *     1.内存:4GiB
 *     2.处理器:1
 *     3.CD/DVD:CentOS-1804
 *     4.网络适配器:NAT
 *     5.USB控制器:存在
 *     6.声卡:自动检测
 *     7.显示器:自动检测
 *     a.Date&Time(选择上海)
 *     b.Keyboard(选择us)
 *     c.Language Support(选择us)
 *     d.Installation Source(默认)
 *     e.Software Selection(默认)
 *     f.Installation Destination(/-10 GiB,/home-4096 MiB,/boot-2048 MiB,/var-4096 MiB,swap-4096 MiB)
 *     g.KDump(默认)
 *     h.Network&HostName(Open)
 *     i.Security Policy(默认)
 * 03.CentOS-7安装Docker-18.x
 *     0.查看CentOS系统相关信息
 *         # 通过 uname -r 命令查看你当前的内核版本
 *         TODO ->$ uname -r
 *         3.10.0-1160.el7.x86_64
 *         # 使用 root 权限登录 Centos。确保 yum 包更新到最新。
 *         TODO ->$ sudo yum update
 *     1.使用官方安装脚本自动安装
 *         安装命令如下：curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun
 *         也可以使用国内 daocloud 一键安装命令：curl -sSL https://get.daocloud.io/docker | sh
 *     2.手动安装
 *         卸载旧版本
 *         较旧的Docker版本称为docker或docker-engine。如果已安装这些程序，请卸载它们以及相关的依赖项。
 *     3.安装 Docker Engine-Community
 *         # 使用 Docker 仓库进行安装
 *         # 在新主机上首次安装 Docker Engine-Community 之前，需要设置 Docker 仓库。之后，您可以从仓库安装和更新 Docker。
 *         # 设置仓库
 *         01.安装所需的软件包。yum-utils 提供了 yum-config-manager ，并且 device mapper 存储驱动程序需要 device-mapper-persistent-data 和 lvm2。
 *         TODO ->$ sudo yum install -y yum-utils device-mapper-persistent-data lvm2
 *         02.使用以下命令来设置稳定的仓库。
 *         # 使用官方源地址（比较慢）
 *         TODO ->$ sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
 *         02.可以选择国内的一些源地址：
 *         # 阿里云
 *         TODO ->$ sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
 *         # 清华大学源
 *         TODO ->$ sudo yum-config-manager --add-repo https://mirrors.tuna.tsinghua.edu.cn/docker-ce/linux/centos/docker-ce.repo
 *         03.安装最新版本的 Docker Engine-Community
 *         # 安装最新版本的 Docker Engine-Community 和 containerd，或者转到下一步安装特定版本：
 *         # $ sudo yum install docker-ce docker-ce-cli containerd.io
 *         03.安装特定版本的 Docker Engine-Community，请在存储库中列出可用版本，然后选择并安装：
 *         1、列出并排序您存储库中可用的版本。此示例按版本号（从高到低）对结果进行排序。
 *         TODO ->$ yum list docker-ce --showduplicates | sort -r
 *         TODO ->$ yum list docker-ce --showduplicates | sort -r
 *         # docker-ce.x86_64            18.06.3.ce-3.el7                    docker-ce-stable
 *         # docker-ce.x86_64            18.06.2.ce-3.el7                    docker-ce-stable
 *         # docker-ce.x86_64            18.06.1.ce-3.el7                    docker-ce-stable
 *         # docker-ce.x86_64            18.06.0.ce-3.el7                    docker-ce-stable
 *         # docker-ce.x86_64            18.03.1.ce-1.el7.centos             docker-ce-stable
 *         # docker-ce.x86_64            18.03.0.ce-1.el7.centos             docker-ce-stable
 *         # docker-ce.x86_64            17.12.1.ce-1.el7.centos             docker-ce-stable
 *         # docker-ce.x86_64            17.12.0.ce-1.el7.centos             docker-ce-stable
 *         # docker-ce.x86_64            17.09.1.ce-1.el7.centos             docker-ce-stable
 *         # docker-ce.x86_64            17.09.0.ce-1.el7.centos             docker-ce-stable
 *         # docker-ce.x86_64            17.06.2.ce-1.el7.centos             docker-ce-stable
 *         # docker-ce.x86_64            17.06.1.ce-1.el7.centos             docker-ce-stable
 *         # docker-ce.x86_64            17.06.0.ce-1.el7.centos             docker-ce-stable
 *         # docker-ce.x86_64            17.03.3.ce-1.el7                    docker-ce-stable
 *         # docker-ce.x86_64            17.03.2.ce-1.el7.centos             docker-ce-stable
 *         # docker-ce.x86_64            17.03.1.ce-1.el7.centos             docker-ce-stable
 *         # docker-ce.x86_64            17.03.0.ce-1.el7.centos             docker-ce-stable
 *         2、通过其完整的软件包名称安装特定版本，该软件包名称是软件包名称（docker-ce）加上版本字符串（第二列），从第一个冒号（:）一直到第一个连字符，并用连字符（-）分隔。例如：docker-ce-18.09.1。
 *         TODO ->$ sudo yum install docker-ce-<VERSION_STRING> docker-ce-cli-<VERSION_STRING> containerd.io
 *         TODO ->$ sudo yum install docker-ce-18.06.3.ce-3.el7
 *         3、启动 Docker。启动并加入开机启动
 *         TODO ->$ sudo systemctl start docker
 *         TODO ->$ sudo systemctl enable docker
 *         # 下面打印这句话很重要，是告诉你Docker安装目录。
 *         Created symlink from /etc/systemd/system/multi-user.target.wants/docker.service to /usr/lib/systemd/system/docker.service.
 *         # 验证安装是否成功(有client和service两部分表示docker安装启动都成功了)
 *         TODO ->$ docker version
 *         # Client:
 *         # Version:           18.06.3-ce
 *         # API version:       1.38
 *         # Go version:        go1.10.3
 *         # Git commit:        d7080c1
 *         # Built:             Wed Feb 20 02:26:51 2019
 *         # OS/Arch:           linux/amd64
 *         # Experimental:      false
 *         #
 *         # Server:
 *         # Engine:
 *         # Version:          18.06.3-ce
 *         # API version:      1.38 (minimum version 1.12)
 *         # Go version:       go1.10.3
 *         # Git commit:       d7080c1
 *         # Built:            Wed Feb 20 02:28:17 2019
 *         # OS/Arch:          linux/amd64
 *         # Experimental:     false
 *         4、通过运行 hello-world 映像来验证是否正确安装了 Docker Engine-Community 。
 *         TODO ->$ sudo docker run hello-world
 *     4.Docker 常用相关操作命令
 *         00.查看镜像列表。
 *         # [root@localhost ~]# docker images
 *         # REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
 *         # hello-world         latest              feb5d9fea6a5        2 weeks ago         13.3kB
 *         # gitlab/gitlab-ce    13.12.12-ce.0       738ea2e05451        2 weeks ago         2.25GB
 *         00.查看启动容器。
 *         # [root@localhost ~]# docker ps
 *         # CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
 *         00.查看历史启动容器。
 *         # [root@localhost ~]# docker ps -a
 *         # CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS                      PORTS               NAMES
 *         # 0f794dccef1f        hello-world         "/hello"            21 minutes ago      Exited (0) 21 minutes ago                       vigorous_johnson
 *         # d4105e93fb61        hello-world         "/hello"            21 minutes ago      Exited (0) 21 minutes ago                       reverent_hodgkin
 *         # 3d3ed502db27        hello-world         "/hello"            21 minutes ago      Exited (0) 21 minutes ago                       hopeful_mestorf
 *         01.删除镜像。
 *         02.删除容器。
 *         03.重启Docker。
 *         04.安装GitLab
 * 04.Docker-18.x安装GitLab-ce-13.x
 *     1.拉取镜像。
 *         # 区分CE版本和EE版本。
 *         TODO ->$ docker pull gitlab/gitlab-ce:13.0.0-ce.0
 *         TODO ->$ docker pull gitlab/gitlab-ce:13.12.12-ce.0
 *         # 从8.x版本到目前14.x版本可选。
 *         TODO ->$ docker pull gitlab/gitlab-ee:8.0.0-ee.0
 *         TODO ->$ docker pull gitlab/gitlab-ee:8.17.8-ee.0
 *         TODO ->$ docker pull gitlab/gitlab-ee:14.0.0-ee.0
 *         TODO ->$ docker pull gitlab/gitlab-ee:14.3.2-ee.0
 *         # 13.12.12-ce.0: Pulling from gitlab/gitlab-ce
 *         # 35807b77a593: Pull complete
 *         # 3af31d463a40: Pull complete
 *         # 8d15a5358839: Pull complete
 *         # 9f16925130c5: Pull complete
 *         # bea3f899bed6: Pull complete
 *         # 69fd839fa357: Pull complete
 *         # e9d368f32089: Pull complete
 *         # Digest: sha256:45688347241db321d9a2b518b454ee3e3b0a983d9d784d129aa195979ddb121a
 *         # Status: Downloaded newer image for gitlab/gitlab-ce:13.12.12-ce.0
 *     2.通常会将GitLab的配置(config)/日志(logs)/数据(data)放到容器之外，便于日后升级，因此请先准备这三个目录。
 *         # 官方链接教程：
 *         # https://docs.gitlab.com/ee/install/docker.html
 *         # 设置卷位置
 *         # 在设置其他所有内容之前，请配置一个新的环境变量，$GITLAB_HOME 指向配置、日志和数据文件所在的目录。确保该目录存在并且已授予适当的权限。
 *         # 对于 Linux 用户，将路径设置为/srv/gitlab：
 *         # export GITLAB_HOME=/srv/gitlab
 *         # $GITLAB_HOME/data      /var/opt/gitlab    用于存储应用程序数据。
 *         # $GITLAB_HOME/logs      /var/log/gitlab    用于存储日志。
 *         # $GITLAB_HOME/config    /etc/gitlab        用于存储 GitLab 配置文件。
 *         TODO ->$ mkdir -p /srv/gitlab/config
 *         TODO ->$ mkdir -p /srv/gitlab/logs
 *         TODO ->$ mkdir -p /srv/gitlab/data
 *     3.使用 Docker 引擎安装 GitLab
 *         # 您可以微调这些目录以满足您的要求。设置GITLAB_HOME变量后，您可以运行图像：
 *         TODO ->$ sudo docker run --detach --hostname gitlab.example.com --publish 443:443 --publish 80:80 --publish 22:22 --name gitlab --restart always --volume $GITLAB_HOME/config:/etc/gitlab --volume $GITLAB_HOME/logs:/var/log/gitlab --volume $GITLAB_HOME/data:/var/opt/gitlab gitlab/gitlab-ee:latest
 *         TODO ->$ sudo docker run --detach --hostname 192.168.11.130 --publish 8443:443 --publish 8880:80 --publish 8822:22 --name gitlab --restart always --volume /srv/gitlab/config:/etc/gitlab --volume /srv/gitlab/logs:/var/log/gitlab --volume /srv/gitlab/data:/var/opt/gitlab gitlab/gitlab-ce:13.12.12-ce.0
 *         # 5fff7df1959dae6950786895824ab7ca84371b8e344409731563f6894b07d7d2
 *         # [root@localhost ~]# docker images
 *         # REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
 *         # hello-world         latest              feb5d9fea6a5        2 weeks ago         13.3kB
 *         # gitlab/gitlab-ce    13.12.12-ce.0       738ea2e05451        2 weeks ago         2.25GB
 *         # [root@localhost ~]# docker ps
 *         # CONTAINER ID        IMAGE                            COMMAND             CREATED             STATUS                             PORTS                                                               NAMES
 *         # 5fff7df1959d        gitlab/gitlab-ce:13.12.12-ce.0   "/assets/wrapper"   38 seconds ago      Up 37 seconds (health: starting)   0.0.0.0:8822->22/tcp, 0.0.0.0:8880->80/tcp, 0.0.0.0:8443->443/tcp   gitlab
 *     4.配置GitLab主机名
 *         1、修改/mnt/gitlab/etc/gitlab.rb把external_url改成部署机器的域名或者IP地址
 *         TODO ->$ vi /srv/gitlab/config/gitlab.rb
 *         ## GitLab configuration settings
 *         ## GitLab URL
 *         # external_url 'GENERATED_EXTERNAL_URL'
 *         external_url 'http://192.168.11.130'
 *         ## Roles for multi-instance GitLab
 *         # roles ['redis_sentinel_role', 'redis_master_role']
 *         ## Legend
 *         ################################################################################
 *         ################################################################################
 *         ##                Configuration Settings for GitLab CE and EE                 ##
 *         ################################################################################
 *         ################################################################################
 *         2、修改/mnt/gitlab/data/gitlab-rails/etc/gitlab.yml，其实不修改已经可以正常访问GitLab地址。
 *         TODO ->$ vi /srv/gitlab/data/gitlab-rails/etc/gitlab.yml
 *         ## This file is managed by gitlab-ctl. Manual changes will be
 *         ## erased! To change the contents below, edit /etc/gitlab/gitlab.rb
 *         ## and run `sudo gitlab-ctl reconfigure`.
 *         #production: &base
 *         #  #
 *         #  # 1. GitLab app settings
 *         #  # ==========================
 *         #  ## GitLab settings
 *         #  gitlab:
 *         #    ## Web server settings (note: host is the FQDN, do not include http://)
 *         #    host: 192.168.11.130
 *         #    port: 80
 *         #    https: false
 *         3、访问地址：
 *         http://192.168.11.130:8880/
 *
 * http://localhost:8283/swagger-ui/
 * @author Administrator
 * @since 2021-09-11
 */
@SpringBootApplication
@Log4j2
public class DemoSwagger3Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoSwagger3Application.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }
}
