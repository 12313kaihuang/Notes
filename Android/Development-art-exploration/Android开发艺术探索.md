《Android开发艺术探索》读书笔记
-----------------------------

[Demos项目](./Tests)


# 第1章 Activity的声明周期和启动模式
## 1.1 Activity生命周期
* 典型情况下生命周期方法
* 异常情况下生命周期方法
## 1.2 Activity启动模式
* lunchMode
> 通过taskAffinity可以设置Activity所处任务栈
* 隐式启动Flags
> 同时有以显式方式启动 
## 1.3 IntentFilter匹配规则
* Action、Category、data匹配规则
* Intent匹配查询
> packageManager.queryIntentActivities(Intent,Int);intet.resolveActivity(PackageManager)



# 第2章 IPC机制

## 2.1 Android IPC简介

* Android进程间通信方式
  * 机制： Binder，Socket
  * 具体实现方式：Bundle、文件共享、AIDL、Messenger、ContentProvider和Scoket；
  
* Windows
  
  > 剪切板、管道、邮槽
  
* Linux

  > 命名管道、共享内存、信号量 



## 2.2 Android中的多进程模式
* 开启多进程方法：`android:process`
> ":"开头表示私有进程、不以":"开头属于全局进程
* 多进程模式的运行机制
> 多进程会产生的问题
> * 静态成员和单例模式完全失效
> * 线程同步机制完全失效
> * SharedPreferences的可靠性下降
> * Application会多次创建



 ## 2.3 IPC基础概念介绍

* Serializable接口

  Serializable是一个**空接口**，作为一种标记；真正实现序列化的是**ObjectOutputStream**和**ObjectInputStream**。

  1. `serialVersionUID`是用来辅助序列化和反序列化的，原则上序列化后的数据中的`serialVersionUID`和当前类的`serialVersionUID`相同才能正常地被反序列化。
  2. 若前后serialVersionUID没有改变，然后是类结构有变化，不会报错，但是读取到的状态只能是序列化时的状态，即
     * 若序列化后新增了属性，那么反序列化时新增属性不会被赋值，是默认值。
     * 若序列化后减少了属性，那么反序列化时只能得到当前的属性的值。
  3. 静态成员变量属于类不属于对象，所以**不会参与**序列化过程。
  4. 用`transient`**关键字**标记的成员变量不参与序列化过程。

* Parcelable接口

  Parcel内部包装了可序列化的数据，可以在Binder中自由传输。

  |                 方法                 |                             功能                             |            标记值             |
  | :----------------------------------: | :----------------------------------------------------------: | :---------------------------: |
  |     createFromParcel(Parcel in)      |                从序列化后的对象中创建原始对象                |                               |
  |          newArray(int size)          |                  创建指定长度的原始对象数组                  |                               |
  |      PacelableObject(Parcel in)      |                从序列化后的对象中创建原始对象                |                               |
  | writeToParcel(Parcel out, int flags) | 将当前对象写入序列化结构中，其中flags标识有两种值：0或1（参见右侧标记位）。为1时标识当前对象需要作为返回值返回，不能立即释放资源，几乎所有情况都为0 | PARCELABLE_WRITE_RETURN_VALUE |
  |           describeContents           | 返回当前对象的内容描述。如果含有文件描述符，返回1（参见右侧标记位），否则返回0，几乎所有情况都返回0 |   CONTENTS_FILE_DESCRIPTOR    |

  > 两种序列化方式对比：
  >
  > * Serializable是**Java**中的序列化接口，其**使用起来简单**但是**开销很大**，序列化和反序列化过程需要**大量I/O操作**。
  > * Parcelable是Android中的序列化方式，更适合在Android平台上，缺点就是使用起来稍微**麻烦**点，但**效率很高**。
  > * Serializable适合将对象**序列化到存储设备**中或者将对象序列化后通过**网络传输**。
  > * Parcelable主要用在**内存序列化**上。

* Binder

  Binder是Android中的一个类，它实现了IBinder接口。从IPC角度来说，Binder是Android中的一种跨进程通信方式；从Android应用层来说，Binder是客户端和服务端进行通信的媒介。

  AIDL文件并不是实现Binder的必须品，其本质是系统为我们提供了一种快速实现Binder的工具。

  > * 可以通过linkToDeath(DeathRecipient)和unlinkToDeath方法设置死亡代理。
  > * Binder#isBinderAlive可以用来判断Binder是否死亡。



## 2.4 Android中的IPC方式

* 使用Bundle

* 文件共享

  > 需要解决或避免并发读/写。

* 使用Messenger

  Messenger对AIDL做了封装，使得我们可以更简单地进行进程间通信。同时，它一次处理一个请求（通过**Handler**），因此在服务/客户端不用考虑线程同步的问题。

  > 服务端可以通过msg.replyTo对应的Messenger对象来向客户端传递数据。

