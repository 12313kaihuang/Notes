## Linux相关命令
  [同步简书](https://www.jianshu.com/p/736853240fdb)  
  
* vim删除文件所有东西  
> 命令模式下键入```:%d```  
* 执行jar文件  
> 首先得安装jdk环境，这个[教程](https://blog.csdn.net/weixin_36241760/article/details/80322639)很容易找,这里随便附上一个。
> 然后cd到jar包所在文件夹目录下  
> ```shell
> nohup java -jar jingdong-1.0.jar >jingdong.txt &
> ```
> `nohup` 意思是不挂断运行命令,当账户退出或终端关闭时,程序仍然运行  
> `java -jar` 是基础的执行jar包的命令,而` jingdong-1.0.jar`就是打包好的jar包  
> `> jingdong.txt` 是将command的输出重定向到out.file文件，即输出内容不打印到屏幕上，而是输出到out.file文件中。  
> `&`代表在后台运行。    
* 停止jar等进程  
> 首先找到进程的**pid**
> ```shell
> netstat -tunlp  
> //当然也可以直接检索端口
> netstat -tunlp | grep 8082
> ```
> `-t` (tcp)仅显示tcp相关的选项    
> `-u` (udp)仅显示udp相关选项  
> `-n` 拒绝显示别名，能显示数字的全部转化成数字  
> `-l` 仅列出有在Listen(监听)的服务状态  
> `-p` 显示建立相关链接的程序名  
* 查看**tomcat**等程序占用的端口
> ```shell
> ps -aux | grep tomcat
> ```
> 这条命令与上一条命令有什么区别暂时没搞清楚  
