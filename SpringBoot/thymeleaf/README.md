## thymeleaf学习笔记:maple_leaf:  
#### 一、基本语法   
>  **1.命名空间：**
>  ```html
>  <html lang="zh" xmlns:th="http://www.thymeleaf.org">
> ```  
> **2.url相关（`@{}`）**
> ```html
> <a th:href="@{/Login.html}" >  登录 </a>
> ```
> **3.变量值（`${}`）**
> ```html
> <td th:text="${questionaire.getId()}">1</td> //注意model中传过来的
> ```
> **4.非标签内获取值（`[[ ]]`）**
>```html
> //标签中使用
> <a id="name">[[${name}]]</a>  //这时在IDEA中是没有提示的，所以要注意别写错了  
> ```
> ```javascript
> // js/jquery中使用
> let name = [[${name}]]
> ```
