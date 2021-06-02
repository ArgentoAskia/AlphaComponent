# AlphaToolkit
AlphaToolkit（ATK），一个可以帮助你快速迭代出客户端组件和服务器组件的网络IO(BIO)小构件。只需要几行代码，就可以构建一个服务器啦！！！

基于Java的BIO网络通信制作。

## 😊使用方法

```
  AlphaBroadcastServer alphaServer = new AlphaBroadcastServer(13251);
  alphaServer.setResponsibility(new FileTransmissionResp("D:\\AlphaFileTranslate", "根目录"));
  alphaServer.waitForConnected();
```

上面的代码将建立一个文件共享服务器

## 👌Todo

- [ ] 添加线程池框架支持
- [x] 转发服务器
- [x] 对点服务器
- [ ] 代理服务器
- [ ] 引入设计模式
- [x] 文件共享实例
- [x] 时间同步测试实例
- [ ] 创建NIO版本

## Feature Do

- [ ] 创建AIO版本

## 当前版本为dev 1.9

