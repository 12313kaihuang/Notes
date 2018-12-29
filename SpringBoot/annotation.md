## SpringBoot 相关注解
1. Controller注解  
`@Controller`表示这个一个控制器，像是一个容器，里面可以加入多个url映射，映射到指定的页面。一般习惯于一个页面对应一个Controller，里面写入那个页面所有的url映射。
```java
@Controller  
public class IndexController {
	
}
```
