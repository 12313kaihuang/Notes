Demos结构
-----------------

# 第1章 Activity的生命周期和启动模式
## Activity 启动
> 日志TAG：`ActivityTest`
#### Test1
> 注意：BActivity和CActivity的启动模式均为singleTask，且taskAffinity并不是默认的任务栈

1.首先启动B然后在B中启动C，在C中启动本Activity，此时通过`adb shell dumpsys activity activities`
命令查看Activity任务栈，可以发现此时任务栈状态为
```text
* Task{a56377c #38350 visible=true type=standard mode=fullscreen translucent=true A=10509:com.example.task1 U=0 StackId=38350 sz=3}
        mLastOrientationSource=ActivityRecord{f0d6758 u0 com.example.activitytest/.MainActivity t38350}
        bounds=[0,0][1080,2400]
        * ActivityRecord{f0d6758 u0 com.example.activitytest/.MainActivity t38350}
        * ActivityRecord{270ef6a u0 com.example.activitytest/.CActivity t38350}
        * ActivityRecord{6065e6f u0 com.example.activitytest/.BActivity t38350}
* Task{d7acb07 #38349 visible=true type=standard mode=fullscreen translucent=true A=10509:com.example.activitytest U=0 StackId=38349 sz=1}
        mLastOrientationSource=ActivityRecord{8f8cbd4 u0 com.example.activitytest/.MainActivity t38349}
        bounds=[0,0][1080,2400]
        * ActivityRecord{8f8cbd4 u0 com.example.activitytest/.MainActivity t38349}
```
因为CActivity在另一个栈中，所以启动MainActivity时会在本栈中创建实例

2. 再次跳转至BActivity，此时任务栈状态为：
```text
* Task{a56377c #38350 visible=true type=standard mode=fullscreen translucent=true A=10509:com.example.task1 U=0 StackId=38350 sz=1}
        mLastOrientationSource=ActivityRecord{6065e6f u0 com.example.activitytest/.SecondActivity t38350}
        bounds=[0,0][1080,2400]
        * ActivityRecord{6065e6f u0 com.example.activitytest/.SecondActivity t38350}
* Task{d7acb07 #38349 visible=true type=standard mode=fullscreen translucent=true A=10509:com.example.activitytest U=0 StackId=38349 sz=1}
        mLastOrientationSource=ActivityRecord{8f8cbd4 u0 com.example.activitytest/.MainActivity t38349}
        bounds=[0,0][1080,2400]
        * ActivityRecord{8f8cbd4 u0 com.example.activitytest/.MainActivity t38349}
```
`com.example.task1`任务栈中就只剩SecondActivity了，因为它的启动模式是singleTask模式，此时**按两次返回键就会退出App**。

#### Test2
隐式启动Activity时，若注册表中IntentFilter中没有加入category（android.intent.category.DEFAULT），会报错
`No Activity found to handle Intent { act=com.example.activity.test.D }`

#### Test3
同时有显示和隐式时会以隐式启动的方式为主

#### Test4
隐式启动Activity只有NEW_TASK和配合CLEAR_TOP使用才能达到singgleTask的启动效果

# 第2章 IPC机制

## 序列化接口-SerializationTest
> 日志TAG：`ActivityTest`
Serializable & Parcelable
1. serialVersionUID是用来辅助序列化和反序列化的，原则上序列化后的数据中的`serialVersionUID`和当前类的`serialVersionUID`相同才能正常地被反序列化。
2. 若前后serialVersionUID没有改变，然后是类结构有变化，不会报错，但是读取到的状态只能是序列化时的状态，即
   * 若序列化后新增了属性，那么反序列化时新增属性不会被赋值，是默认值。
   * 若序列化后减少了属性，那么反序列化时只能得到当前的属性的值。
3. 静态成员变量属于类不属于对象，所以不会参与序列化过程。
4. 用`transient`关键字标记的成员变量不参与序列化过程。

## 跨进程通信方式
需要注意并发、对象不一致即线程切换/阻塞等问题
### Messenger Test
> 日志TAG：`MessengerTest`
### AIDL Test
> 日志TAG：`AIDL Test`
### ContentProvier Test
> 日志TAG：`ContentProviderTest`
## Socket Test
> 日志TAG：`SocketTest`
通过`Socket`和`ServerSocket`建立C/S连接，这个过程需要网络请求，所以需要注意不要在主线程中进行网络请求