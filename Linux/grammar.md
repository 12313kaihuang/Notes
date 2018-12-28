## Linux相关命令
  
  
* ##### vim删除文件所有东西  
> 命令模式下键入```:%d```  
* ##### 执行jar文件  
> 首先得安装jdk环境，这个[教程](https://blog.csdn.net/weixin_36241760/article/details/80322639)很容易找,这里随便附上一个。
> 然后cd到jar包所在文件夹目录下  
> ```shell
> nohup java -jar jingdong-1.0.jar >jingdong.txt &
> ```
> `nohup` 意思是不挂断运行命令,当账户退出或终端关闭时,程序仍然运行
> `java -jar` 是基础的执行jar包的命令,而` jingdong-1.0.jar`就是打包好的jar包
> `> jingdong.txt` 是将command的输出重定向到out.file文件，即输出内容不打印到屏幕上，而是输出到out.file文件中。
> `&`代表在后台运行。

