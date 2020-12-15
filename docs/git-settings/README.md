# Git/SSH 配置

## Git 客户端配置

Git 客户端的配置文件位于用户根目录下，包括 `.gitconfig`（Git 环境配置） 和 `.minttyrc`（Git 客户端 mintty 配置）两个文件。

[~/.gitconfig](https://git-scm.com/docs/git-config/) 的配置命令如下：

```shell
git config --global user.name liym@com        公司
git config --global user.name liym@home       家里
git config --global user.email ijliym@163.com

git config --global core.ignoreCase false
git config --global core.quotePath false
git config --global core.safecrlf warn
git config --global core.autocrlf input
git config --global core.checkRoundtripEncoding UTF-8
git config --global core.whitespace cr-at-eol
git config --global core.longpaths true

git config --global gui.encoding UTF-8
git config --global i18n.commitEncoding UTF-8
git config --global i18n.logOutputEncoding UTF-8
git config --global log.date iso-local
git config --global pull.rebase true
git config --global push.default simple

git config --global alias.st status
git config --global alias.cm "commit -m"
git config --global alias.l log
git config --global alias.lg "log --graph --all --format=format:'%C(bold blue)%h%C(reset) - %C(bold green)(%ar)%C(reset) %C(black)%s%C(reset) %C(cyan)— %an%C(reset)%C(bold yellow)%d%C(reset)' --abbrev-commit --date=relative"
git config --global alias.lgp "log --pretty=oneline"
git config --global alias.ck checkout
git config --global alias.df diff
git config --global alias.br branch
git config --global alias.me "config user.email ijliym@163.com"
```

其中，`user.name` 是 Git 提交人名称（Committer），`user.email` 是注册 Git 账号时使用的Email地址，请填写正确，否则Git提交日志中会出现重复的提交人。

更多帮助，请参考：

- https://git-scm.com/book/zh/v2

## Git SSH 密钥配置

### 生成密钥

使用`ssh-keygen`命令生成密钥，`ssh-keygen`命令参数说明请见：[ssh-keygen](https://man.openbsd.org/ssh-keygen.1)。

```shell
ssh-keygen -o -t ed25519 -a 16   -f ~/.ssh/id_ed25519 -C "Ed25519-Key@$(hostname)"
ssh-keygen -o -t rsa     -b 4096 -f ~/.ssh/id_rsa     -C "RSA-Key@$(hostname)"
```

参数说明：

- `-o`：使用新格式保存密钥，可不指定，默认为`RFC 4716`；
- `-t`：指定生成密钥的类型；
- `-a`：指定密钥导出函数（Key Derivation Function，默认为`bcrypt_pbkdf`）的轮数，默认为`16`；
- `-b`：指定`RSA`（3072）、`DSA`（1024）、`ECDSA`（256）三种密钥的长度；
- `-f`：输出文件名称；
- `-C`：注释。

生成密钥类型为`Ed25519`的密钥对，`Ed25519`算法不需要指定密钥长度。

- https://cryptsus.com/blog/how-to-secure-your-ssh-server-with-public-key-elliptic-curve-ed25519-crypto.html
- https://medium.com/risan/upgrade-your-ssh-key-to-ed25519-c6e8d60d3c54

请注意：可以设置密钥的密码（passphrase），密钥的密码为私钥的密码，如果设置了密码，即使私钥遗失，没有密码也无法解锁（只能暴力破解）。

`-sk` 结尾的密钥类型，如 `ecdsa-sk`、`ed25519-sk`，用于生成实现`双因素认证`的密钥对。

- https://ubuntu.com/server/docs/service-openssh

### 将私钥添加到 ssh-agent 高速缓存中

```shell
# start the ssh-agent in the background
eval $(ssh-agent -s)
ssh-add ~/.ssh/id_ed25519

ssh-add -l
ssh-add -D
```

如果生成密钥时设置了密码（passphrase），请按要求输入密码。

### 将公钥（`id_ed25519.pub`）添加到 `Git 服务器`

```shell
clip < ~/.ssh/id_ed25519.pub
cat ~/.ssh/id_ed25519.pub | clip
```

- https://gitlab.com/help/ssh/README

### 修改配置文件

修改 SSH 客户端配置文件：[~/.ssh/config](ssh/config)。

### 测试

```shell
ssh -T git@gitee.com
ssh -T git@github.com
ssh -T git@gitlab.com
ssh -T git@com-git.me
ssh -Tv ssh://git@192.168.0.0:8022
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

* [/usr/bin/win]()

```shell
#!/bin/bash
$@ |iconv -f gbk -t utf-8
```

* [/etc/profile.d/alias.sh]()

```shell
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

---

## 配置 SSH 免密登录

可以配置 SSH 密码，实现服务器1（192.168.0.1）免密登录服务器2 (192.168.0.2)。

- 使用 `ssh-copy-id` 命令将服务器1通过 `ssh-keygen` 生成的公钥复制到服务器2：

```shell
ssh-copy-id username@hostname
ssh-copy-id root@192.168.0.2
ssh-copy-id -i ~/.ssh/id_ed25519.pub -p 22 root@192.168.0.2
```

- 在服务器1上免密登录服务器2:

 ```shell
ssh root@192.168.0.2
```

### 设置文件权限

分别为普通用户和 root 用户对密钥文件设置权限。

- 将公钥添加到 `authorized_keys` 文件：

```shell
cd ~/.ssh
cat id_ed25519.pub >> authorized_keys
```

- 设置权限 - 普通用户

```shell
cd ~/.ssh
chmod 600 authorized_keys id_ed25519 id_ed25519.pub
```

- 设置权限 - root

```shell
cd /root/.ssh
chmod 644 authorized_keys id_ed25519 id_ed25519.pub
```

