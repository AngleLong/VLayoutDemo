[不怕跌倒，所以飞翔](http://www.jianshu.com/u/4a99c9554afc)


参考文献:
[Carson_Ho 的Android开源库V - Layout：淘宝、天猫都在用的UI框架，赶紧用起来吧！](http://www.jianshu.com/p/6b658c8802d1?from=singlemessage)
[我就是马云飞的android VLayout 全面解析   ](http://blog.csdn.net/sw950729/article/details/67634747)

[V- Layout](https://github.com/alibaba/vlayout) 是阿里出品的基础 UI 框架，用于快速实现页面的复杂布局。这个控件一般用于实现种类较多的页面展示。

一直以来都想看看这个开源项目怎么用，但是一直也没有太多时间去看，今天特意花了点时间去看了一下这个开源项目，没有深入的研究源码，只是简单的使用，把使用心得记录下来，以便学习它的人能快速的上手使用；

##1.使用
首先先介绍几种布局形式
```
   - LinearLayoutHelper: 线性布局 
   - GridLayoutHelper: Grid布局， 支持横向的colspan 
   - FixLayoutHelper: 固定布局，始终在屏幕固定位置显示 
   - ScrollFixLayoutHelper: 固定布局，但之后当页面滑动到该图片区域才显示, 可以用来做返回顶部或其他书签等 
   - FloatLayoutHelper: 浮动布局，可以固定显示在屏幕上，但用户可以拖拽其位置 
   - ColumnLayoutHelper: 栏格布局，和布局在一排，可以配置不同列之间的宽度比值 
   - SingleLayoutHelper: 通栏布局，只会显示一个组件View 
   - OnePlusNLayoutHelper: 一拖N布局，可以配置1-5个子元素 
   - StickyLayoutHelper: stikcy布局， 可以配置吸顶或者吸底 
   - StaggeredGridLayoutHelper: 瀑布流布局，可配置间隔高度/宽度
```
以上这几种布局格式包含了多种布局的格式。

####1.引入类库
这个没有什么好说的直接按照gitHub上的方法直接引入就可以了
```
 compile('com.alibaba.android:vlayout:1.0.7@aar') {
        transitive = true
    }
```
####2.创建VirtualLayoutManager对象

```
VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
rv.setLayoutManager(layoutManager);
```
这个其实就是相当于RecycleView中的LayoutManager只不过是重写了罢了。

####3.设置复用池大小
```
// 设置组件复用回收池
RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool(); 
recyclerView.setRecycledViewPool(viewPool); 
viewPool.setMaxRecycledViews(0, 10);
```

####4.创建管理Adapter的适配器
```
DelegateAdapter delegateAdapter = new DelegateAdapter(manager, true);
```
这个相当于适配器的类，但是他可以管理更多适配器去进行布局，所以相当于适配器的管理类，用于之后添加所有的适配器的集合

####5.创建一个集合管理其中的适配器
```
List<DelegateAdapter.Adapter> adapters = new ArrayList<>();
```
这个集合是用来存放你写的适配器的。

####6.实现适配器
其实实现适配器的话和以前的实现没有什么区别就是多覆写了一个类，继承关系也发生了改变而已。
```
public class BaseItemsAdapter extends DelegateAdapter.Adapter<ItemHolder> {

    private Context mContext;
    private LayoutHelper mHelper;
    private String mColor;/*这个颜色值主要是为了区分样式的*/
    private int mCount;

    public BaseItemsAdapter(Context context, LayoutHelper helper, String color, int count) {
        mContext = context;
        mHelper = helper;
        this.mColor = color;
        this.mCount = count;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mHelper;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.items_linear, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.itemView.setBackgroundColor(Color.parseColor(mColor));
        holder.mTvContent.setText("这是基类的布局" + position);
    }

    @Override
    public int getItemCount() {
        return mCount;
    }
}
```
#####注意几点问题：
1.这里你主要看一下继承的不在是RecycleView的Adapter了而是**DelegateAdapter**的Adapter了；
2.重新覆写了一个**onCreateLayoutHelper（）**这个方法，这个方法返回的是一个LayoutHelper；
3.关于这个LayoutHelper是上面提到的几种类型，这里可以从上个页面通过构造方法传过来。

####6.最后把相应的适配器进行添加就好了
```
/*添加了所有的适配器之后添加到*/
delegateAdapter.addAdapters(adapters);

 /*设置recycleView*/
mRecycleView.setAdapter(delegateAdapter);
```
以上方法就能实现简单的组合布局了；还有一些细节的问题我会在下面进行讲解。。。


##2.其他特点添加：
####1.margin和padding
这里面的Margin和Padding指的是LayoutHelper的Margin和Padding而不是整个RecycleView的Margin和Padding
设置方法如下：
```
setPadding(int leftPadding, int topPadding, int rightPadding, int bottomPadding)
setMargin(int leftMargin, int topMargin, int rightMargin, int bottomMargin)
```
![引用网络上的一张图片](http://upload-images.jianshu.io/upload_images/2546238-5b16cc2a647a2ce6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

####2.设置背景颜色和图片
bgColor, bgImg这两个属性都是针对于非fix类型的LayoutHelper
设置方法如下：
```
setBgColor(int bgColor)；
```
设置背景的方法我觉得没有用所以没有去研究怎么使用。

####3.设置宽高比属性aspectRatio
这个宽高比要从两个方面来说
- LayoutHelper定义的aspectRatio，指的是一行视图整体的宽度与高度之比，当然整体的宽度是减去了RecyclerView和对应的LayoutHelper的margin, padding。
- 视图的LayoutParams定义的aspectRatio，指的是在LayoutHelper计算出视图宽度之后，用来确定视图高度时使用的，它会覆盖通过LayoutHelper的aspectRatio计算出来的视图高度，因此具备更高优先级。
 ![引用网络上的一张图片](http://upload-images.jianshu.io/upload_images/2546238-571ed5c5bae5af52.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
设置方法如下：
```
//对于LayoutHelper，调用
setAspectRatio(float aspectRatio);
//对于LayoutParams，调用
((VirutalLayoutManager.LayoutParams) layoutParams).mAspectRatio
```

####4. 设置dividerHeight 行间距
这个行间距是针对LinearLayoutHelper的
设置方法如下：
```
setDividerHeight(int dividerHeight);
```

####5.设置weights属性
这个属只是针对于ColumnLayoutHelper, GridLayoutHelper的属性 
- weights属性是一个float数组，每一项代表某一列占父容器宽度的百分比，总和建议是100，否则布局会超出容器宽度；如果布局中有4列，那么weights的长度也应该是4；长度大于4，多出的部分不参与宽度计算；如果小于4，不足的部分默认平分剩余的空间。 
设置方法如下：
```
setWeights(float[] weights);
```
![引用网络上的一张图片](http://upload-images.jianshu.io/upload_images/2546238-78577858fcc7fe15.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

####6.vGap, hGap
这个属性是针对GridLayoutHelper与StaggeredGridLayoutHelper都有这两个属性，分别控制视图之间的垂直间距和水平间距。 

![引用网络上的一张图片](http://upload-images.jianshu.io/upload_images/2546238-e108074b790de073.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
设置方法如下：
```
setHGap(int hGap);
setVGap(int vGap);
```
####7.spanCount, spanSizeLookup
只针对GridLayoutHelper的属性,但通过提供自定义的spanSizeLookUp，可以指定某个位置的视图占用多个网格区域。 
![引用网络上的一张图片](http://upload-images.jianshu.io/upload_images/2546238-f8434c905dc7392e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
设置方法如下：
```
setSpanCount(int spanCount);
setSpanSizeLookup(SpanSizeLookup spanSizeLookup);
```
####8.autoExpand
GridLayoutHelper的属性，当一行里视图的个数少于spanCount值的时候，如果autoExpand为true，视图的总宽度会填满可用区域；否则会在屏幕上留空白区域。 
![引用网络上的一张图片](http://upload-images.jianshu.io/upload_images/2546238-bfeebb68a86914b8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
设置方法如下：
```
setAutoExpand(boolean isAutoExpand);
```

####9.lane
StaggeredGridLayoutHelper中有这个属性，设置瀑布流的列数

设置方法如下：
```
setLane(int lane)
```
####10.fixAreaAdjuster
fix类型的LayoutHelper的属性，在可能需要设置一个相对父容器四个边的偏移量，比如整个页面里有一个固定的标题栏添加在vlayout容器上，vlayout内部的fix类型视图不希望与外部的标题有所重叠，那么就可以设置一个fixAreaAdjuster来做偏移。
 ![引用网络上的一张图片](http://upload-images.jianshu.io/upload_images/2546238-21cfa67d4c4c82c7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
设置方法如下：
```
setAdjuster(FixAreaAdjuster adjuster);
```

####11.alignType, x, y
FixLayoutHelper, ScrollFixLayoutHelper, FloatLayoutHelper的属性，表示吸边时的基准位置，有四个取值，分别是TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT。x和y是相对这四个位置的偏移量，最终的偏移量还要受上述的fixAreaAdjuster影响
- TOP_LEFT：基准位置是左上角，x是视图左边相对父容器的左边距偏移量，y是视图顶边相对父容器的上边距偏移量；
- TOP_RIGHT：基准位置是右上角，x是视图右边相对父容器的右边距偏移量，y是视图顶边相对父容器的上边距偏移量；
- BOTTOM_LEFT：基准位置是左下角，x是视图左边相对父容器的左边距偏移量，y是视图底边相对父容器的下边距偏移量；
- BOTTOM_RIGHT：基准位置是右下角，x是视图右边相对父容器的右边距偏移量，y是视图底边相对父容器的下边距偏移量；
![引用网络上的一张图片](http://upload-images.jianshu.io/upload_images/2546238-deb052dba76b1655.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

设置代码如下：
```
//设置基准调用
setAlignType(int alignType);
//设置偏移量调用
setX(int x);
setY(int y);
```

####12.showType
ScrollFixLayoutHelper的属性，取值有SHOW_ALWAYS, SHOW_ON_ENTER, SHOW_ON_LEAVE。
- SHOW_ALWAYS：与FixLayoutHelper的行为一致，固定在某个位置；
- SHOW_ON_ENTER：默认不显示视图，当页面滚动到这个视图的位置的时候，才显示；
- SHOW_ON_LEAVE：默认不显示视图，当页面滚出这个视图的位置的时候显示； 

![引用网络上的一张图片](http://upload-images.jianshu.io/upload_images/2546238-79331d5a582af869.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
设置代码如下：
```
setShowType(int showType);
```

####13.stickyStart, offset
StickyLayoutHelper的属性，当视图的位置在屏幕范围内时，视图会随页面滚动而滚动；当视图的位置滑出屏幕时，StickyLayoutHelper会将视图固定在顶部（stickyStart = true）或者底部（stickyStart = false），固定的位置支持设置偏移量offset。 

![引用网络上的一张图片](http://upload-images.jianshu.io/upload_images/2546238-a81ac2f362886207.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

设置代码如下：
```
setStickyStart(boolean stickyStart);
setOffset(int offset);
```


###关于这个开源框架里面有好多的细节需要注意，这里面有写得不好的话还希望大神们指点
Demo有很多都没有完善，等到有时间的时候去完善
[Demo地址]()
