1. refreshAfterWrite < now < expireAfterWrite 时，guava和caffeine缓存击穿时都支持单线程去加载数据，区别是guava是其中一个请求线程去加载数据的，而
caffeine是ForkJoin#commonPool其中一个线程异步加载的（参考：https://cloud.tencent.com/developer/article/1780929）
2. 例子参考
- (guava) [CacheTest.java](caffeine%2FCacheTest.java)
- (caffeine) [AsyncLoadingCacheTest.java](guava%2FAsyncLoadingCacheTest.java)