package org.liuweiwei;

import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * TODO -> DEV 开发(dev+ops)
 * Spring Native [Experimental]
 * Incubating support for compiling Spring applications to native executables using the GraalVM native-image compiler.
 * 孵化支持使用 GraalVM 本机图像编译器将 Spring 应用程序编译为本机可执行文件。
 *
 * Spring Boot DevTools
 * Provides fast application restarts, LiveReload, and configurations for enhanced development experience.
 * 提供快速的应用程序重启、LiveReload 和配置以增强开发体验。
 *
 * Lombok
 * Java annotation library which helps to reduce boilerplate code.
 * Java 注释库，有助于减少样板代码。
 *
 * Spring Configuration Processor
 * Generate metadata for developers to offer contextual help and "code completion" when working with custom configuration keys (ex.application.properties/.yml files).
 * 为开发人员生成元数据，以便在使用自定义配置键（例如，application.properties/.yml 文件）时提供上下文帮助和"代码完成"。
 *
 * TODO -> WEB
 * Spring Web
 * Build web, including RESTFul, applications using Spring MVC. Uses Apache Tomcat as the default embedded container.
 * 使用 Spring MVC 构建 Web（包括 RESTFul）应用程序。 使用 Apache Tomcat 作为默认的嵌入式容器。
 *
 * Spring Reactive Web
 * Build reactive web applications with Spring WebFlux and Netty.
 * 使用 Spring WebFlux 和 Netty 构建响应式 Web 应用程序。
 *
 * Rest Repositories
 * Exposing Spring Data repositories over REST via Spring Data REST.
 * 通过 Spring Data REST 在 REST 上公开 Spring Data 存储库。
 *
 * Spring Session
 * Provides an API and implementations for managing user session information.
 * 提供用于管理用户会话信息的 API 和实现。
 *
 * Rest Repositories HAL Explorer
 * Browsing Spring Data REST repositories in your browser.
 * 在浏览器中浏览 Spring Data REST 存储库。
 *
 * Spring HATEOAS
 * Eases the creation of RESTFul APIs that follow the HATEOAS principle when working with Spring / Spring MVC.
 * 在使用 Spring / Spring MVC 时，简化遵循 HATEOAS 原则的 RESTFul API 的创建。
 *
 * Spring Web Services
 * Facilitates contract-first SOAP development. Allows for the creation of flexible web services using one of the many ways to manipulate XML payloads.
 * 促进契约优先的 SOAP 开发。 允许使用多种操作 XML 负载的方法之一创建灵活的 Web 服务。
 *
 * Jersey
 * Framework for developing RESTFul Web Services in Java that provides support for JAX-RS APIs.
 * 用于在 Java 中开发 RESTFul Web 服务的框架，为 JAX-RS API 提供支持。
 *
 * TODO -> TEMPLATE ENGINES
 * Thymeleaf
 * A modern server-side Java template engine for both web and standalone environments. Allows HTML to be correctly displayed in browsers and as static prototypes.
 * 适用于 Web 和独立环境的现代服务器端 Java 模板引擎。 允许 HTML 在浏览器中正确显示并作为静态原型。
 *
 * Apache Freemarker
 * Java library to generate text output (HTML web pages, e-mails, configuration files, source code, etc.) based on templates and changing data.
 * 基于模板和不断变化的数据生成文本输出（HTML 网页、电子邮件、配置文件、源代码等）的 Java 库。
 *
 * Mustache
 * Logic-less Templates. There are no if statements, else clauses, or for loops. Instead there are only tags.
 * 无逻辑模板。 没有 if 语句、else 子句或 for 循环。 相反，只有标签。
 *
 * Groovy Templates
 * Groovy templating engine.
 * Groovy 模板引擎。
 *
 * TODO -> SECURITY
 * Spring Security
 * Highly customizable authentication and access-control framework for Spring applications.
 * 用于 Spring 应用程序的高度可定制的身份验证和访问控制框架。
 *
 * OAuth2 Client
 * Spring Boot integration for Spring Security's OAuth2/OpenID Connect client features.
 * Spring Security 的 OAuth2/OpenID Connect 客户端功能的 Spring Boot 集成。
 *
 * OAuth2 Resource Server
 * Spring Boot integration for Spring Security's OAuth2 resource server features.
 * Spring Security 的 OAuth2 资源服务器功能的 Spring Boot 集成。
 *
 * Spring LDAP
 * Makes it easier to build Spring based applications that use the Lightweight Directory Access Protocol.
 * 使构建使用轻量级目录访问协议的基于 Spring 的应用程序变得更加容易。
 *
 * Okta
 * Okta specific configuration for Spring Security/Spring Boot OAuth2 features. Enable your Spring Boot application to work with Okta via OAuth 2.0/OIDC.
 * 针对 Spring Security/Spring Boot OAuth2 功能的 Okta 特定配置。 使您的 Spring Boot 应用程序能够通过 OAuth 2.0/OIDC 与 Okta 一起使用。
 *
 * TODO -> SQL
 * JDBC API
 * Database Connectivity API that defines how a client may connect and query a database.
 * 定义客户端如何连接和查询数据库的数据库连接 API。
 *
 * Spring Data JPA
 * Persist data in SQL stores with Java Persistence API using Spring Data and Hibernate.
 * 使用 Spring Data 和 Hibernate 使用 Java Persistence API 将 SQL 存储中的数据持久化。
 *
 * Spring Data JDBC
 * Persist data in SQL stores with plain JDBC using Spring Data.
 * 使用 Spring Data 使用普通 JDBC 将数据保存在 SQL 存储中。
 *
 * Spring Data R2DBC
 * Provides Reactive Relational Database Connectivity to persist data in SQL stores using Spring Data in reactive applications.
 * 提供反应式关系数据库连接，以在反应式应用程序中使用 Spring Data 将数据保存在 SQL 存储中。
 *
 * MyBatis Framework
 * Persistence framework with support for custom SQL, stored procedures and advanced mappings. MyBatis couples objects with stored procedures or SQL statements using a XML descriptor or annotations.
 * 支持自定义 SQL、存储过程和高级映射的持久性框架。 MyBatis 使用 XML 描述符或注解将对象与存储过程或 SQL 语句耦合。
 *
 * Liquibase Migration
 * Liquibase database migration and source control library.
 * Liquibase 数据库迁移和源代码控制库。
 *
 * Flyway Migration
 * Version control for your database so you can migrate from any version (incl. an empty database) to the latest version of the schema.
 * 数据库的版本控制，以便您可以从任何版本（包括空数据库）迁移到架构的最新版本。
 *
 * JOOQ Access Layer
 * Generate Java code from your database and build type safe SQL queries through a fluent API.
 * 从您的数据库生成 Java 代码并通过流畅的 API 构建类型安全的 SQL 查询。
 *
 * IBM DB2 Driver
 * A JDBC driver that provides access to IBM DB2.
 * 提供对 IBM DB2 的访问的 JDBC 驱动程序。
 *
 * Apache Derby Database
 * An open source relational database implemented entirely in Java.
 * 一个完全用 Java 实现的开源关系数据库。
 *
 * H2 Database
 * Provides a fast in-memory database that supports JDBC API and R2DBC access, with a small (2mb) footprint. Supports embedded and server modes as well as a browser based console application.
 * 提供支持 JDBC API 和 R2DBC 访问的快速内存数据库，占用空间小 (2mb)。支持嵌入式和服务器模式以及基于浏览器的控制台应用程序。
 *
 * HyperSQL Database
 * Lightweight 100% Java SQL Database Engine.
 * 轻量级 100% Java SQL 数据库引擎。
 *
 * MariaDB Driver
 * MariaDB JDBC and R2DBC driver.
 * MariaDB JDBC 和 R2DBC 驱动程序。
 *
 * MS SQL Server Driver
 * A JDBC and R2DBC driver that provides access to Microsoft SQL Server and Azure SQL Database from any Java application.
 * 一个 JDBC 和 R2DBC 驱动程序，可从任何 Java 应用程序提供对 Microsoft SQL Server 和 Azure SQL 数据库的访问。
 *
 * MySQL Driver
 * MySQL JDBC and R2DBC driver.
 * MySQL JDBC 和 R2DBC 驱动程序。
 *
 * Oracle Driver
 * A JDBC driver that provides access to Oracle.
 * 提供对 Oracle 的访问的 JDBC 驱动程序。
 *
 * TODO -> NOSQL
 * Spring Data Redis (Access+Driver)
 * Advanced and thread-safe Java Redis client for synchronous, asynchronous, and reactive usage. Supports Cluster, Sentinel, Pipelining, Auto-Reconnect, Codecs and much more.
 * 用于同步、异步和反应式使用的高级和线程安全 Java Redis 客户端。支持集群、哨兵、流水线、自动重新连接、编解码器等等。
 *
 * Spring Data Reactive Redis
 * Access Redis key-value data stores in a reactive fashion with Spring Data Redis.
 * 使用 Spring Data Redis 以响应方式访问 Redis 键值数据存储。
 *
 * Spring Data MongoDB
 * Store data in flexible, JSON-like documents, meaning fields can vary from document to document and data structure can be changed over time.
 * 将数据存储在灵活的类似 JSON 的文档中，这意味着字段可能因文档而异，并且数据结构可以随着时间的推移而改变。
 *
 * Spring Data Reactive MongoDB
 * Provides asynchronous stream processing with non-blocking back pressure for MongoDB.
 * 为 MongoDB 提供具有非阻塞背压的异步流处理。
 *
 * Spring Data Elasticsearch (Access+Driver)
 * A distributed, RESTFul search and analytics engine with Spring Data Elasticsearch.
 * 带有 Spring Data Elasticsearch 的分布式 RESTFul 搜索和分析引擎。
 *
 * Spring Data for Apache Cassandra
 * A free and open-source, distributed, NoSQL database management system that offers high-scalability and high-performance.
 * 一个免费的开源分布式 NoSQL 数据库管理系统，可提供高可扩展性和高性能。
 *
 * Spring Data Reactive for Apache Cassandra
 * Access Cassandra NoSQL Database in a reactive fashion.
 * 以反应方式访问 Cassandra NoSQL 数据库。
 *
 * Spring for Apache Geode
 * Apache Geode is a data management platform that helps users build real-time, highly concurrent, highly performant and reliable Spring Boot applications at scale that is compatible with Pivotal Cloud Cache.
 * Apache Geode 是一个数据管理平台，可帮助用户大规模构建与 Pivotal Cloud Cache 兼容的实时、高并发、高性能和可靠的 Spring Boot 应用程序。
 *
 * Spring Data Couchbase
 * NoSQL document-oriented database that offers in memory-first architecture, geo-distributed deployments, and workload isolation.
 * NoSQL 面向文档的数据库，提供内存优先架构、地理分布式部署和工作负载隔离。
 *
 * Spring Data Reactive Couchbase
 * Access Couchbase NoSQL database in a reactive fashion with Spring Data Couchbase.
 * 使用 Spring Data Couchbase 以响应式方式访问 Couchbase NoSQL 数据库。
 *
 * Spring Data Neo4j
 * An open source NoSQL database that stores data structured as graphs consisting of nodes, connected by relationships.
 * 一个开源的 NoSQL 数据库，它存储结构化为由节点组成的图形的数据，通过关系连接。
 *
 * TODO -> MESSAGING
 * Spring Integration
 * Adds support for Enterprise Integration Patterns. Enables lightweight messaging and supports integration with external systems via declarative adapters.
 * 添加对企业集成模式的支持。启用轻量级消息传递并支持通过声明式适配器与外部系统集成。
 *
 * Spring for RabbitMQ
 * Gives your applications a common platform to send and receive messages, and your messages a safe place to live until received.
 * 为您的应用程序提供一个通用平台来发送和接收消息，并为您的消息提供一个安全的地方，直到收到为止。
 *
 * Spring for Apache Kafka
 * Publish, subscribe, store, and process streams of records.
 * 发布、订阅、存储和处理记录流。
 *
 * Spring for Apache Kafka Streams
 * Building stream processing applications with Apache Kafka Streams.
 * 使用 Apache Kafka Streams 构建流处理应用程序。
 *
 * Spring for Apache ActiveMQ 5
 * Spring JMS support with Apache ActiveMQ 'Classic'.
 * Spring JMS 支持 Apache ActiveMQ 'Classic'。
 *
 * Spring for Apache ActiveMQ Artemis
 * Spring JMS support with Apache ActiveMQ Artemis.
 * Spring JMS 支持 Apache ActiveMQ Artemis。
 *
 * WebSocket
 * Build WebSocket applications with SockJS and STOMP.
 * 使用 SockJS 和 STOMP 构建 WebSocket 应用程序。
 *
 * RSocket
 * RSocket.io applications with Spring Messaging and Netty.
 * 带有 Spring Messaging 和 Netty 的 RSocket.io 应用程序。
 *
 * Apache Camel
 * Apache Camel is an open source integration framework that empowers you to quickly and easily integrate various systems consuming or producing data.
 * Apache Camel 是一个开源集成框架，使您能够快速轻松地集成使用或生成数据的各种系统。
 *
 * Solace PubSub+
 * Connect to a Solace PubSub+ Advanced Event Broker to publish, subscribe, request/reply and store/replay messages
 * 连接到 Solace PubSub+ 高级事件代理以发布、订阅、请求/回复和存储/重播消息
 *
 * TODO -> I/O
 * Spring Batch
 * Batch applications with transactions, retry/skip and chunk based processing.
 * 具有事务、重试/跳过和基于块的处理的批处理应用程序。
 *
 * Validation
 * Bean Validation with Hibernate validator.
 * 使用 Hibernate 验证器进行 Bean 验证。
 *
 * Java Mail Sender
 * Send email using Java Mail and Spring Framework's JavaMailSender.
 * 使用 Java Mail 和 Spring Framework 的 JavaMailSender 发送电子邮件。
 *
 * Quartz Scheduler
 * Schedule jobs using Quartz.
 * 使用 Quartz 调度作业。
 *
 * Spring cache abstraction
 * Provides cache-related operations, such as the ability to update the content of the cache, but does not provide the actual data store.
 * 提供与缓存相关的操作，例如更新缓存内容的能力，但不提供实际的数据存储。
 *
 * TODO -> OPS 运维(dev+ops)
 * Spring Boot Actuator
 * Supports built in (or custom) endpoints that let you monitor and manage your application - such as application health, metrics, sessions, etc.
 * 支持内置（或自定义）端点，可让您监控和管理应用程序 - 例如应用程序运行状况、指标、会话等。
 *
 * Codecentric's Spring Boot Admin (Client)
 * Required for your application to register with a Codecentric's Spring Boot Admin Server instance.
 * 您的应用程序需要向 Codecentric 的 Spring Boot Admin Server 实例注册。
 *
 * Codecentric's Spring Boot Admin (Server)
 * A community project to manage and monitor your Spring Boot applications. Provides a UI on top of the Spring Boot Actuator endpoints.
 * 一个用于管理和监控 Spring Boot 应用程序的社区项目。 在 Spring Boot Actuator 端点之上提供 UI。
 *
 * TODO -> OBSERVE ABILITY
 * Datadog
 * Publish Micrometer metrics to Datadog, a dimensional time-series SaaS with built-in dashboarding and alerting.
 * 将 Micrometer 指标发布到 Datadog，这是一种具有内置仪表板和警报的维度时间序列 SaaS。
 *
 * Influx
 * Publish Micrometer metrics to InfluxDB, a dimensional time-series server that support real-time stream processing of data.
 * 将 Micrometer 指标发布到 InfluxDB，这是一个支持实时数据流处理的维度时间序列服务器。
 *
 * Graphite
 * Publish Micrometer metrics to Graphite, a hierarchical metrics system backed by a fixed-size database.
 * 将 Micrometer 指标发布到 Graphite，这是一个由固定大小数据库支持的分层指标系统。
 *
 * New Relic
 * Publish Micrometer metrics to New Relic, a SaaS offering with a full UI and a query language called NRQL.
 * 将 Micrometer 指标发布到 New Relic，这是一种具有完整 UI 和称为 NRQL 的查询语言的 SaaS 产品。
 *
 * Prometheus
 * Expose Micrometer metrics in Prometheus format, an in-memory dimensional time series database with a simple built-in UI, a custom query language, and math operations.
 * 以 Prometheus 格式公开 Micrometer 指标，这是一个内存维度时间序列数据库，具有简单的内置 UI、自定义查询语言和数学运算。
 *
 * Sleuth
 * Distributed tracing via logs with Spring Cloud Sleuth.
 * 使用 Spring Cloud Sleuth 通过日志进行分布式跟踪。
 *
 * Wavefront
 * Publish Micrometer metrics to Tanzu Observability by Wavefront, a SaaS-based metrics monitoring and analytics platform that lets you visualize, query, and alert over data from across your entire stack.
 * 将 Micrometer 指标发布到 Tanzu Observability by Wavefront，这是一个基于 SaaS 的指标监控和分析平台，可让您对整个堆栈中的数据进行可视化、查询和警报。
 *
 * Zipkin Client
 * Distributed tracing with an existing Zipkin installation and Spring Cloud Sleuth Zipkin.
 * 使用现有 Zipkin 安装和 Spring Cloud Sleuth Zipkin 进行分布式跟踪。
 *
 * TODO -> TESTING
 * Spring REST Docs
 * Document RESTFul services by combining hand-written with Asciidoctor and auto-generated snippets produced with Spring MVC Test.
 * 通过将手写的 Asciidoctor 和自动生成的片段与 Spring MVC 测试相结合来记录 RESTFul 服务。
 *
 * Test containers
 * Provide lightweight, throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container.
 * 提供通用数据库、Selenium Web 浏览器或任何其他可以在 Docker 容器中运行的轻量级、一次性实例。
 *
 * Contract Verifier
 * Moves TDD to the level of software architecture by enabling Consumer Driven Contract (CDC) development.
 * 通过启用消费者驱动的契约 (CDC) 开发，将 TDD 移至软件架构级别。
 *
 * Contract Stub Runner
 * Stub Runner for HTTP/Messaging based communication. Allows creating WireMock stubs from RestDocs tests.
 * 用于基于 HTTP/消息的通信的 Stub Runner。 允许从 RestDocs 测试创建 WireMock 存根。
 *
 * Embedded LDAP Server
 * Provides a platform neutral way for running a LDAP server in unit tests.
 * 提供在单元测试中运行 LDAP 服务器的平台中立方式。
 *
 * Embedded MongoDB Database
 * Provides a platform neutral way for running MongoDB in unit tests.
 * 提供在单元测试中运行 MongoDB 的平台中立方式。
 *
 * TODO -> SPRING CLOUD
 * Cloud Bootstrap
 * Non-specific Spring Cloud features, unrelated to external libraries or integrations (e.g. Bootstrap context and @RefreshScope).
 * 非特定 Spring Cloud 功能，与外部库或集成无关（例如 Bootstrap 上下文和 @RefreshScope）。
 *
 * Function
 * Promotes the implementation of business logic via functions and supports a uniform programming model across serverless providers, as well as the ability to run standalone (locally or in a PaaS).
 * 通过函数促进业务逻辑的实现，并支持跨无服务器提供商的统一编程模型，以及独立运行（本地或在 PaaS 中）的能力。
 *
 * Task
 * Allows a user to develop and run short lived microservices using Spring Cloud. Run them locally, in the cloud, and on Spring Cloud Data Flow.
 * 允许用户使用 Spring Cloud 开发和运行短期微服务。在本地、云端和 Spring Cloud Data Flow 上运行它们。
 *
 * TODO -> SPRING CLOUD TOOLS
 * Open Service Broker
 * Framework for building Spring Boot apps that implement the Open Service Broker API, which can deliver services to applications running within cloud native platforms such as Cloud Foundry, Kubernetes and OpenShift.
 * Requires Spring Boot >= 2.0.0.RELEASE and < 2.5.0-M1.
 * 用于构建实现 Open Service Broker API 的 Spring Boot 应用程序的框架，该框架可以向在 Cloud Foundry、Kubernetes 和 OpenShift 等云原生平台中运行的应用程序提供服务。
 * 需要 Spring Boot >= 2.0.0.RELEASE 和 < 2.5.0-M1。
 *
 * TODO -> SPRING CLOUD CONFIG
 * Config Client
 * Client that connects to a Spring Cloud Config Server to fetch the application's configuration.
 * 连接到 Spring Cloud Config Server 以获取应用程序配置的客户端。
 *
 * Config Server
 * Central management for configuration via Git, SVN, or HashiCorp Vault.
 * 通过 Git、SVN 或 HashiCorp Vault 对配置进行集中管理。
 *
 * Vault Configuration
 * Provides client-side support for externalized configuration in a distributed system. Using HashiCorp's Vault you have a central place to manage external secret properties for applications across all environments.
 * 为分布式系统中的外部化配置提供客户端支持。使用 HashiCorp 的 Vault，您可以在一个中心位置管理所有环境中应用程序的外部机密属性。
 *
 * Apache Zookeeper Configuration
 * Enable and configure common patterns inside your application and build large distributed systems with Apache Zookeeper based components. The provided patterns include Service Discovery and Configuration.
 * 在您的应用程序中启用和配置通用模式，并使用基于 Apache Zookeeper 的组件构建大型分布式系统。提供的模式包括服务发现和配置。
 *
 * Consul Configuration
 * Enable and configure the common patterns inside your application and build large distributed systems with Hashicorp’s Consul. The patterns provided include Service Discovery, Distributed Configuration and Control Bus.
 * 使用 Hashicorp 的 Consul 启用和配置应用程序中的通用模式并构建大型分布式系统。提供的模式包括服务发现、分布式配置和控制总线。
 *
 * TODO -> SPRING CLOUD DISCOVERY
 * Eureka Discovery Client
 * A REST based service for locating services for the purpose of load balancing and failover of middle-tier servers.
 * 一种基于 REST 的服务，用于定位服务以实现中间层服务器的负载平衡和故障转移。
 *
 * Eureka Server
 * spring-cloud-netflix Eureka Server.
 * spring-cloud-netflix 尤里卡服务器。
 *
 * Apache Zookeeper Discovery
 * Service discovery with Apache Zookeeper.
 * 使用 Apache Zookeeper 进行服务发现。
 *
 * Cloud Foundry Discovery
 * Service discovery with Cloud Foundry.
 * 使用 Cloud Foundry 进行服务发现。
 *
 * Consul Discovery
 * Service discovery with Hashicorp Consul.
 * 使用 Hashicorp Consul 进行服务发现。
 *
 * TODO -> SPRING CLOUD ROUTING
 * Gateway
 * Provides a simple, yet effective way to route to APIs and provide cross cutting concerns to them such as security, monitoring/metrics, and resiliency.
 * 提供一种简单而有效的方法来路由到 API 并为它们提供跨领域的关注点，例如安全性、监控/指标和弹性。
 *
 * OpenFeign
 * Declarative REST Client. OpenFeign creates a dynamic implementation of an interface decorated with JAX-RS or Spring MVC annotations.
 * 声明式 REST 客户端。 OpenFeign 创建了一个用 JAX-RS 或 Spring MVC 注释修饰的接口的动态实现。
 *
 * Cloud LoadBalancer
 * Client-side load-balancing with Spring Cloud LoadBalancer.
 * 使用 Spring Cloud LoadBalancer 进行客户端负载平衡。
 *
 * TODO -> SPRING CLOUD CIRCUIT BREAKER
 * Resilience4J
 * Spring Cloud Circuit breaker with Resilience4j as the underlying implementation.
 * 以 Resilience4j 作为底层实现的 Spring Cloud 断路器。
 *
 * TODO -> SPRING CLOUD MESSAGING
 * Cloud Bus
 * Links nodes of a distributed system with a lightweight message broker which can used to broadcast state changes or other management instructions (requires a binder, e.g. Apache Kafka or RabbitMQ).
 * 将分布式系统的节点与轻量级消息代理链接，该消息代理可用于广播状态更改或其他管理指令（需要绑定程序，例如 Apache Kafka 或 RabbitMQ）。
 *
 * Cloud Stream
 * Framework for building highly scalable event-driven microservices connected with shared messaging systems (requires a binder, e.g. Apache Kafka, RabbitMQ or Solace PubSub+).
 * 用于构建与共享消息系统连接的高度可扩展的事件驱动微服务的框架（需要绑定器，例如 Apache Kafka、RabbitMQ 或 Solace PubSub+）。
 *
 * TODO -> VMWARE TANZU APPLICATION SERVICE
 * Config Client (TAS)
 * Config client on VMware Tanzu Application Service.
 * Requires Spring Boot >= 2.0.0.RELEASE and < 2.5.0-M1.
 * 在 VMware Tanzu 应用服务上配置客户端。
 * 需要 Spring Boot >= 2.0.0.RELEASE 和 < 2.5.0-M1。
 *
 * Service Registry (TAS)
 * Eureka service discovery client on VMware Tanzu Application Service.
 * Requires Spring Boot >= 2.0.0.RELEASE and < 2.5.0-M1.
 * VMware Tanzu Application Service 上的 Eureka 服务发现客户端。
 * 需要 Spring Boot >= 2.0.0.RELEASE 和 < 2.5.0-M1。
 *
 * Circuit Breaker (TAS)
 * Hystrix circuit breaker client on VMware Tanzu Application Service.
 * Requires Spring Boot >= 2.0.0.RELEASE and < 2.5.0-M1.
 * VMware Tanzu Application Service 上的 Hystrix 断路器客户端。
 * 需要 Spring Boot >= 2.0.0.RELEASE 和 < 2.5.0-M1。
 *
 * TODO -> MICROSOFT AZURE
 * Azure Support
 * Auto-configuration for Azure Services (Service Bus, Storage, Active Directory, Cosmos DB, Key Vault, and more).
 * Azure 服务（服务总线、存储、Active Directory、Cosmos DB、Key Vault 等）的自动配置。
 *
 * Azure Active Directory
 * Spring Security integration with Azure Active Directory for authentication.
 * Spring Security 与 Azure Active Directory 集成以进行身份​​验证。
 *
 * Azure Key Vault
 * Manage application secrets.
 * 管理应用程序机密。
 *
 * Azure Storage
 * Azure Storage service integration.
 * Azure 存储服务集成。
 *
 * TODO -> GOOGLE CLOUD PLATFORM
 * GCP Support
 * Contains auto-configuration support for every Spring Cloud GCP integration. Most of the auto-configuration code is only enabled if other dependencies are added to the classpath.
 * 包含对每个 Spring Cloud GCP 集成的自动配置支持。大多数自动配置代码仅在其他依赖项添加到类路径时才启用。
 *
 * GCP Messaging
 * Adds the GCP Support entry and all the required dependencies so that the Google Cloud Pub/Sub integration work out of the box.
 * 添加 GCP 支持条目和所有必需的依赖项，以便 Google Cloud Pub/Sub 集成开箱即用。
 *
 * GCP Storage
 * Adds the GCP Support entry and all the required dependencies so that the Google Cloud Storage integration work out of the box.
 * 添加 GCP 支持条目和所有必需的依赖项，以便 Google Cloud Storage 集成开箱即用。
 *
 * @author Liuweiwei
 * @since 2021-01-05
 */
