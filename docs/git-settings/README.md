# Git 配置

## Git 客户端配置

Git 客户端的配置文件位于用户根目录下，包括 `.gitconfig`（Git 环境配置） 和 `.minttyrc`（Git 客户端 mintty 配置）两个文件。

`~/.gitconfig` 的配置命令如下：

```bash
git config --global user.name liym@com         公司
git config --global user.name liym@home        家里
git config --global user.email ijliym@163.com

git config --global core.autocrlf input
git config --global core.safecrlf warn
git config --global core.whitespace cr-at-eol
git config --global core.longpaths true
git config --global core.quotepath false
git config --global core.ignorecase false

git config --global i18n.commitencoding utf-8
git config --global push.default simple
git config --global svn.pathnameencoding utf-8
git config --global log.date iso-local
git config --global gui.encoding utf-8
```

其中，`user.name` 是 Git 提交人名称（Committer），`user.email` 是注册 Git 账号时使用的Email地址，请填写正确，否则Git提交日志中会出现重复的提交人。

更多帮助，请参考：

 - https://git-scm.com/book/zh/v2

## Git SSH 密钥配置

```
ssh/config 是 SSH 密钥配置文件，路径为 ~/.ssh/config。
```

### 生成密钥

```bash
ssh-keygen -t ed25519 -C "git-key@com"  -f ~/.ssh/id_ed25519
ssh-keygen -t ed25519 -C "git-key@home" -f ~/.ssh/id_ed25519
```

生成密钥类型为 `Ed25519` 的密钥对，Ed25519 算法不需要指定密钥长度。

请注意：可以设置密钥的密码（passphrase），密钥的密码为私钥的密码，如果设置了密码，即使私钥遗失，没有密码也无法解锁（只能暴力破解）。

`-sk` 结尾的密钥类型，如 `ecdsa-sk`、`ed25519-sk`，用于生成实现双因素验证的密钥对。

 - https://ubuntu.com/server/docs/service-openssh

### 将私钥添加到 ssh-agent 高速缓存中

```bash
eval $(ssh-agent -s)
ssh-add ~/.ssh/id_ed25519

ssh-add -l
ssh-add -D
```

如果生成密钥时，设置了密码（passphrase），请按要求输入密码。

### 将公钥（~/.ssh/id_ecdsa.pub）添加到 `Git 服务器`

```bash
clip < ~/.ssh/id_ed25519.pub
cat ~/.ssh/id_ed25519.pub | clip
```

 - https://gitlab.com/help/ssh/README

### 测试

```bash
ssh -T git@github.com
ssh -T git@gitee.com
ssh -Tvvv git@gitee.com
ssh -T git@gitlab.com
ssh -T ssh://git@192.168.0.0:8022
```

---

更多帮助，请参考：

 - https://help.github.com/articles/adding-a-new-ssh-key-to-your-github-account/
 - https://gitee.com/help/articles/4181
 - https://gitee.com/help/categories/38
 - https://my.oschina.net/stefanzhlg/blog/529403
 - http://blog.csdn.net/windzhu0514/article/details/54140084

---

### 解决 Git 客户端 mintty 的中文乱码问题

解决 Git Bash 运行 MS-Windows 命令（如 ping、ipconfig 等）时的中文乱码问题。
在 Git 安装目录下创建下面兩个文件：

 * `/usr/bin/win`

```bash
#!/bin/bash
$@ |iconv -f gbk -t utf-8
```

 * `/etc/profile.d/alias.sh`

```bash
alias ls="/bin/ls --color=tty --show-control-chars"
alias grep="/bin/grep --color"
alias ll="/bin/ls --color=tty --show-control-chars -l"

alias ping="/bin/win ping"
alias netstat="/bin/win netstat"
alias nslookup="/bin/win nslookup"
alias ipconfig="/bin/win ipconfig"
```

参考 `如何解决MSYS2下的中文乱码问题`：

 - https://kc123kc.github.io/2015/12/24/How-To-Solve-Gibberish-Problem-Under-MSYS2/

