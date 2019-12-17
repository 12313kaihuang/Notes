# SearchView--自定义搜索框

### 效果图：  
![](imgs/TIM图片20190131162357.png)      
![](imgs/TIM图片20190131162509.png)      

### 使用：
将[res](https://github.com/12313kaihuang/Notes/tree/master/Android/widget/SearchView/res)文件夹内的文件放入相应资源文件夹内即可使用，如有不懂的请参照[Demo](https://github.com/12313kaihuang/Notes/tree/master/Android/widget/SearchView/Demo)。    
`setImageButtonVoiceClickListener`方法设置语音按钮点击事件  
`setonTextViewSearchClickListener`设置点击搜索时的点击事件  
`setImageButtonCancelClickListener`设置清除按钮点击后的事件  

### 小技巧：  
如果不想让editText自动获取焦点，可以在布局文件中的父控件上加上`android:focusable="true"`和`android:focusableInTouchMode="true"`属性。[Demo](https://github.com/12313kaihuang/Notes/tree/master/Android/widget/SearchView/Demo)中也使用了此方法：    
```xml

<!--activity_main.xml-->

<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:focusable="true"
    android:focusableInTouchMode="true"
    tools:context="android.searchview.MainActivity">

    <android.searchview.SearchView
        android:id="@+id/searchView"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:layout_marginEnd="8dp"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</android.support.constraint.ConstraintLayout>

```

 
