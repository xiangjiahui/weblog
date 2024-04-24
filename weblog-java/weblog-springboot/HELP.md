# WebLog—个人博客

## 项目结构

````text
---- weblog-springboot(父模块)
  ---- weblog-admin(后台管理相关功能)
  ---- weblog-common(公告模块)
  ---- weblog-web(项目入口)
````



## IDEA代码构建相关

````text
如果IDEA没有自动下载依赖,执行命令 mvn dependency:resolve
Maven和IDEA版本适配问题,IDEA2021版本只能适配Maven3.6.3
````



## SpringSecurity整合JWT实现身份认证

### 1、封装工具类

````text
封装JwtTokenUtil工具类，用于生成解析Token、生成Base64安全密钥。
封装PasswordEncoder工具，用于密码加密。（BCryptPasswordEncoder）
````

### 2、实现UserDetailsService：Spring Security 用户详情服务

````text
UserDetailsService 是 Spring Security 提供的接口，用于从应用程序的数据源（如数据库、LDAP、内存等）中加载用户信息。
它是一个用于将用户详情加载到 Spring Security 的中心机制。UserDetailsService 主要负责两项工作：
1、加载用户信息:从数据源中加载用户的用户名、密码和角色等信息。
2、创建 UserDetails 对象:根据加载的用户信息，创建一个 Spring Security 所需的 UserDetails 对象，包含用户名、密码、角色和权限等。

实现这个接口后，将用户的用户名、密码、角色信息返回给SpringSecurity
````

### 3、自定义认证过滤器

````text
自定义个过滤器，继承AbstractAuthenticationProcessingFilter类。
用于处理 JWT（JSON Web Token）的用户身份验证过程。
在构造函数中，调用父类 AbstractAuthenticationProcessingFilter 的构造函数，通过 AntPathRequestMatcher 指定处理用户登录的访问地址。这意味着当请求路径匹配 /login 并且请求方法为 POST 时，该过滤器将被触发。

重写attemptAuthentication() 方法用于实现用户身份验证的具体逻辑。首先解析提交的 JSON 数据，并获取用户名、密码，校验是否为空，若不为空，则将它们封装到 UsernamePasswordAuthenticationToken 中。最后，使用 getAuthenticationManager().authenticate() 来触发 Spring Security 的身份验证管理器执行实际的身份验证过程，然后返回身份验证结果。
````

### 4、自定义处理器

````text
自定义两个处理器，一个是验证成功的处理器，一个是验证失败的处理器。分别实现AuthenticationSuccessHandler和AuthenticationFailureHandler接口。

如登录成功，则返回 Token 令牌，登录失败，则返回对应的提示信息。在 Spring Security 中，AuthenticationFailureHandler 和 AuthenticationSuccessHandler 是用于处理身份验证失败和成功的接口。它们允许您在用户身份验证过程中自定义响应，以便更好地控制和定制用户体验。
````

### 5、自定义JWT认证功能配置

````text
完成以上前置准备工作后，开始配置JWT认证相关配置。

自定义一个配置类，继承SecurityConfigurerAdapter类，用于在 Spring Security 配置中添加自定义的认证过滤器和提供者。通过重写 configure() 方法，我们将之前写好过滤器、认证成功、失败处理器、UserDetailService，以及加密算法整合到httpSecurity 中。
````

### 6、应用JWT认证功能配置

````text
自定义配置类，继承WebSecurityConfigurerAdapter类，注入自定义的JWT认证功能配置类。
重写WebSecurityConfigurerAdapter类的configure方法，应用自定义的JWT认证功能配置类。
````



## JSR 380 参数校验注解

````text
@NotNull: 验证对象值不应为 null。
@AssertTrue: 验证布尔值是否为 true。
@AssertFalse: 验证布尔值是否为 false。
@Min(value): 验证数字是否不小于指定的最小值。
@Max(value): 验证数字是否不大于指定的最大值。
@DecimalMin(value): 验证数字值（可以是浮点数）是否不小于指定的最小值。
@DecimalMax(value): 验证数字值（可以是浮点数）是否不大于指定的最大值。
@Positive: 验证数字值是否为正数。
@PositiveOrZero: 验证数字值是否为正数或零。
@Negative: 验证数字值是否为负数。
@NegativeOrZero: 验证数字值是否为负数或零。
@Size(min, max): 验证元素（如字符串、集合或数组）的大小是否在给定的最小值和最大值之间。
@Digits(integer, fraction): 验证数字是否在指定的位数范围内。例如，可以验证一个数字是否有两位整数和三位小数。
@Past: 验证日期或时间是否在当前时间之前。
@PastOrPresent: 验证日期或时间是否在当前时间或之前。
@Future: 验证日期或时间是否在当前时间之后。
@FutureOrPresent: 验证日期或时间是否在当前时间或之后。
@Pattern(regexp): 验证字符串是否与给定的正则表达式匹配。
@NotEmpty: 验证元素（如字符串、集合、Map 或数组）不为 null，并且其大小/长度大于0。
@NotBlank: 验证字符串不为 null，且至少包含一个非空白字符。
@Email: 验证字符串是否符合有效的电子邮件格式。
````

