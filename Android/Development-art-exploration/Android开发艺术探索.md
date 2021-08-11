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



# 第11章 Android的线程和线程池

线程在Android中是一个很重要的概念，从用途上来说，线程分为**主线程**和子线程，**主线程主要处理和界面相关的事情**，而**子线程**则往往**用于执行耗时操作**。由于Android的特性，如果在主线程中执行耗时操作那么就会导致程序无法及时地响应，因此耗时操作必须放在子线程中去执行。除了Thread本身以外，在Android中可以扮演线程角色的还有很多，比如**AsyncTask**和**IntentService**，同时**HandlerThread**也是一种特殊的线程。尽管AsyncTask、IntentService以及HandlerThread的表现形式都有别于传统的线程，但是它们的本质仍然是传统的线程。对于AsyncTask来说，它的底层用到了线程池，对于IntentService和HandlerThread来说，它们的底层则直接使用了线程。

不同形式的线程虽然都是线程，但是它们仍然具有不同的特性和使用场景。AsyncTask封装了线程池和Handler，它主要是为了方便开发者在子线程中更新UI。HandlerThread是一种具有消息循环的线程，在它的内部可以使用Handler。IntentService是一个服务，系统对其进行了封装使其可以更方便地执行后台任务，IntentService内部采用HandlerThread来执行任务，当任务执行完毕后IntentService会自动退出。从任务执行的角度来看，IntentService的作用很像一个后台线程，但是IntentService是一种服务，它不容易被系统杀死从而可以尽量保证任务的执行，而如果是一个后台线程，由于这个时候进程中没有活动的四大组件，那么这个进程的优先级就会非常低，会很容易被系统杀死，这就是IntentService的优点。

在操作系统中，**线程是操作系统调度的最小单元**，同时线程又是一种受限的系统资源，即线程不可能无限制地产生，并且线程的创建和销毁都会有相应的开销。当系统中存在大量的线程时，系统会通过时间片轮转的方式调度每个线程，因此**线程不可能做到绝对的并行**，除非线程数量小于等于CPU的核心数，一般来说这是不可能的。试想一下，如果在一个进程中频繁地创建和销毁线程，这显然不是高效的做法。正确的做法是采用线程池，一个线程池中会缓存一定数量的线程，通过线程池就可以避免因为频繁创建和销毁线程所带来的系统开销。Android中的线程池来源于Java，主要是通过Executor来派生特定类型的线程池，不同种类的线程池又具有各自的特性，详细内容会在11.3节中进行介绍。

## 主线程和子线程

主线程是指进程所拥有的线程，在Java中默认情况下一个进程只有一个线程，这个线程就是主线程。Android沿用了Java的线程模型，其中的线程也分为主线程和子线程，其中主线程也叫UI线程。主线程的作用是**运行四大组件**以及**处理它们和用户的交互**，而自线程的作用是**执行耗时任务**，比如网络请求，I/O操作等。

从Android 3.0开始系统要求网络访问必须在自线程中进行，否则网络访问会失败并抛出NetworkOnMainThreadException异常，这样是为了避免主线程由于被耗时操作所阻塞从而出现ANR现象。

### Android中的线程形态

#### AsyncTask



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



## Android中的缓存策略

缓存策略主要包含缓存的**添加**、**读取**和**删除**三类操作。策略主要体现在删除上面，当缓存容量满了之后，需要通过策略删除调用之前的缓存。

常见的缓存策略主要有LRU（Least Recently Used）最近最少使用算法和LFU（Least Ferquently Used）最不常使用。

### LruCache

LruCache是一个范型类，它内部采用一个LinkedHashMap以**强引用**的方式缓存对象，当缓存满时，LruCache会**移除较早使用的缓存**对象，然后再添加新的缓存对象。

一些特殊情况下，还需要重写LruCache的entryRemoved方法，LruCache移除旧缓存时会调用**entryRemoved**方法，因此可以在该方法内完成一些资源回收工作（如果需要的话）。

```java
public final V get(K key) //获取
public final V put(K key, V value) //添加
public final V remove(K key) //删除
```



### DiskLruCache

DiskLruCache用于实现存储设备缓存，即**磁盘缓存**。它通过将缓存对象写入文件系统从而实现缓存的效果。

* DiskLruCache的创建

  DiskLruCache并不能通过构造方法来创建，它提供了open方法用于创建自身。

* DiskLruCache的缓存添加

  DiskLruCache的缓存添加的操作是通过**Editor**完成的，Editor表示一个缓存对象的编辑对象。

  > 一般采用url的md5值作为key

* DiskLruCache的缓存查找

  和添加过程类似，缓存查找过程也需要将url转换为key，然后通过get方法得到一个**Snapshot**对象，接着再通过其得到缓存的文件输入流，进而得到Bitmap对象。

  > 为了避免OOM，一般不直接加载原始图片，而是通过**采样率**inSampleSize来得到缩放后的图片Bitmap对象



### ImageLoader的实现

一个优秀的ImageLoader应该具有如下功能：

* 图片的同步加载
* 图片的异步加载
* 图片压缩
* 内存缓存
* 磁盘缓存
* 网络拉取

其中**内存缓存**和**磁盘缓存**时ImageLoader的核心。除此之外，还需要处理一些特殊的情况，例如在ListView或GridView中，View的复用既是它们的优点也是它们的缺点，假设Item A正在加载图片，这时快速滑动屏幕，Item A被移出屏幕然后新的Item复用了A，这时就会出现图片错位的问题。

> 优化卡顿：当上下频繁快速滑动时，会瞬间产生大量一步操作，但是这些操作是无意义的，可以通过setOnScrollListener设置滑动监听，在滑动的时候不加载图片，停止滑动时再执行加载逻辑。

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
