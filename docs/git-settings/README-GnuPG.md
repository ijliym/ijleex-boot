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
gpg --armor --export <密钥ID>
```

## Git 配置 GPG 签名

### 设置全局签名

```bash
git config --global commit.gpgsign true
git config --global user.signingkey <密钥ID>
git config --global tag.gpgsign true
```

### 设置项目签名

```bash
git config --local commit.gpgsign true
git config --local user.signingkey <密钥ID>
git config --local tag.gpgsign true
```

