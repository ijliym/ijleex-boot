# 加快国内 GitHub 访问速度

修改系统 hosts 文件，绕过国内 DNS 解析，直接访问 GitHub 的 CDN 节点，从而达到加速的目的。

## 1. 获取 GitHub 官方 CDN 地址

打开 [IPAddress.com](https://www.ipaddress.com/)，查询下面三个域名的 DNS 解析地址：

 * github.com
 * assets-cdn.github.com
 * github.global.ssl.fastly.net

记录查询到的IP地址。

## 2. 修改系统 hosts 文件

打开系统 hosts 文件（需管理员权限）。

路径如下：

```
%SystemRoot%\System32\drivers\etc\HOSTS     Windows
/etc/hosts                                  Linux/macOS
```

在文件的末尾添加上面三个域名与IP地址的映射并保存。（需管理员权限，注意IP地址与域名间需留有空格）

```
192.30.253.113	github.com
185.199.108.153	assets-cdn.github.com
185.199.109.153	assets-cdn.github.com
185.199.110.153	assets-cdn.github.com
185.199.111.153	assets-cdn.github.com
151.101.185.194	github.global.ssl.fastly.net
```

## 3. 刷新系统 DNS 缓存

以管理员身份运行 Windows 命令提示符或 Linux Shell，运行如下的命令手动刷新系统 DNS 缓存：

 * Windows
```bash
ipconfig /flushdns
```

 * Linux
```bash
/etc/init.d/nscd restart
```

 * macOS
```bash
sudo killall -HUP mDNSResponder
```

