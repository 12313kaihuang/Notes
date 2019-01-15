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
* **将手机号的中间四位变成`*`号** 
```java

public class PhoneNumber {

	private static final String FORMAT_PHONE_REGEX = "(\\d{3})\\d{4}(\\d{4})";
	public static String dimPhoneNumber(String phoneNumber){
            return phoneNumber.replaceAll(FORMAT_PHONE_REGEX,"$1****$2");
        }

	public static void main(String[] args) {
	    String phoneNumber = "12345678900";
	    System.out.println(PhoneNumber.dimPhoneNumber(phoneNumber));
	}
}
```  
&emsp;输出结果：`123****8900 `。
