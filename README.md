# 基于 Spring-Boot 2.x 框架的模板项目

 - 作为 IntelliJ IDEA 开发的根项目（相当于 Eclipse 的 Workspace）
 - 基于 Spring-Boot 2.x 框架 [Spring Boot Reference Guide](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
 - 相关技术：Spring-Boot、MyBatis、jQuery、Bootstrap、Vue.js、ElementUI 等

                                        2020-07-04 21:08:17


## 使用 Git 进行项目版本管理

更多帮助，请参考：[Git Book](https://git-scm.com/book/zh/v2)。

### 初始化提交

```shell
git init
git add .
git commit -m 'Initial commit'
```

### 推送到远程仓库

```shell
git remote add origin https://github.com/ijliym/ijleex-boot.git
git branch -M main
git push -u origin main

git fetch origin
git remote show origin
```
                                        2018-04-16 17:31:26

### 回滚更改

使用命令：`git checkout -- filename`

```shell
git -c core.quotepath=false -c log.showSignature=false checkout HEAD --\
 .idea/copyright/apache_v2_license.xml\
 .idea/copyright/company_lic.xml\
 .idea/copyright/default_lic.xml\
 .idea/copyright/mit_license.xml\
 .idea/copyright/profiles_settings.xml\
 .idea/scopes/com_honeybees.xml\
 .idea/scopes/company.xml\
 .idea/scopes/html_files.xml\
 .idea/scopes/js_css_files.xml\
 .idea/scopes/me_ijleex.xml\
 .idea/scopes/mybatis.xml\
 .idea/scopes/org_apache.xml\
 .idea/scopes/org_springframework.xml
```
                                        2018-04-19 09:25:32

## License

Copyright &copy; ijym-lee. All rights reserved.

Licensed under the [Apache License, Version 2.0](LICENSE).