* 使用AIDL

  * AIDL支持的数据类型：
    * 基本数据类型（int、long、char、boolean、double等）；
    * String和CharSequence；
    * List：只支持ArrayList，里面每个元素都必须能够被AIDL支持；
    * Map：只支持HashMap，里面每个元素都必须被AIDL支持，包括key和value；
    * Parcelable：所有实现了Parcelable接口的对象
    * AIDL：所有AIDL接口本身也可以在AIDL文件中使用
  * 除了基本数据类型，其他类型的参数必须标上方向：in、out或者inout 
  * AIDL接口只支持方法，不支持声

* 使用ContentProvider

  * 需要注意CRUD操作，防止SQL注入和权限控制
  * 重写6个方法：onCreate、getType和增删改查四个方法
  * 除了onCreate方式由系统回调并运行在主线程里，其他五个方法均由外界回调并运行在Binder线程池中，要做好线程同步
  * android:authorities是ContentProvider的唯一标识
  * ContentResolver.registerContentObserver来监听数据变化

* 使用Socket

  分为流式套接字（TCP）和用户数据报套接字（TCP）



## 2.5 Binder连接池

随着AIDL数量的增加，我们**不能无限制地增加Service**，Service是四大组件之一，本身就是一种系统资源。而且太多的Service会使得我们的应用看起来很重量级；**使用BinderPool**能够极大的提高AIDL的开发效率，并且可以避免大量的Service创建。

```java
interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
```



## 2.6 选用合适的IPC方式

|      名称       |                             优点                             |                             缺点                             |                           适用场景                           |
| :-------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
|     Bundle      |                           简单易用                           |                 只能传输Bundle支持的数据类型                 |                    四大组件间的进程间通信                    |
|    文件共享     |                           简单易用                           |        不适合高并发场景，并且无法做到进程间的即时通信        |        无并发访问情形，交换简单的数据实时性不高的场景        |
|      AIDL       |          功能强大，支持一对多并发通信，支持实时通信          |                使用稍复杂，需要处理好线程同步                |            一对多通信且有RPC需求（远程方法调用）             |
|    Messenger    |          功能一般，支持一对多并发通信，支持实时通信          | 不能很好处理高并发情形，不支持RPC，数据通过Message进行传输，因此只能传输Bundle支持的数据类型 | 低并发的一对多即时通信，无RPC需求，或者无须要返回结果的RPC请求 |
| ContentProvider | 在数据源访问方面功能强大，支持一对多并发数据共享，可通过Call方法扩展其他操作 |       可以理解为受约束的AIDL，主要提供数据源的CRUD操作       |                   一对多的进程间的数据共享                   |
|     Socket      |   功能强大，可以通过网络传输字节流，支持一对多并发实时通信   |            实现细节稍微有点繁琐，不支持直接的RPC             |                         网络数据交换                         |



# 第3章

##  3.1 View基础知识

* 坐标系
* MotionEvent & TouchSlop
* 辅助类
	* VelocityTracker 速度追踪
	* GestureDetector 手势检测
	* Scroller 弹性滑动对象


## 3.2 View的滑动
滑动分为3类实现方式：
1. 通过View本身提供的scrollTo/scrollBy方法
	滑动的是View的内容，而不是View本身；操作简单，适合对View内容的滑动
2. 通过动画给View施加平移效果
	View动画会导致新位置不会响应点击等事件；操作简单，主要适用于没有交互的View和实现复杂的动画效果
3. 通过改变View的LayoutParams使得View重新布局从而实现滑动 
	操作稍微复杂，适用于有交互的View

## 3.3 弹性滑动
* 使用Scroller
* 通过动画
* 使用延时策略（handler，postDelay）

## 3.4 事件分发机制
## 3.5 View的滑动冲突



# 第12章 Bitmap的加载和Cache

## Bitmap的高效加载

Bitmap在Android中指的是一张图片，通过**BitmapFactory**来加载Bitmap，BitmapFactory提供了四类方法：

* decodeFile ---- 从文件系统加载bitmap对象
* decodeResource ---- 从资源加载bitmap对象
* decodeStream ---- 从输入流加载bitmap对象
* decodeByteArray ---- 从字节数组加载bitmap对象

其中decodeFile和decodeResource又间接调用了decodeStream方法，这四类方法最终是在Android底层实现的，对应着BitmapFactory类的几个native方法。

**高效加载**Bitmap的**核心思想**就是采用`BitmapFactory.Options`来加载所需尺寸的图片，通过**inSampleSize**参数来缩放图片。具体的操作流程如下：

