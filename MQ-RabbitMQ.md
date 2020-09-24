# 1 安装rabbitmq
## 1.1 linux安装

    1.安装erlang
        0.安装依赖：
            yum install ncurse-devel 或者
            yum -y install make gcc gcc-c++ kernel-devel m4 ncurses-devel openssl-devel unixODBC-devel
        1.http://www.erlang.org/downloads/20.3 --> http://erlang.org/download/otp_src_20.3.tar.gz
            wget http://erlang.org/download/otp_src_20.3.tar.gz
            tar xf otp_src_20.3.tar.gz
            cd otp_src_20.3
            ./configure --prefix=/usr/local/erlang --with-ssl -enable-threads -enable-smmp-support -enable-kernel-poll --enable-hipe --without-javac
            make 
            make install
            验证：
            cd erlang/bin
            ./erl
            进入shell 
            exit 退出
        2.必要操作--> 添加系统环境变量
            vim /etc/profile
                export PATH=$PATH:/usr/local/erlang/bin
            soucre /etc/profile
    2.安装rabbitmq:
        1.安装python：yum install python -y
        2.安装simplejson：yum install xmlto -y
        3.yum install python-simplejson -y
        4.https://www.rabbitmq.com/install-generic-unix.html 
          wget https://github.com/rabbitmq/rabbitmq-server/releases/download/v3.7.15/rabbitmq-server-generic-unix-3.7.15.tar.xz
            xz -d rabbitmq-server-generic-unix-3.6.3.tar.xz
            tar -xvf rabbitmq-server-generic-unix-3.6.3.tar       
        5.解压可使用
        6.验证：
            cd sbin
            ./rabbirmq-server
            tail -f ..log
        7.关闭： ./rabbitmqctl stop
            ./rabbitmqctl start
            ./rabbitmq-server           --启动
            netstat -nap |grep 5672     --查看端口
            ps -ef |grep rabbitmq
        8.添加系统环境变量 根目录启动
            vim /etc/profile
                添加：若提示错误则分开写
                export PATH=$PATH:/usr/local/ruby/bin;/usr/local/erlang/bin;/usr/local/rabbitmq/sbin
            esc
            :wq
            source /etc/profile
                rabbitmq-server
        9.启用web管理界面 : 端口号默认15672 访问 localhost:15672
          ./rabbitmq-plugins enable rabbitmq_management
          启动
          ./rabbitmq-server -detached
          添加用户
          ./rabbitmqctl add_user admin 111111
          设置权限
          ./rabbitmqctl set_user_tags admin administrator

## 1.2 docker启动
    
    1.指定主机名有利于集群
          docker run -d --hostname localhost --name rabbit-management --restart=always -p 15672:15672 -p 5672:5672 rabbitmq:3.6-management-alpine
          docker run -d --hostname rabbit-host --name rabbitmq -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=pwd -p 15672:15672 -p 5672:5672 rabbitmq:3-management
          docker logs rabbit-management
          访问：http://server-ip:15672  账号： guest 密码： guest
    
# 2 程序远程连接时配置

    1.账号：guest
      密码：guest
      以上只支持本地连接，若要远程需要在服务器配置 
      www.rabbitmq.com/access-control.html
       1.cd /usr/local/rabbitmq/etc/rabbitmq/
       2.vim rabbitmq.config
            [{rabbit, [{loopback_users, []}]}].
       3.重启
    2.www.rabbitmq.com/download.html
    
# 3 RabbitMQ 清除全部队列及消息

    前言
    安装RabbitMQ后可访问：http://{rabbitmq安装IP}:15672使用(默认的是帐号guest，密码guest。此账号只能在安装RabbitMQ的机器上登录，无法远程访问登录。）
    远程访问登录，可以使用自己创建的帐号，给与对应的管理员权限即可。
    
    直接在管理页面删除
    访问http://{rabbitmq安装IP}:15672，登录。
    点击queues，这里可以看到你创建的所有的Queue，
    选中某一个Queue，下方有个Delete Queue删除队列/Purge Message清除消息。
    但是这样只能一个队列一个队列的删除，如果队列中的消息过多就会特别慢。
    
    命令行批量删除
    首先定位到 rabbitMQ 安装目录的sbin 目录下。打开cmd窗口。
    关闭应用的命令为： rabbitmqctl stop_app
    清除的命令为： rabbitmqctl reset
    重新启动命令为： rabbitmqctl start_app
    ps
    查看所有队列命令： rabbitmqctl list_queues
    
## 4 六种模式
    
    简单模式：生产者，一个消费者，一个队列 工作模式：生产者，多个消费者，一个队列
    订阅与发布模式(fanout)：生产者，一个交换机(fanoutExchange)，没有路由规则，多个消费者，多个队列
    路由模式(direct)：生产者，一个交换机(directExchange)，路由规则，多个消费者，多个队列
    主题模式(topic)：生产者，一个交换机(topicExchange)，模糊匹配路由规则，多个消费者，多个队列
    RPC模式：生产者，多个消费者，路由规则，多个队列 总结 一个队列，一条消息只会被一个消费者消费（有多个消费者的情况也是一样的）。
    订阅模式，路由模式，主题模式，他们的相同点就是都使用了交换机，只不过在发送消息给队列时，添加了不同的路由规则。订阅模式没有路由规则，路由模式为完全匹配规则，主题模式有正则表达式，完全匹配规则。
    在订阅模式中可以看到一条消息被多个消费者消费了，不违背第一条总结，因为一条消息被发送到了多个队列中去了。
    在交换机模式下：队列和路由规则有很大关系 在有交换机的模式下：3，4，5模式下，生产者只用关心交换机与路由规则即可，无需关心队列
    消费者不管在什么模式下：永远不用关心交换机和路由规则，消费者永远只关心队列，消费者直接和队列交互
