# lingshou-core

```
 ___                                 __                        
/\_ \    __                         /\ \                       
\//\ \  /\_\    ___      __     ____\ \ \___     ___   __  __  
  \ \ \ \/\ \ /' _ `\  /'_ `\  /',__\\ \  _ `\  / __`\/\ \/\ \ 
   \_\ \_\ \ \/\ \/\ \/\ \L\ \/\__, `\\ \ \ \ \/\ \L\ \ \ \_\ \
   /\____\\ \_\ \_\ \_\ \____ \/\____/ \ \_\ \_\ \____/\ \____/
   \/____/ \/_/\/_/\/_/\/___L\ \/___/   \/_/\/_/\/___/  \/___/ 
                         /\____/                               
                         \_/__/                                



  ___    ___   _ __    __   
 /'___\ / __`\/\`'__\/'__`\ 
/\ \__//\ \L\ \ \ \//\  __/ 
\ \____\ \____/\ \_\\ \____\
 \/____/\/___/  \/_/ \/____/
 
```

## 项目结构

#### 总览

```
| src/main/java
|
|----/common 放一些工具类, 常量
|
|
|----/contract 放一些公共类结构

```

#### 详细


```
|/common 
|----/bean 
|		bean拷贝相关
|
|----/constant
|		一些常量(TRUE, VALID...)
|
|----/json
|		obj -> jsonStr , jsonStr -> obj
|
|----/time
|		日期相关操作(addDay, addMin...)
|
|----/validate
|		dto入参校验(NotNull, Min...)
|
|
|/contract 放一些公共类结构
|----/code
|		用来代替枚举类
|
|----/exception
|		公用异常
|
|----/resonse
|		公用数据结构(Page, BaseResult...)
|

```


## 部分代码示例

### Code

建议接口中这样定义一些类型值

Dto声明

```java
/**
* 终端类型
* @see TerminalType
*/
private Integer terminalType;
```

Code 声明 - eg:TerminalType

```java

/**
 * Created by diwa on 24/8/2017.
 * 终端类型
 */
public class TerminalType extends Code {
    /**
     * 货架
     */
    @Display("货架")
    public static final int TERMINAL_TYPE_SHELF = 10;
    /**
     * 门店
     */
    @Display("门店")
    public static final int TERMINAL_TYPE_SHOP = 20;
}
```

展示端

```java
String machineType = Code.displayValue(MachineTypeCode.class, machine.getType());
```


why not enum

* pigeon接口更新


### BaseResult

如果你的接口不想把异常抛给上游, 可以用这个接口做统一包装

接口定义:

```java
/**
 * 根据路线id查询对应路线信息
 *
 * @param routeId
 * @return
 */
BaseResult<TmsRouteDTO> queryRouteById(int routeId);
```

### ResultHandler

用来方便调用者解析BaseResult的工具类

ResultHandler的api

```java
public static <T> T handle(BaseResult<T> baseResult);

public static <T> T handle(BaseResult<T> baseResult, EDLogger edLogger)

public static <T> T handle(BaseResult<T> baseResult, HandleAction action)
```

常用调用方法:

```java
直接取值, 如果成功拿到返回结果中泛型值, 如果失败, 则会抛出ServiceException异常, 其中code和msg分别是BaseResult中的code和msg;

List<CityBO> cityBOS = TmsResultHandler.handle(cityBOSResult);


```

如果需要打印异常日志, 方便设置logscan

```java
private static final EDLogger ERROR = LoggerManager.getLogger(LoggerEnum.TMS_ROUTE_ERROR.getValue());

...
...
...

List<CityBO> cityBOS = TmsResultHandler.handle(cityBOSResult, ERROR);

如果出现异常, 则会用传入的logger打印相应日志
```

如果需要做更复杂的定制


```java
private static final EDLogger ERROR = LoggerManager.getLogger(LoggerEnum.TMS_ROUTE_ERROR.getValue());

...
...

