# 生成GPG密钥对

## 生成密钥对

```bash
gpg --full-generate-key
```

## 导出公钥

```bash
# 查看密钥ID
gpg --list-keys
gpg --list-keys --with-colons
gpg --list-secret-keys --keyid-format=long

# 导出公钥
gpg --armor --export <ID>
```

