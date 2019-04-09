# Dpider
参考网上大佬的一张分布式爬虫的架构图，对其的一个实现。因为时间比较急，所以在某些地方跟原架构图不一样（有的省去了，有的用的其他技术），但总体架构是按照这个来的。文章很棒，推荐大家看一看！
- 文章如下：
  [分布式爬虫系统设计、实现与实战：爬取京东、苏宁易购全网手机商品数据+MySQL、HBase存储](https://blog.51cto.com/xpleaf/2093952)
- 架构图如下:
  ![](http://ww1.sinaimg.cn/large/a4971773ly1g1w6njiy6wj21fc0wln61.jpg)

## 系统分为如下几部分
根据架构图，系统分为
- URL调度系统，对应着dpider-urlScheduler模块
- 子爬虫，对应着dpider-spider模块
- 监控系统，对应着dpider-monitor模块

其中子爬虫、URL调度系统为集群，监控系统为单点监控。

以下是对这三个模块的介绍。

## URL调度系统

是整个分布式爬虫系统的任务调度系统，向外暴露两个方法：poll，push。另外还具有URL去重功能，后面还可以加入增量爬取支持，在URL调度系统侧的爬虫任务量统计等功能。

通过dubbo向外暴露服务。

使用spring-quartz定时向zk报告自身信息。

这里可以有多种实现，这里先只提供了一种Redis实现，该实现包括了poll，push，去重功能。在redis中有三个存储：

1. 使用set结构的一个已消费存储，存储的是已经消费过（已经被push过）的URL，用来去重用的，后面的增量爬取支持，在URL调度系统侧的爬虫任务量统计等功能也需要使用到它。
2. 使用list结构的一个高优先级队列，存储的是高优先级的URL（比如说列表页），队列的实现方式是利用redis的左边添加，右边弹的方式。
3. 使用list结构的一个低优先级队列，存储的是低优先级的URL（比如说详情页）。

### poll方法

用于子爬虫从URL管理队列中获取需要爬取的链接。先从高优先级队里获取，如果没有的话，再从低优先级队列里获取。

### push方法

用于子爬虫向URL管理队列添加新的链接。先去已消费队列中看看是不是存在，如果存在则丢弃，否则判断是哪种URL并放入对应队列中。

## 子爬虫-spider

获取一个URL，下载其页面，解析其页面，将获得数据进行持久化等操作（添加进数据库等），将获取到的新的URL添加回URL调度系统。

以上都是基于webMagic实现，架构图中其内部的download，parser等部件跟webMagic一样。这里因为时间原因没有实现IP池。后面我觉得可以专门写一个IP代理管理系统，该系统可以通过不同的方式添加可用IP（爬虫爬取或者人工添加），自己管理失效IP等，向外暴露服务。

使用spring-quartz定时向zk报告自身信息。

后面还准备实现监控端的动态启停。

## 监控系统-monitor

基于Monitor接口，可以有多种实现，现在只提供了Zookeeper的实现方式。

他实现了两个主要功能：监听子爬虫节点、URL调度系统节点的上下线状态并通知给工作人员，web端的节点状态信息（爬虫的工作量，健康状况，URL调度系统节点的信息统计等）的监控及一些操作。

### 监听节点情况

使用zk的注册watcher的方式，监听节点状态，当有节点上下线的时候，调用Notice接口进行通知。这里的Notice后面也可以单拿出来做成一个系统，使其可以通过dubbo或者消息队列向外提供服务。

### web端操作支持

主要是监控一些信息，后面会加入动态启停等主动干预操作。