List<CityBO> cityBOS = TmsResultHandler.handle(cityBOSResult, () -> {
	//1 打印定制化日志
	ERROR.error("foo");
	//2 
	doYourFunc1();
	//3
	doYourFunc2();
});

支持依靠lambda表达式做个性化定制
```

### RemoteCallWrapper

用于包装外部调用(rpc, db, redis)等

将外部异常, 封装成内部异常, 默认重载方法, 采用ServiceException

也暴露``ExceptionHandler``, 可用lambda复写, 实现定制化异常处理 ``onException``


#### 默认用法

```java

private static final EDLogger INFO = LoggerManager.getLogger(LoggerEnum.BACK_DOOR_INFO.getValue());


@Resource
private TestService testService;


int userId = 10;
User user = RemoteCallWrapper.call("获取用户信息", () -> testService.getUserInfo(userId), ERROR);


```

#### 高级用法, 实现定制化ExceptionHandler


```java

private static final EDLogger INFO = LoggerManager.getLogger(LoggerEnum.BACK_DOOR_INFO.getValue());


@Resource
private TestService testService;


int userId = 10;
User user = RemoteCallWrapper.call("获取用户信息", () -> testService.getUserInfo(userId), ERROR, 
	exception -> {
		//todo
		doSomeThing();
		
		if (exection instanceof TimeOutException){
			//todo
		}else{
			//todo		
		}
}

);


```

### MetricWrapper

适用于日志记录执行某个动作耗时打点

比如以下部分代码, 可以做大幅度重构

```java
private static final EDLogger INFO = LoggerManager.getLogger(LoggerEnum.BACK_DOOR_INFO.getValue());


@Resource
private TestService testService;


long time1 = System.currentTimeMillis();

testService.foo1();


long time2 = System.currentTimeMillis();

INFO.info("action 1 last:" + (time2 - time1));

testService.foo2();

long time3 = System.currentTimeMillis();

INFO.info("action 2 last:" + (time3 - time2));

testService.foo3();

long time4 = System.currentTimeMillis();
INFO.info("action 3 last:" + (time4 - time3));

testService.foo4();

long time5 = System.currentTimeMillis();
INFO.info("action 4 last:" + (time5 - time4));

testService.foo5();

INFO.info("action 5 last:" + (time6 - time5));



```

#### 现在的改造方案

```java
private static final EDLogger INFO = LoggerManager.getLogger(LoggerEnum.BACK_DOOR_INFO.getValue());


@Resource
private TestService testService;

MetricWrapper.handleAndMetric("action1", testService::foo1, INFO);
MetricWrapper.handleAndMetric("action2", testService::foo2, INFO);
MetricWrapper.handleAndMetric("action3", testService::foo3, INFO);
MetricWrapper.handleAndMetric("action4", testService::foo4, INFO);
MetricWrapper.handleAndMetric("action5", testService::foo5, INFO);


```

#### 带参数调用的改造方案

```java
private static final EDLogger INFO = LoggerManager.getLogger(LoggerEnum.BACK_DOOR_INFO.getValue());


@Resource
private TestService testService;

MetricWrapper.handleAndMetric("action1", testService::foo1, INFO);


int arg = 10;
MetricWrapper.handleAndMetric("action1", () -> testService.fooWithArgs(args), INFO);

```

#### 更定制化的改造方案

```java
private static final EDLogger INFO = LoggerManager.getLogger(LoggerEnum.BACK_DOOR_INFO.getValue());

private static final EDLogger ERROR = LoggerManager.getLogger(LoggerEnum.BACK_DOOR_ERROR.getValue());


@Resource
private TestService testService;

MetricWrapper.handleAndMetric("action1", testService::foo1, INFO);


int arg = 10;
MetricWrapper.handleAndMetric("action1", () -> testService.fooWithArgs(args), INFO, ERROR, 
exception -> {
	//log err
	ERROR.error("fail!", e);
	//do something u want
	callRemoteWhenFailed();
	//throw
	throw new BizServiceException(exception);
}
);

```




