DROP TABLE IF EXISTS t_unicode_hanzi;
CREATE TABLE t_unicode_hanzi (
	id INT(10) NOT NULL AUTO_INCREMENT,
	han_char VARCHAR(8) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '汉字',
	code_point VARCHAR(8) NOT NULL COMMENT '汉字编码(十进制)',
	code_point_hex VARCHAR(8) NOT NULL COMMENT '汉字编码(十六进制)',
	hanyu_pinyin VARCHAR(50) COMMENT '汉语拼音',
	mandarin VARCHAR(50) COMMENT '普通话',
	total_strokes INT(3) COMMENT '总笔画数',
	frequency INT(2) COMMENT '使用频率：1-6，1为常用字，2为次一级常用字，……',
	definition VARCHAR(200) COMMENT '英文解释',
	unicode_block VARCHAR(35) COMMENT 'Unicode中所属块',
	CONSTRAINT pk_unicode_hanzi PRIMARY KEY(id),
	CONSTRAINT uk_unicode_hanzi_code UNIQUE KEY(code_point),
	CONSTRAINT uk_unicode_hanzi_hex UNIQUE KEY(code_point_hex)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Unicode Hàn (漢) Database (Unihan)';

LOAD DATA INFILE 'D:/ProgramFiles/MySQL/mysql-8.0.15-winx64/run/cjk-unified-ideographs.txt'
INTO TABLE t_unicode_hanzi
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n'
(han_char,code_point,code_point_hex,unicode_block);


DROP TABLE IF EXISTS t_unihan_variants;
CREATE TABLE t_unihan_variants (
	id INT(10) NOT NULL AUTO_INCREMENT,
	code1 VARCHAR(8) NOT NULL COMMENT '简体汉字编码(十六进制)',
	han_simplified VARCHAR(8) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '汉字',
	code2 VARCHAR(8) NOT NULL COMMENT '繁体漢字编码(十六进制)',
	han_traditional VARCHAR(8) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '漢字',
	CONSTRAINT pk_unihan_variants PRIMARY KEY(id),
	CONSTRAINT uk_unihan_variants UNIQUE KEY(code1,code2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简体字与繁体字对照表';

LOAD DATA INFILE 'D:/ProgramFiles/MySQL/mysql-8.0.15-winx64/run/s-t.txt'
INTO TABLE t_unihan_variants
FIELDS TERMINATED BY '\t' ENCLOSED BY '' ESCAPED BY '\\'
LINES  STARTING BY '' TERMINATED BY '\n'
(code1,han_simplified,code2,han_traditional);

-- 统计每个块的字数 2017-09-22 17:28
SELECT unicode_block, COUNT(1) AS char_count FROM t_unicode_hanzi GROUP BY unicode_block;

