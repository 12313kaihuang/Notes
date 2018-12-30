## thymeleaf学习笔记:maple_leaf:  
#### 一、引入  
> * [官方文档](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)  
> 首先在**pom.xml**文件的**dependencies**中加入依赖:  
> ```xml
>         <!--默认的thymeleaf版本太低了，在properties中可设置版本-->
>        <dependency>
>            <groupId>org.springframework.boot</groupId>
>            <artifactId>spring-boot-starter-thymeleaf</artifactId>
>         </dependency>
> ```
> &emsp;由于默认的thymeleaf版本过低，所以最好配置上一个较新版本的，方法有两种，一种是在**properties**标签中加入版本号：
> ```xml
> <thymeleaf.version>3.0.11.RELEASE</thymeleaf.version>
>         <!-- 布局功能的支持程序 thymeleaf3主程序 layout2以上版本-->
> <thymeleaf-layout-dialect.version>2.2.2</thymeleaf-layout-dialect.version>
> ```
> &emsp;第二种方式可以直接在dependency中加上版本号(**注意这里的写法与上面的写法不一致了**)：  
> ```xml
> <dependency>
>   <groupId>org.thymeleaf</groupId>
>   <artifactId>thymeleaf</artifactId>
>   <version>3.0.0.RELEASE</version>
> </dependency>
> ```
> [查看最新版本号](https://github.com/thymeleaf/thymeleaf/releases)
#### 二、基本语法   
>  **1. 命名空间：**
>  ```html
>  <html lang="zh" xmlns:th="http://www.thymeleaf.org">
> ```  
> **2. url相关（`@{}`）**
> ```html
> <a th:href="@{/Login.html}" >  登录 </a>
> ```
> **3. 变量值（`${}`）**
> ```html
> <td th:text="${questionaire.getId()}">1</td> //注意model中传过来的
> ```
> **4. 非标签内获取值（`[[ ]]`）**
>```html
> //标签中使用
> <a id="name">[[${name}]]</a>  //这时在IDEA中是没有提示的，所以要注意别写错了  
> ```
> ```javascript
> // js/jquery中使用
> let name = [[${name}]]
> ```  
> **5. 错误页面(404等)获取出错信息**  
> ```html
>  <!-- [[${status}]] 错误码 -->
>  <h2 class="txt-wthree" >error [[${status}]]</h2>  
>  <!-- [[${message}]]  错误信息 -->
>  <p th:class="detail">[[${message}]]  </p>
> ```
> **6. 公共页面提取**  
> ```html
> <!--1、抽取公共片段-->
> <div th:fragment="copy">
>   &copy; 2011 The Good Thymes Virtual Grocery
> </div>
> <!--2、引入公共片段-->  
> ~{templatename::selector}：模板名::选择器
> <div th:insert="~{footer :: copy}"></div>
> 
> ```
> &emsp;第二种方法
> ```html
> <!--公共片段  header-->
> <div class="header clear" th:fragment="header">
> 
> ~{templatename::fragmentname}:模板名::片段名
> <div th:replace="commons/header::header"></div>
> 
> 3、默认效果：
> insert的公共片段在div标签中
> 如果使用th:insert等属性进行引入，可以不用写~{}：
> 行内写法可以加上：[[~{}]];[(~{})]；
> ```
> &emsp;三种引入公共片段的th属性：  
> &emsp;&emsp;**th:insert**：将公共片段整个插入到声明引入的元素中  
> &emsp;&emsp;**th:replace**：将声明引入的元素替换为公共片段  
> &emsp;&emsp;**th:include**：将被引入的片段的内容包含进这个标签中    
> ```html
> <footer th:fragment="copy">
>    &copy; 2011 The Good Thymes Virtual Grocery
> </footer>
> 
> 引入方式  
> <div th:insert="footer :: copy"></div>  
> <div th:replace="footer :: copy"></div>  
> <div th:include="footer :: copy"></div>  
> 
> 效果
> <div>
>     <footer>
>     &copy; 2011 The Good Thymes Virtual Grocery
>    </footer>
> </div>
> 
> <footer>
> &copy; 2011 The Good Thymes Virtual Grocery
> </footer>
> 
> <div>
> &copy; 2011 The Good Thymes Virtual Grocery
> </div>
> ```
> &emsp;引入片段的时候传入参数：
> ```html
> <nav class="col-md-2 d-none d-md-block bg-light sidebar" id="sidebar">
>     <div class="sidebar-sticky">
>         <ul class="nav flex-column">
>            <li class="nav-item">
>                <a class="nav-link active"
>                   th:class="${activeUri=='main.html'?'nav-link active':'nav-link'}"href="#" th:href="@{/main.html}">
>                   <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-home">
>                         <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z">> </path>
>                         <polyline points="9 22 9 12 15 12 15 22"></polyline>
>                     </svg>
>                     Dashboard <span class="sr-only">(current)</span>
>                 </a>
>             </li>
> </nav>
> 
> <!--引入侧边栏;传入参数-->
> <div th:replace="commons/bar::#sidebar(activeUri='emps')"></div>
> ```
