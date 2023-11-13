server:
  port: %t9Port%

logging:
  config: ${rose.mary.home}/config/logback-spring.xml
  level:
    root: debug

spring:
  application:
    name: t9
  #    admin:
  #     enabled: true
  # boot:
  #  admin:
  #    client:
  #      url: http://localhost:8092
  #      instance:
  #        name: project t9
  main:
    banner-mode: CONSOLE
  h2:
    console:
      enabled: true
      path: /h2
      settings:
        web-allow-others: true
  datasources:
    datasource01:
      jdbcUrl: %jdbcUrl%
      driver-class-name: %driverClassName%
      username: %username%
      password: %password%
      minimum-idle: 14
      maximum-pool-size: 20
      idle-timeout: 10000
      connection-timeout: 5000

  #  datasource02:
  #   #jdbcUrl: jdbc:oracle:thin:@10.10.1.35:1521:iip
  #   jdbcUrl: jdbc:oracle:thin:@idc.mocomsys.com:31521:iip
  #   driver-class-name: oracle.jdbc.driver.OracleDriver
  #   username: iipdmc
  #   password: iipdmc
  #   minimum-idle: 10
  #   maximum-pool-size: 12
  #   #idle-timeout: 10000
  #   connection-timeout: 30000
  #  datasource03:
  #   #jdbc-url: jdbc:h2:tcp://localhost/~/LOADER;AUTO_SERVER=TRUE
  #   #jdbc-url: jdbc:h2:file:~/LOADER;AUTO_SERVER=TRUE;FILE_LOCK=SOCKET
  #   url: jdbc:h2:file:~/TRACE;AUTO_SERVER=TRUE
  #   driver-class-name: org.h2.Driver
  #   username: rose
  #   password: rose
  messages:
    always-use-message-format: false
    basename: messages/error, messages/message
    cache-second: -1
    encoding: UTF-8
  task:
    execution:
      pool:
        max-size: 100
        queue-capacity: 200
        keep-alive: 10s
    scheduling:
      pool:
        size: 10
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: ALWAYS
    shutdown:
      enabled: true
    metrics:
      enabled: true
---
spring:
  profiles: development

logging:
  level:
    root: debug

---
spring:
  profiles: production

logging:
  level:
    root: info
