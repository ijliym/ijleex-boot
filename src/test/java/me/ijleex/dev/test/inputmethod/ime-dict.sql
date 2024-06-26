DROP DATABASE IF EXISTS db_ime_dict;
CREATE DATABASE db_ime_dict DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;

USE db_ime_dict;

SET names utf8mb4;

SHOW VARIABLES WHERE Variable_name LIKE 'character_set_%' OR Variable_name LIKE 'collation%';

-- 输入法 形码词库
DROP TABLE IF EXISTS t_ime_dict;
-- TRUNCATE TABLE t_ime_dict;
CREATE TABLE t_ime_dict (
  id     BIGINT(10) unsigned     NOT NULL AUTO_INCREMENT,
  code   VARCHAR(32)             NOT NULL COMMENT '编码',
  text   VARCHAR(200)            NOT NULL COMMENT '词条',
  weight BIGINT(10)  DEFAULT 0   NOT NULL COMMENT '词频，用于排序（数字越大候选列表位置越靠前）',
  type   VARCHAR(10) DEFAULT '-' NOT NULL COMMENT '类型',
  stem   VARCHAR(4)  DEFAULT '-' COMMENT '构词码（用于Rime输入法）',
  CONSTRAINT pk_ime_dict PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT ='输入法词库 形码部分';

CREATE UNIQUE INDEX idx_ime_dict ON t_ime_dict (code, text);

-- 查看 MySQL 的 warnings 语句
SHOW WARNINGS;

-- 输入法 拼音词库
DROP TABLE IF EXISTS t_ime_dict_py;
/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

-- TRUNCATE TABLE t_ime_dict_py;
CREATE TABLE t_ime_dict_py (
  id     BIGINT(10) unsigned NOT NULL AUTO_INCREMENT,
  code   VARCHAR(32)  NOT NULL COMMENT '编码',
  text   VARCHAR(200) NOT NULL COMMENT '词条',
  weight BIGINT(10)          NOT NULL DEFAULT 0 COMMENT '词频，用于排序（数字越大候选列表位置越靠前）',
  type   VARCHAR(10)  NOT NULL COMMENT '类型',
  CONSTRAINT pk_ime_dict_py PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT ='输入法词库 拼音部分';

CREATE UNIQUE INDEX idx_ime_dict_py ON t_ime_dict_py (code, text);

-- ----------------------------------------------------------
-- 统计每个编码的词条数（数据好多，不要在命令行中执行）
SELECT COUNT(*)
FROM t_ime_dict_py
GROUP BY code INTO OUTFILE 'D:/ProgramFiles/MySQL/mysql-8.0.22-winx64/docs/count-out.txt'
  FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
  LINES STARTING BY '' TERMINATED BY '\n';

SELECT code, COUNT(*) AS count0
FROM t_ime_dict_py
GROUP BY code
HAVING COUNT(*) > 400;
-- -----------------------------------------------------------

-- 统计每个字母的编码、词条总数（MySQL SUBSTR(str,pos,len) pos从1开始的）
SELECT SUBSTR(code, 1, 1) AS letter,
       COUNT(*)           AS count
FROM t_ime_dict
GROUP BY letter
ORDER BY letter INTO OUTFILE 'D:/ProgramFiles/MySQL/mysql-8.0.22-winx64/docs/dict-out.txt'
  FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
  LINES STARTING BY '' TERMINATED BY '\n';

-- 按字母、类型统计词条数 2018-05-02 17:58:22
SELECT SUBSTR(code, 1, 1) AS         letter,
       COUNT(text)        AS         词条数,
       SUM(IF(type = '-', 1, 0))     常用字,
       SUM(IF(type = '-#类1', 1, 0)) 词组,
       SUM(IF(type = '-#类2', 1, 0)) 快捷符号,
       SUM(IF(type = '-#类3', 1, 0)) 命令直通车,
       SUM(IF(type = '-#次', 1, 0))  生僻字,
       SUM(IF(type = '-#用', 1, 0))  用户词,
       SUM(IF(type = '#辅', 1, 0))   辅码
FROM t_ime_dict
GROUP BY letter
ORDER BY letter;
