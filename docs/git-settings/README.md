# Git 配置

## Git 客户端配置
.gitconfig 是 Git 的配置文件，.minttyrc 是 Git 客户端的配置文件，在用户根目录下。

.gitconfig 中 user.name 是 Git 提交后，显示的提交人名称（commit-author）。

```
.gitconfig user.name=liym@com       公司
.gitconfig user.name=liym@home      家里
```

user.email 是注册Git账号时使用的Email，请填写正确，否则Git提交日志中会出现重复的提交人。


## Git SSH 密钥配置

```
ssh/config 是 SSH 密钥配置文件，路径为 ~/.ssh/config。
```

### 生成密钥

```bash
ssh-keygen -t rsa -b 4096 -C "liym@git-key" -f ~/.ssh/id_rsa
```

### 将密钥添加到ssh-agent的高速缓存中

```bash
ssh-agent bash --login -i
ssh-add ~/.ssh/id_rsa

ssh-add -l
ssh-add -D
```

### 将 ~/.ssh/id_rsa.pub 的内容添加到 `Git 服务器`
 - https://help.github.com/articles/adding-a-new-ssh-key-to-your-github-account/
 - https://gitee.com/help/articles/4181

### 测试

```bash
ssh -T git@github.com
ssh -T git@gitee.com
ssh -T git@git.oschina.net
```

---

更多帮助，请参考：

 - https://gitee.com/help/categories/38
 - https://my.oschina.net/stefanzhlg/blog/529403
 - http://blog.csdn.net/windzhu0514/article/details/54140084

---

解决 MSYS2 下的中文乱码问题：http://kc123kc.github.io/2015/12/24/How-To-Solve-Gibberish-Problem-Under-MSYS2/


