# Hibernate学习笔记:blue_heart:  
> * [同步简书](https://www.jianshu.com/p/49413ca6ac19)  
  
**一、IDEA下的数据库配置**
> 在**resources**目录下的**application.properties**中填入以下配置  
> ```yml
> spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver #数据库驱动  
> spring.datasource.url=jdbc:mysql://localhost:3306/mysql #数据库url  
> spring.datasource.username=username #用户名  
> spring.datasource.password=password #密码  
> 
> spring.jpa.show-sql=true #表示在jpa执行sql时会在控制台打印sql语句  
> spring.jpa.hibernate.ddl-auto=validate  #validate表示每次启动是会检查entity类中的注解与数据表是否匹配  另一个参数是update，表示每次启动更新数据表
> ```
**二、@Entity相关**   
> ```java
> @Entity //表示这是一个实体类，以对应数据库的表或试图  
> @Table(name = "muser") //映射数据表或视图，如果设定则默认为类名  
> public class User{
> 
>    @Id //表示这是主键  
>    @GeneratedValue(strategy = GenerationType.IDENTITY) //表示主键自增  
>    private int id;
> 
>    //@Column 指定列名，如果不指定则默认就是属性名。
>    //nullable 是否为空； length 长度
>    @Column(name = "username",nullable = false,length = 20)
>    private String username;
> 
>    //注意如果属性名采用驼峰式命名，那么转换成列时会自动变为 first_name
>    private String firstName;
> 
>    @Transient  //加上这个注解表示该属性不对应数据表中的列 注意是com.fasterxml.jackson.annotation.JsonProperty这个类  
>    private int stuNo;
> 
>    //还得有一个空的构造方法
>    public User() {
>   
>    }
>  
>    //别忘了还有get/set方法
> }
> ```  
**三、Repository相关**
> ```java
> public interface UserRepository extends JpaRepository<User,Integer> {
> 
>     @Query
>     User findById(int id);  //这种普通的通过列的查询就加一个@Query注解即可，如果连上了Database会有提示的。  
> 
>     @Modifying  // 修改/删除操作
>     @Transactional  //事务  修改或删除操作必须加上这个注解，不然会报错
>     @Query(nativeQuery = true,value = "update user set username = ? where id = ?") //nativeQuery = true 表示使用指定方言(mysql,oracle等)
>     int updateUsername(String newName,String userId);
> 
>     //使用Collection
>     @Modifying  
>     @Transactional  
>     @Query(value = "update user p set select_num = select_num + 1 where id in (:collection)",nativeQuery = true) //另一种指明参数的方法
>     int addNum(@Param("collection") Collection<Integer> collection); //这样就可以使用 in 了
> }
> ```
**四、视图映射**
> 视图映射其实就是数据表的映射，只不过视图没有主键，所以这里**得指定一个唯一列作为主键**，否则会报错  
>  ```java
> @Entity
> @Table(name = "questionaire_view")
> public class QuestionaireView {
> 
>     @Id
>     @Column(name = "cid")
>     private int chooseId;
>
>     .....
> }
> ```
