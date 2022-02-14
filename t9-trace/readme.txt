2020-04-28 15:10:07.629 DEBUG 37482 --- [           main] o.s.b.f.s.DefaultListableBeanFactory     : Creating shared instance of singleton bean 'org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerInvoker'
2020-04-28 15:10:07.632 DEBUG 37482 --- [           main] o.s.b.f.s.DefaultListableBeanFactory     : Creating shared instance of singleton bean 'spring.datasource-org.springframework.boot.autoconfigure.jdbc.DataSourceProperties'
2020-04-28 15:10:07.648 DEBUG 37482 --- [           main] o.s.b.f.s.DefaultListableBeanFactory     : Autowiring by type from bean name 'org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerInvoker' via constructor to bean named 'spring.datasource-org.springframework.boot.autoconfigure.jdbc.DataSourceProperties'
2020-04-28 15:10:07.649 DEBUG 37482 --- [           main] o.s.b.f.s.DefaultListableBeanFactory     : Autowiring by type from bean name 'org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerInvoker' via constructor to bean named 'org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@1324409e'
2020-04-28 15:10:07.668 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : HikariPool-1 - configuration:
2020-04-28 15:10:07.673 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : allowPoolSuspension.............false
2020-04-28 15:10:07.675 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : autoCommit......................true
2020-04-28 15:10:07.676 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : catalog.........................none
2020-04-28 15:10:07.676 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : connectionInitSql...............none
2020-04-28 15:10:07.677 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : connectionTestQuery.............none
2020-04-28 15:10:07.677 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : connectionTimeout...............30000
2020-04-28 15:10:07.678 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : dataSource......................none
2020-04-28 15:10:07.678 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : dataSourceClassName.............none
2020-04-28 15:10:07.679 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : dataSourceJNDI..................none
2020-04-28 15:10:07.680 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : dataSourceProperties............{password=<masked>}
2020-04-28 15:10:07.681 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : driverClassName................."oracle.jdbc.driver.OracleDriver"
2020-04-28 15:10:07.681 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : healthCheckProperties...........{}
2020-04-28 15:10:07.682 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : healthCheckRegistry.............none
2020-04-28 15:10:07.682 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : idleTimeout.....................600000
2020-04-28 15:10:07.683 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : initializationFailTimeout.......1
2020-04-28 15:10:07.683 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : isolateInternalQueries..........false
2020-04-28 15:10:07.684 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : jdbcUrl.........................jdbc:oracle:thin:@idc.mocomsys.com:31521:iip
2020-04-28 15:10:07.684 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : leakDetectionThreshold..........0
2020-04-28 15:10:07.686 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : maxLifetime.....................1800000
2020-04-28 15:10:07.687 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : maximumPoolSize.................20
2020-04-28 15:10:07.687 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : metricRegistry..................none
2020-04-28 15:10:07.688 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : metricsTrackerFactory...........none
2020-04-28 15:10:07.691 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : minimumIdle.....................14
2020-04-28 15:10:07.692 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : password........................<masked>
2020-04-28 15:10:07.692 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : poolName........................"HikariPool-1"
2020-04-28 15:10:07.693 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : readOnly........................false
2020-04-28 15:10:07.693 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : registerMbeans..................false
2020-04-28 15:10:07.693 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : scheduledExecutor...............none
2020-04-28 15:10:07.694 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : schema..........................none
2020-04-28 15:10:07.694 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : threadFactory...................internal
2020-04-28 15:10:07.695 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : transactionIsolation............default
2020-04-28 15:10:07.695 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : username........................"iipdmc"
2020-04-28 15:10:07.695 DEBUG 37482 --- [           main] com.zaxxer.hikari.HikariConfig           : validationTimeout...............5000
2020-04-28 15:10:07.696  INFO 37482 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2020-04-28 15:10:07.709  WARN 37482 --- [           main] com.zaxxer.hikari.util.DriverDataSource  : Registered driver with driverClassName=oracle.jdbc.driver.OracleDriver was not found, trying direct instantiation.
2020-04-28 15:10:07.709 DEBUG 37482 --- [           main] com.zaxxer.hikari.util.DriverDataSource  : Driver class oracle.jdbc.driver.OracleDriver found in Thread context class loader TomcatEmbeddedWebappClassLoader
  context: ROOT
  delegate: true
----------> Parent Classloader:






[actuator url]
http://localhost:8090/actuator

[jdbc properties]
datasource01: {
prefix: "spring.datasources.datasource01",
properties: {
initializationFailTimeout: 1,
validationTimeout: 5000,
hikariPoolMXBean: { },
readOnly: false,
registerMbeans: false,
healthCheckProperties: { },
isolateInternalQueries: false,
leakDetectionThreshold: 0,
maxLifetime: 1800000,
minimumIdle: 10,
password: "******",
metricsTrackerFactory: { },
allowPoolSuspension: false,
idleTimeout: 600000,
dataSourceProperties: { },
driverClassName: "oracle.jdbc.driver.OracleDriver",
jdbcUrl: "jdbc:oracle:thin:@idc.mocomsys.com:31521:iip",
loginTimeout: 30,
maximumPoolSize: 10,
autoCommit: true,
connectionTimeout: 30000,
username: "projectq",
poolName: "HikariPool-1"
}
},

[rfh2]
- <mcd>
  <Msd>xml</Msd> 
  <Set /> 
  <Type /> 
  <Fmt /> 
  </mcd>


- <usr>
- <mte_info>
- <data_key_info>
- <rec_1>
  <_status_>0</_status_> 
  <A>30</A> 
  </rec_1>
  </data_key_info>
- <interface_info>
  <host_id>ANGEL</host_id> 
  <group_id>DEV</group_id> 
  <intf_id>a</intf_id> 
  <seq_no>171030208000</seq_no> 
  <date>20030820</date> 
  <time>171030208000</time> 
  </interface_info>
- <prev_host_info>
  <host_id /> 
  <process_id /> 
  </prev_host_info>
- <host_info>
  <host_id>ANGEL</host_id> 
  <os_type>WINNT-ANGEL-angel</os_type> 
  <os_version /> 
  <process_id /> 
  <user_name /> 
  </host_info>
- <process_info>
  <date>20030820</date> 
  <time>171030208000</time> 
  <hop_cnt>1</hop_cnt> 
  <process_mode>SNDR</process_mode> 
  <process_type>ADTR</process_type> 
  <process_id>CODEIF_ADT</process_id> 
  <hub_cnt>0</hub_cnt> 
  <recv_spoke_cnt>1</recv_spoke_cnt> 
  <timezone>-32400</timezone> 
  <elaspsed_time>10</elaspsed_time> 
  </process_info>
- <status_info>
  <status>0</status> 
  <error_type /> 
  <error_code /> 
  <error_reason /> 
  <error_message /> 
  <errorq_msgid /> 
  <errorq /> 
  </status_info>
- <policy_info>
  <error_policy>0</error_policy> 
  </policy_info>
  <sender_info /> 
  - <sender_info>
  <file_name>%SEND_FILENAME%</file_name> 
  </sender_info>
- <receiver_info>
  <host_id>a</host_id>
  <directory>%SEND_DIR%</directory> 
  <filename>%SEND_FILE%</filename> 
  </receiver_info>
- <data_info>
  <record_cnt>1</record_cnt> 
  <record_size>0</record_size> 
  <data_size>36</data_size> 
  <data_compress>N</data_compress> 
  <compression_method /> 
  <compression_mode /> 
  <compressed_size /> 
  <data_conversion /> 
  <conv_method /> 
  <conv_mode /> 
  <converted_size /> 
  </data_info>
  </mte_info>
  </usr>
  