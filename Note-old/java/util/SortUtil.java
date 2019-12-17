package com.yu.hu.util;

/**
 * Created by HY
 * 2019/3/21 18:35
 * <p>
 * 常见的8种排序方法 升序排序 即从小到大排序
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class SortUtil {

    /**
     * 直接插入排序
     * <p>
     * 基本思想：
     * 在要排序的一组数中，假设前面(n-1) [n>=2] 个数已经是排好顺序的，
     * 现在要把第n个数插到前面的有序数中，使得这n个数也是排好顺序的。
     * 如此反复循环，直到全部排好顺序。
     * <p>
     * 需要一个辅助空间保存当前所要排序的那个数
     * 如果前一个数(i-1)大于该数，那么这个数将会往后移动，即该位置(i)将会被替换掉。
     *
     * @param arr arr
     */
    public static void straightInsertionSort(int[] arr) {
        int length = arr.length;  //单独把数组长度拿出来 提高效率
        int insertNum; //要插入的数
        for (int i = 1; i < length; i++) {  //第一个数不用排序，所以下标从1开始
            insertNum = arr[i]; //取出要排序的这个数（后面移动时会丢失这个数）
            int j = i - 1; //序列元素个数（已排好序的元素个数）
            while (j >= 0 && arr[j] > insertNum) { //从后往前循环，将大于insertNum的数向后移动
                arr[j + 1] = arr[j];  //元素向后移动
                j--;
            }
            arr[j + 1] = insertNum; //找到位置，插入当前元素。
        }
    }


    /**
     * 希尔排序 又称递减增量排序
     * 插入排序的一种更高效的改进版本
     * 非稳定排序算法
     * <p>
     * 将数的个数设为n，取奇数k=n/2，将下标差值为k的数分为一组，构成有序序列。
     * 再取k=k/2 ，将下标差值为k的数分为一组，构成有序序列。
     * 重复第二步，直到k=1执行简单插入排序。
     *
     * @param arr arr
     */
    public static void shellSort(int[] arr) {
        int length = arr.length;
        while (length != 0) {
            length = length / 2;
            for (int i = 0; i < length; i++) { //分组
                for (int j = i + length; j < arr.length; j += length) {//元素从第二个开始
                    int k = j - length;  //k为有序序列最后一位的位数
                    int temp = arr[j];  //要插入的元素
                    while (k >= 0 && arr[k] > temp) {  //从后往前遍历
                        arr[k + length] = arr[k];
                        k -= length;  //向后移动len位
                    }
                    arr[k + length] = temp;
                }
            }
        }
    }


    /**
     * 简单（直接）选择排序
     * 遍历整个序列，将最小的数放在最前面。
     * 遍历剩下的序列，将最小的数放在最前面。
     * 重复第二步，直到只剩下一个数。
     * <p>
     * 首先确定循环次数，并且记住当前数字和当前位置。
     * 将当前位置后面所有的数与当前数字进行对比，小数赋值给key，并记住小数的位置。
     * 比对完成后，将最小的值与第一个数的值交换。
     * 重复2、3步。
     *
     * @param arr arr
     */
    public static void simpleSelectSort(int[] arr) {
        int length = arr.length;
        for (int i = 0; i < length; i++) { //循环次数
            int value = arr[i]; //用于存储最小值
            int position = i; //用于存储最小值下标
            for (int j = i + 1; j < length; j++) { //找到最小值的位置
                if (arr[j] < value) {
                    value = arr[j];
                    position = j;
                }
            }
            arr[position] = arr[i]; //交换
            arr[i] = value;
        }
    }


    /**
     * 堆排序
     * 对简单选择排序的优化
     * <p>
     * 将序列构建成大顶堆。
     * 将根节点与最后一个节点交换，然后断开最后一个节点。
     * 重复第一、二步，直到所有节点断开。
     *
     * @param arr arr
     */
    public static void heapSort(int[] arr) {
        int length = arr.length;
        //循环建堆
        for (int i = 0; i < length - 1; i++) {
            //建堆
            buildMaxHeap(arr, length - 1 - i);
            //交换顶堆和最后一个元素
            swap(arr, 0, length - 1 - i);
        }
    }

    /**
     * 冒泡排序
     * <p>
     * 设置循环次数。
     * 设置开始比较的位数，和结束的位数。
     * 两两比较，将最小的放到前面去。
     * 重复2、3步，直到循环次数完毕。
     *
     * @param arr arr
     */
    public static void bubbleSort(int[] arr) {
        int length = arr.length;
        for (int i = 0; i < length; i++) {  //设置循环次数
            for (int j = 0; j < length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    //交换位置
                    arr[j] += arr[j + 1];
                    arr[j + 1] = arr[j] - arr[j + 1];
                    arr[j] -= arr[j + 1];
                }
            }
        }
    }

    /**
     * 快速排序
     * <p>
     * 选择第一个数为p，小于p的数放在左边，大于p的数放在右边。
     * 递归的将p左边和右边的数都按照第一步进行，直到不能递归。
     *
     * @param arr   arr
     * @param start start
     * @param end   end 最后一个值的下标（首次调用应为arr.length - 1）
     */
    public static void quickSort(int[] arr, int start, int end) {
        if (start < end) {
            int baseNum = arr[start]; //选基准值
            int midNum; //记录中间值
            int i = start;
            int j = end;
            do {
                //从前往后找到大于baseNum的第一个数的索引（下标）
                while (arr[i] < baseNum && i < end) {
                    i++;
                }
                //从后往前找到小于baseNum的第一个数的索引（下标）
                while (arr[j] > baseNum && j > start) {
                    j--;
                }
                if (i <= j) {
                    midNum = arr[i];
                    arr[i] = arr[j];
                    arr[j] = midNum;
                    i++;
                    j--;
                }
            } while (i <= j);
            //到这里时j < i了
            if (start < j) {
                quickSort(arr, start, j);
            }
            if (i < end) {
                quickSort(arr, i, end);
            }
        }
    }


    /**
     * 归并排序
     * <p>
     * 速度仅次于快速排序，内存少的时候使用，可以进行并行计算的时候使用。
     * <p>
     * 选择相邻两个数组成一个有序序列。
     * 选择相邻的两个有序序列组成一个有序序列。
     * 重复第二步，直到全部组成一个有序序列。
     *
     * @param arr   arr
     * @param start start
     * @param end   end 最后一个值的下标（首次调用应为arr.length - 1）
     */
    public static void mergeSort(int[] arr, int start, int end) {
        int mid = (start + end) / 2;
        if (start < end) {
            mergeSort(arr, start, mid);
            mergeSort(arr, mid + 1, end);
            //左右归并
            merge(arr, start, mid, end);
        }
    }

    /**
     * 基数排序
     * <p>
     * 用于大量数，很长的数进行排序时。
     * <p>
     * 将所有的数的个位数取出，按照个位数进行排序，构成一个序列。
     * 将新构成的所有的数的十位数取出，按照十位数进行排序，构成一个序列。
     *
     * @param arr arr
     */
    public static void radixSort(int[] arr) {
        int max = findMax(arr, 0, arr.length - 1);

        //需要遍历的次数由数组最大值的位数来决定
        for (int i = 1; max / i > 0; i = i * 10) {
            int[][] buckets = new int[arr.length][10];
            //获取每一位数字（个、十、百、千位...分配到桶子里）
            for (int j = 0; j < arr.length; j++) {
                int num = (arr[j] / i) % 10;
                //放入桶子里
                buckets[j][num] = arr[j];
            }
            //回收桶子里的元素
            int k = 0;

            //有10个桶子
            for (int j = 0; j < 10; j++) {
                //对每个桶子里的元素进行回收
                for (int l = 0; l < arr.length; l++) {
                    //如果桶子里面有元素就回收（数据初始化会为0）
                    if (buckets[l][j] != 0) {
                        arr[k++] = buckets[l][j];
                    }
                }
            }
        }


    }


    //找到数组中最大值
    private static int findMax(int[] arrays, int start, int end) {

        //如果该数组只有一个数，那么最大的就是该数组第一个值了
        if (start == end) {
            return arrays[start];
        } else {

            int a = arrays[start];
            int b = findMax(arrays, start + 1, end);//找出整体的最大值

            if (a > b) {
                return a;
            } else {
                return b;
            }
        }
    }

    //进行归并操作
    private static void merge(int[] arr, int start, int mid, int end) {
        //开辟新空间用于存储
        int[] temp = new int[end - start + 1];
        int i = start;
        int j = mid + 1;
        int k = 0;
        //把较小的数先移到新数组中
        while (i <= mid && j <= end) {
            if (arr[i] < arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }
        //把左边剩余的数移入数组
        while (i <= mid) {
            temp[k++] = arr[i++];
        }
        //把右边剩余的数移入数组
        while (j <= end) {
            temp[k++] = arr[j++];
        }
        //把新数组中的数覆盖到arr数组
        if (temp.length >= 0) System.arraycopy(temp, 0, arr, start, temp.length);
    }


    //交换元素
    private static void swap(int[] arr, int i, int j) {
        arr[i] = arr[i] + arr[j];
        arr[j] = arr[i] - arr[j];
        arr[i] -= arr[j];
    }

    /**
     * 对arr数组从0到lastIndex建大顶堆
     * <p>
     * 首先获取lastIndex的父节点(k)并从其开始循环
     * 判断k是否有子节点 -> 取子节点较大值下标 -> 交换父子节点 -> 循环
     *
     * @param arr       arr
     * @param lastIndex 最后一个节点
     */
    private static void buildMaxHeap(int[] arr, int lastIndex) {
        //从lastIndex处节点（最后一个节点）的父节点开始
        //认为存储方式是按层次存储的，先左后右
        for (int i = (lastIndex - 1) / 2; i >= 0; i--) {
            //k保存正在判断的节点
            int k = i;
            //如果当前k节点的子节点存在
            while (k * 2 + 1 <= lastIndex) {
                //k节点的左子节点索引
                int biggerIndex = 2 * k + 1;
                //如果biggerIndex小于lastIndex，即biggerIndex+1代表的k节点的右子节点存在
                if (biggerIndex < lastIndex) {
                    //如果右子节点的值较大
                    if (arr[biggerIndex] < arr[biggerIndex + 1]) {
                        //biggerIndex总是记录较大子节点的索引
                        biggerIndex++;
                    }
                }

                //如果k节点的值小于其较大的子节点的值
                if (arr[k] < arr[biggerIndex]) {
                    //交换他们
                    swap(arr, k, biggerIndex);
                    //将biggerIndex赋予k，开始while循环的下一次循环，重新保证k节点的值大于其左右子节点的值
                    k = biggerIndex;
                } else {
                    break;
                }
            }
        }
    }
}