@SpringBootApplication
@EnableSwagger2
@EnableWebMvc
@Log4j2
public class DemoSwagger2Application extends SpringBootServletInitializer {

    /**
     * 同步日志-实现层：logback<org.slf4j>
     * private static final org.slf4j.Logger SLF4J = LoggerFactory.getLogger(DemoSwagger2Application.class);
     * 异步日志-实现层：log4j<org.apache.log4j>
     * private static final Logger LOG4J2 = LogManager.getLogger(DemoSwagger2Application.class);
     */

    /**
     * TODO -> org.springframework.boot.SpringApplication - application
     * 类，该类可用于从Java主方法引导和启动Spring应用程序。默认情况下，类将执行以下步骤来引导应用程序：
     * TODO -> org.springframework.context.ConfigurableApplicationContext - application.context
     * SPI接口将由大多数（如果不是所有）应用程序上下文实现。除了{ApplicationContext}接口中的应用程序上下文客户端方法外，还提供了配置应用程序上下文的工具。
     * TODO -> org.springframework.core.env.ConfigurableEnvironment - application.context.environment
     * SPI接口要由大多数（如果不是所有的话）{@link-Environment}类型实现的配置接口。提供用于设置活动配置文件和默认配置文件以及操作基础属性源的工具。允许客户端设置和验证所需的属性，通过{ConfigurablePropertyResolver}超级界面定制转换服务等。
     */
    private static SpringApplication application;
    private static ConfigurableApplicationContext applicationContext;
    private static ConfigurableEnvironment applicationContextEnvironment;

    public static void main(String[] args) {
        application = new SpringApplication(DemoSwagger2Application.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        applicationContext = application.run(args);
        applicationContextEnvironment = applicationContext.getEnvironment();

        log.info(applicationContextEnvironment.getProperty("java.vendor.url"));
        log.info(applicationContextEnvironment.getProperty("java.vendor.url.bug"));
        log.info(applicationContextEnvironment.getProperty("sun.java.command") + " started successfully.");
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        /**
         * Redis 默认序列化格式：JdkSerializationRedisSerializer();
         * Redis 指定JSON序列化格式：new GenericJackson2JsonRedisSerializer();, new Jackson2JsonRedisSerializer<>(Object.class);
         */
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("wei_producer_group");
        producer.setVipChannelEnabled(false);
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        return producer;
    }
}
