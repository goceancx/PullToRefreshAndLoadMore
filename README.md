<img src="https://github.com/oceancx/PullToRefreshAndLoadMore/blob/master/gif/pull_to_refresh_and_loadmore.gif" >

简介:alpha版本

特点在于用LinearLayout把header + body + footer布局在了一起 ,然后用ViewDragHelper来处理手势,
只支持RecyclerView,在onTouchEvent的ACTION_UP里面来判断RecyclerView是否滚动到底部和顶部
