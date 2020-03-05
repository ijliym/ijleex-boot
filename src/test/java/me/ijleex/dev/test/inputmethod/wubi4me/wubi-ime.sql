
USE db_ime_dict;

-- ----------------------------------------------------------
TRUNCATE TABLE t_ime_dict;

-- 加载五笔词条（ https://dev.mysql.com/doc/refman/8.0/en/load-data.html ）
LOAD DATA INFILE 'D:/ProgramFiles/MySQL/mysql-8.0.19-winx64/docs/1.t_ime_dict.txt'
INTO TABLE t_ime_dict
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n'
(code,text,weight,type);

-- 设置固顶词条
-- 请执行 wubi-ime-top.sql 中的SQL语句

-- 导出五笔词条（用于 多多输入法，需要删除 TAB和- 相连的内容：VIM %s/\t-//g）
SELECT text,code,type FROM t_ime_dict ORDER BY code,weight DESC
INTO OUTFILE 'D:/ProgramFiles/MySQL/mysql-8.0.19-winx64/docs/1.t_ime_dict-dd.txt'
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n';

-- 导出五笔词条（用于 Rime输入法，需要修改或删除含 ddcmd 的行：zzkh 2017-09-06 11:06:57）
SELECT code,text,weight,stem FROM t_ime_dict ORDER BY code,weight DESC
INTO OUTFILE 'D:/ProgramFiles/MySQL/mysql-8.0.19-winx64/docs/1.t_ime_dict-rime.txt'
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n';

-- ----------------------------------------------------------
-- 谶 讖 ywwy
SELECT code,text,weight,type FROM t_ime_dict WHERE code LIKE '%wwg%' AND type IN ('##','#次') ORDER BY code,weight DESC
INTO OUTFILE 'D:/ProgramFiles/MySQL/mysql-8.0.19-winx64/docs/wb_wwg.txt'
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n';
-- ----------------------------------------------------------

TRUNCATE TABLE t_ime_dict_py;

-- 加载拼音词条
LOAD DATA INFILE 'D:/ProgramFiles/MySQL/mysql-8.0.19-winx64/docs/2.t_ime_dict_py.txt'
INTO TABLE t_ime_dict_py
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n'
(code,text,weight,type);

-- 导出拼音词条（可直接导入到多多输入法）
SELECT text,code FROM t_ime_dict_py ORDER BY code,weight DESC
INTO OUTFILE 'D:/ProgramFiles/MySQL/mysql-8.0.19-winx64/docs/2.t_ime_dict_py-dd.txt'
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n';

-- 导出拼音词条（用于 Rime输入法 2017-09-06 12:45:49）
SELECT code,text,weight FROM t_ime_dict_py ORDER BY code,weight DESC
INTO OUTFILE 'D:/ProgramFiles/MySQL/mysql-8.0.19-winx64/docs/2.t_ime_dict_py-rime.txt'
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n';

-- ----------------------------------------------------------
-- 按类型导出词条（请将第二个 TAB 替换为“#序”——用来保存原始码表）
-- 将第二个 TAB 替换为 “#序” 的命令：%s/\(\a\+\)\t/\1\#序/g （将最后一个字母后面的TAB替换为 “#序”；:h /\(\)）

-- 导出五笔词库
SELECT text,code,weight FROM t_ime_dict
WHERE type='-' OR type='-#固'
INTO OUTFILE 'D:/ProgramFiles/MySQL/mysql-8.0.19-winx64/docs/01.五笔-主码-常用字.txt'
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n';

SELECT text,code,weight FROM t_ime_dict
WHERE type='-#类1'
INTO OUTFILE 'D:/ProgramFiles/MySQL/mysql-8.0.19-winx64/docs/02.五笔-主码-词组.txt'
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n';

-- 导出拼音词库
SELECT text,code,weight FROM t_ime_dict_py
INTO OUTFILE 'D:/ProgramFiles/MySQL/mysql-8.0.19-winx64/docs/07.五笔-辅码-拼音.txt'
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n';

-- ----------------------------------------------------------
-- 在 MySQL 中，下面几个查询结果不正确（MySQL 不能区分汉语拼音和一些其他字符），Oracle 中正确：
SELECT * FROM t_ime_dict WHERE code='zzed' AND text='Е';
SELECT * FROM t_ime_dict WHERE text='a';
SELECT * FROM t_ime_dict WHERE text='E';
SELECT * FROM t_ime_dict WHERE text='㏎';
-- Oracle 中查询重复数据：
SELECT code, text, COUNT(*) FROM t_ime_dict
 GROUP BY code, text
HAVING COUNT(*) > 1;

