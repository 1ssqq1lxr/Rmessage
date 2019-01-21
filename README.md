# 欢迎阅读Rmessage文档

##  技术栈
Rmessage是采用Reactor3,基于reactor-netty项目构建的实时消息推送api。
 - 什么是Reactor3?
 
   Reactor 是一个用于JVM的完全非阻塞的响应式编程框架，具备高效的需求管理（即对 “背压（backpressure）”的控制）能力。它与 Java 8 函数式 API 直接集成，比如 CompletableFuture， Stream， 以及 Duration。它提供了异步序列 API Flux（用于[N]个元素）和 Mono（用于 [0|1]个元素），并完全遵循和实现了“响应式扩展规范”（Reactive Extensions Specification）。
 - 使用Reactor好处?
 
   非常容易构建高吞吐量纯异步的代码,还有就是能够无缝整合spring5[webflux]项目。

##  项目简介
使用Rmessage你需要外部管理群组用户关系,以及离线消息存储，Rmessage不提供持久化，测试可以使用默认Handler内存保留离线消息。
整个项目采用纯异步的编程思想去开发，旨在学习reactive programming。



##  目前支持的功能
1. 单聊
2. 群聊
3. 离线消息落地以及拉取
4. 离线在线通知管理
5. 心跳,连接鉴权机制
6. 群组关系管理
7. 支持多端在线
8. 目前支持tcp协议，项目设计时考虑多协议扩展。
9. 消息QOS机制(还未实现)




##  快速开始
- **服务端**

```java
 ServerStart
     .builder()
     .tcp()
     .ip("127.0.0.1")
     .port(1888)
     .interceptor(frame -> frame,frame -> frame)
     .setAfterChannelInit(channel -> {//  channel设置
      })
     .connect()
     .cast(TcpServerSession.class)
     .subscribe(session->{
       session.addGroupHandler(groupId -> null).subscribe(); // 设置群组管理handler
       session.addOfflineHandler(new DefaultOffMessageHandler()).subscribe(); // 设置离线消息handler
       session.addUserHandler(new DefaultUserTransportHandler()).subscribe(); // 设置用户关系管handler
  });
```

- **客户端**

```java
 ClientStart
   .builder()
   .tcp()
   .ip("127.0.0.1")
   .port(1888)
   .userId("21344")
   .onReadIdle(10000l,()->()->System.out.println("心跳了"))
   .setClientType(ClientType.Ios)
   .setAfterChannelInit(channel -> {
                    //  channel设置
    })
   .connect()
   .cast(TcpClientSession.class)
   .subscribe(session->{
       session.sendPoint("123","测试一下哦").subscribe(); // session　操作类
   });
```
## 协议设计
- **单聊  多聊**
 
FixHeader  【1 byte】

| client_type |  message_type |
| --------   | :----:  |
|  high 4bit       |  low 4bit      |

 Topic     【n byte】

|  from length  |  to length  |  from   |   to |
| ------------ | ------------ | ------------ | ------------ |
|   1byte  |  1byte |  n byte  |    n byte |

 Body    【n byte】

| body length    | body |
| ------------- | ------------- |
| 2 byte  | n byte  |

| timstamp |
| ------------- |
| 8 byte    |


- **Online报文/Offline报文**

FixHeader  【1 byte】
   
   | client_type |  message_type |
   | --------   | :----:  |
   |  high 4bit       |  low 4bit      |

ConnectionState  【n byte】

| user length |  user |
| --------   | :----:  |
|  1 byte      |  n byte     |


- **Ping报文/Pong报文**

FixHeader  【1 byte】

| client_type |  message_type |
| --------   | :----:  |
|  high 4bit       |  low 4bit      |

