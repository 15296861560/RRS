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

##其它脚本
mvn -Dmybatis.generator.overwrite = true mybatis-generator：generate

