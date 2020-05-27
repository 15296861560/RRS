## SpringBoot

##git创建新的存储库
 ##步骤
 - git init
 - git add README.md
 - git commit -m "first commit"
 - git remote add origin 库地址
 - git push -u origin master

##部署
 - Git
 - JDK
 - Maven
 - MySQL
 ##步骤
 - yum update
 - yum install git
 - mkdir App
 - cd App
 - git clone https://github.com/15296861560/RRS.git
 - yum install maven
 - mvn -v
 - mvn compile package
 - more src/main/resources/application.properties
 - cp src/main/resources/application.properties src/main/resources/application-production.properties
 - vim src/main/resources/application-production.properties
 - mvn package
 - java -jar -Dspring.profile.active=production target/rrs-0.0.1-SNAPSHOT.jar
 - ps -aux | grep-java
 - git pull
 使项目一直运行
 -yum install screen
 - screen -S  rrs
 -java -jar -Dspring.profile.active=production target/rrs-0.0.1-SNAPSHOT.jar
 - CTRL+C终止运行

## 资料
[Spring 文档](https://spring.io/guides)

[Spring 外部文档](https://spring.io/guides/gs/serving-web-content/)

[GitHub deploy key文档](https://developer.github.com/v3/guides/managing-deploy-keys/#deploy-keys)

[BootStrap文档](https://v3.bootcss.com/getting-started/)

[菜鸟教程](https://www.runoob.com/)

[Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html)

[SweetAlert](https://sweetalert2.github.io/#examples)

[Layui](https://www.layui.com/)


##工具
[Git](https://git-scm.com/download)

[Visual Paradigm](https://www.visual-paradigm.com/cn/)

[lombok](https://projectlombok.org/)

##数据库脚本
DROP TABLE IF EXISTS `admin_table`;
CREATE TABLE `admin_table` (
  `admin_id` varchar(11) NOT NULL,
  `admin_name` varchar(255) DEFAULT NULL,
  `level` int(2) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` char(20) DEFAULT NULL,
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `advise_table`;
CREATE TABLE `advise_table` (
  `advise_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) DEFAULT NULL,
  `description` longtext,
  `gmt_create` bigint(20) DEFAULT NULL,
  `advice_type` char(4) DEFAULT NULL,
  `creator` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`advise_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `basket_detail_table`;
CREATE TABLE `basket_detail_table` (
  `basket_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `basket_id` bigint(20) NOT NULL,
  `qty` int(11) DEFAULT NULL,
  `food_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`basket_detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=192 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `basket_table`;
CREATE TABLE `basket_table` (
  `basket_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` bigint(20) DEFAULT NULL,
  `gmt_modified` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `basket_status` char(10) DEFAULT NULL,
  `payment` double(255,0) DEFAULT '0',
  PRIMARY KEY (`basket_id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `menu_table`;
CREATE TABLE `menu_table` (
  `food_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `food_name` varchar(255) NOT NULL,
  `price` double(10,0) DEFAULT NULL,
  `food_url` varchar(255) DEFAULT NULL,
  `type` char(10) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `gmt_create` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`food_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `order_table`;
CREATE TABLE `order_table` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `seat_id` int(11) DEFAULT NULL,
  `order_time` varchar(255) DEFAULT NULL,
  `content` longtext,
  `amount` double(255,0) DEFAULT NULL,
  `order_status` char(10) DEFAULT NULL,
  `basket_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `seat_table`;
CREATE TABLE `seat_table` (
  `seat_id` int(11) NOT NULL AUTO_INCREMENT,
  `location` varchar(255) NOT NULL,
  `seat_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`seat_id`,`location`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `user_table`;
CREATE TABLE `user_table` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) DEFAULT NULL,
  `phone` char(20) DEFAULT NULL,
  `password` char(64) DEFAULT NULL,
  `gmt_create` bigint(20) NOT NULL,
  `gmt_modified` bigint(20) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4;



##其它脚本
mvn -Dmybatis.generator.overwrite = true mybatis-generator：generate

