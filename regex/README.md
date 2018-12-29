# regex 正则相关  

* **提取字符串中的数字**  
```java
	String key = ["12","34","56"];
	String[] split = key.split(",");
	String regEx = "[^0-9]";  //匹配非数字
	Pattern p = Pattern.compile(regEx);
	for (String s : split) {
		Matcher m = p.matcher(s);   //matcher可以理解为是一个List<String>,里面是所匹配的结果集
		int i = Integer.parseInt(m.replaceAll("").trim());
		System.out.printf("%d ",i);
	}
```
&emsp;输出结果：`12 34 56 `。
