### ThreadPoolExecutor异常捕获问题
参考：https://www.51cto.com/article/745551.html


1. submit 封装FutureTask, 返回FutureTask，除非使用get，否则不会抛异常
2. execute 会抛异常

解决方案：
1. 用 execute
2. 实现一个自己的线程池工厂
```
   ThreadFactory factory = (Runnable r) -> {
   //创建一个线程
   Thread t = new Thread(r);
   //给创建的线程设置UncaughtExceptionHandler对象 里面实现异常的默认逻辑
   t.setDefaultUncaughtExceptionHandler((Thread thread1, Throwable e) -> {
   e.printStackTrace();
   });
   return t;
   };
```
3. 重写afterExecute进行异常处理
```
        ExecutorService executorService = new ThreadPoolExecutor(
                2,
                3,
                0,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue(10)
        ) {
            //重写afterExecute方法
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                System.out.println("afterExecute里面获取到异常信息，处理异常" + t.getMessage());
            }
        };
```
## 优雅关闭线程
https://www.cnblogs.com/Andrew-Zhou/p/16099670.html