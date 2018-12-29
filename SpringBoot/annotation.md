## SpringBoot 相关注解:blue_heart:
1. **Controller**   
`@Controller`表示这个一个控制器，像是一个容器，里面可以加入多个url映射，映射到指定的页面。一般习惯于一个页面对应一个Controller，里面写入那个页面所有的url映射。
```java
@Controller  
public class IndexController {
	
}
```
  如果确定这个Controller内返回的都是**以json格式返回数据而不是页面**的话，可以直接使用`@RestController`。  
2. **RequestMapping**  
表示这是一个**url映射**，如果要映射多个url可以通过value属性设置。一个Controller里面可以写入多个mapping映射。  
```java
@Controller  
public class IndexController {

    @RequestMapping("/login")   //表示访问/login.html时会显示Login.html,后缀名是默认的，也可以在配置文件中去设置。
    public String login(){
        return "Login";
    }

     //多映射的写法
    @RequestMapping(value = {"login.html","Login.html"})
    public String login(){
        return "Login";
    }
}
```  
`@RequestMapping`不限制请求的方式，如果需要通过相同的url不同的请求方式来区分的话可以这样写mapping注解：
```java
@Controller  
public class IndexController {

    @GetMapping("/login")   //映射/login.html的get请求
    public String login(){
        return "Login";
    }

    @PostMapping("/login")   //映射/login.html的post请求
    public String login(){
        return "Login";
    }

}
```  
除了post和get的外，还有`@DeleteMapping`、`@PutMapping`。  
3. **Configuration**  
`@Configuration`表示这是一个配置。  
4. **Bean**  
 `@Bean`表示这是一个bean容器。  
5. **Component**  
`@Component`表示这是一个组件。  
6. **JsonProperty**
 `@JsonProperty`用来指定对象属性当做json返回时所对应的**key**的值。一般标注在get方法上面，例如:  
```java
class User {
 
   private int id;

   public User(int id){
      this.id = id;
   }

   @JsonProperty("userId")  //指定返回json数据时的key值
   publica int getId(){
     return id;
   }

}

@RestController  
public class IndexController {

    @GetMapping("/user")   
    public User login(){
        return new User(1);
    }

    //如果标注的Controller注解那么需要加上一个@ResponseBody注解
    @GetMapping("/user")   
    @ResponseBody
    public User login(){
        return new User(1);
    }

}
```
这样返回的结果将是`{userId:1}`
