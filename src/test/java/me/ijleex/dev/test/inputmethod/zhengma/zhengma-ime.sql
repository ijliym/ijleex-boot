
USE db_ime_dict;

-- ----------------------------------------------------------
TRUNCATE TABLE t_ime_dict;

-- 加载词条（https://dev.mysql.com/doc/refman/5.7/en/load-data.html）
LOAD DATA INFILE 'D:/ProgramFiles/MySQL/mysql-8.0.16-winx64/run/1.t_ime_dict-zm.txt'
INTO TABLE t_ime_dict
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n'
(code,text,weight,type);

-- 设置固顶词条
-- 请执行 zhengma-ime-top.sql 中的SQL语句

-- 导出词条（用于 多多输入法，需要删除 TAB和- 相连的内容：VIM %s/\t-//g）
SELECT text,code,type FROM t_ime_dict ORDER BY code,weight DESC
INTO OUTFILE 'D:/ProgramFiles/MySQL/mysql-8.0.16-winx64/run/1.t_ime_dict-zm-dd.txt'
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n';

-- 导出词条（用于 Rime输入法，需要修改或删除含 ddcmd 的行：zzkh 2017-09-06 11:06:57）
SELECT code,text,weight,stem FROM t_ime_dict ORDER BY code,weight DESC
INTO OUTFILE 'D:/ProgramFiles/MySQL/mysql-8.0.16-winx64/run/1.t_ime_dict-zm-rime.txt'
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n';

-- ----------------------------------------------------------

TRUNCATE TABLE t_ime_dict_py;

-- 加载拼音词条
LOAD DATA INFILE 'D:/ProgramFiles/MySQL/mysql-8.0.16-winx64/run/2.t_ime_dict_py.txt'
INTO TABLE t_ime_dict_py
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n'
(code,text,weight,type);

-- 导出拼音词条（可直接导入到多多输入法）
SELECT text,code FROM t_ime_dict_py ORDER BY code,weight DESC
INTO OUTFILE 'D:/ProgramFiles/MySQL/mysql-8.0.16-winx64/run/2.t_ime_dict_py-dd.txt'
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n';

-- 导出拼音词条（用于 Rime输入法 2017-09-06 12:45:49）
SELECT code,text,weight FROM t_ime_dict_py ORDER BY code,weight DESC
INTO OUTFILE 'D:/ProgramFiles/MySQL/mysql-8.0.16-winx64/run/2.t_ime_dict_py-rime.txt'
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n';

-- ----------------------------------------------------------