1. 将BitmapFactory.Options的**inJustDecodeBounds**参数设为true并加载图片

   > 将inJustDecodeBounds参数设为true后，BitmapFactory只会解析图片原始宽高等信息，并不会真正地去加载图片

2. 从BitmapFactory.Options中取出图片的原始宽高信息，它们对应与`outWidth`和`outHeight`参数

3. 根据采样率的规则并结合目标View所需的大小计算出采样率`inSampleSize`

   > 通过Options.inSampleSize设置上采样率

4. 将BitmapFactory.Options的`inJustDecodeBounds`参数设为false，然后重新加载图片

具体代码逻辑可以在Demo项目中查看。

# 第15章 Android性能优化



## Android的性能优化方法

### 布局优化

尽量减少布局文件的层级

* 删除布局中无用的控件和层级，其次有选择地使用性能较低的ViewGroup（LinearLayout、FrameLayout）

* 使用标签

  * `include`标签：布局重用

  * `merge`标签：减少View层级，一般配合include使用

  * `viewStub`标签：按需加载所需的布局文件

    通过`setVisibility`或者`inflate`方法加载后，ViewStub就会被它内部的布局替换掉



### 绘制优化

主要是指View的**onDraw**方法要避免执行大量的操作

* onDraw中**不要创建新的局部对象**

  因为onDraw方法可能会被频繁调用，这样就会在一瞬间产生大量的临时对象，这不仅占用了过多的内存而且还会导致系统更加频繁gc，降低了程序的执行效率

* onDraw中**不要做耗时任务**

  也不能执行成千上万次的循环操作，尽管每次循环都很轻量级，但大量的循环仍然十分抢占CPU的时间片，这回造成View的绘制过程不流畅



### 内存泄露优化

内存泄露的优化分两个方面：一方面是在开发过程中**避免**写有内存泄露的代码；另一方面是通过一些**分析工具**例如MAT来找出潜在的内存泄露继而解决

**常见的内存泄露例子**

* 静态变量导致的内存泄露
* 单例模式导致的内存泄露
* 属性动画导致的内存泄露



### 响应速度优化和ANR日志分析

通过`traces.txt`来分析ANR



### ListView和Bitmap优化

ListView的优化在12章已经做了介绍，详情可以跳转12章回顾

* 要采用ViewHolder并避免在getView中执行耗时操作
* 要根据列表滑动状态来控制任务的执行频率，比如当列表快速滑动时不太适合开启大量的异步任务
* 可以尝试开启硬件加速来使ListView更加流畅



Bitmap的优化同样在12章有做介绍，主要是通过`BitmapFactory.Options`来根据需要对图片进行**重采样**



### 线程优化

线程优化的思想是采用**线程池**，避免程序中存在大量的Thread。线程池可以重用内部的线程，从而**避免了线程的创建和销毁所带来的性能开销**，同时线程池还能**有效地控制线程池的最大并发数**，避免大量的线程因互相抢占资源从而导致阻塞现象的发生。关于线程池的详细介绍在第11章。



### 一些性能优化建议

* 避免创建过多的对象
* 不要过多使用枚举，枚举占用的空间要比整形大
* 常量 请使用`static final`来修饰
* 使用一些Android特有的数据结构，比如SparseArray和Pair等，它们都具有更好的性能
* 适当使用软引用和弱引用
* 采用内存缓存和磁盘缓存
* 尽量采用静态内部类，这样可以避免潜在的由于内部类而导致的内存泄露



## 提高程序的可维护性

**可读性**是代码可维护性的前提，下面是笔者的一些看法：

* 命名要规范

  要能正确地传达出变量或方法的含义，少用缩写，关于变量的前缀可以参考Android源码的命名方式，比如私有成员以m开头，静态成员以s开头，常量则全部用大写字母表示等

* 代码排版上需要留出合理的空白来区分不同的代码块

* 仅为非常关键的代码添加注释



代码的**层次性**是指代码要有分层的概念，对于一段业务逻辑，不要试图在一个方法或者一个类中全部实现，而要将它分成几个子逻辑，然后每个子逻辑做自己的事情，这样既显得代码层次分明，又可以分解任务从而实现简化逻辑的效果。



另外还需要注意**可扩展性**，很多时候我们在开发过程中我们无法保证已经做好的需求不在后面的版本发生变更，因此在写程序的过程中要时刻考虑到扩展，考虑着如果这个逻辑后面发生了改变那么需要做哪些修改，以及怎么样才能降低修改的工作量，面向扩展变成会使程序具有更好的扩展性。



恰当地使用设计模式可以提高代码的可维护性和可扩展性，但是Android程序容易有性能瓶颈，因此要控制设计的度，设计不能太牵强，否则就是过度设计了。
