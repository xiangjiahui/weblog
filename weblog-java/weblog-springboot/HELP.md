# WebLog—个人博客

## 代码结构

````te
---- weblog-springboot(父模块)
  ---- weblog-admin(后台管理相关功能)
  ---- weblog-common(公告模块)
  ---- weblog-web(项目入口)
````

## IDEA代码构建相关
````
如果IDEA没有自动下载依赖,执行命令 mvn dependency:resolve
Maven和IDEA版本适配问题,IDEA2021版本只能适配Maven3.6.3
````

