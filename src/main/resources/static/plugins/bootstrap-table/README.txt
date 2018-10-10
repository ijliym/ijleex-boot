我对 bootstrap-table 的修改


分页参数：offset、limit
查询参数：其他参数，与分页参数一起传送到后台

1. 保存分页参数，用于点击上/下页时使用
关键代码

BootstrapTable.DEFAULTS = {..}
添加：
cacheQueryParams: undefined, // XXX 保存查询参数 2015-11-28 21:26

BootstrapTable.prototype.initServer == function (silent, query, url) {..}
添加/修改：
// XXX 输入查询参数时，保存查询参数；否则，使用原有的查询参数 2015-11-28 21:29
// XXX 分页时，点击上/下页，只传分页参数，没有查询参数，如果不保存查询参数，会查询所有数据。
if(query !== undefined) {
	this.options.cacheQueryParams = query;
}
// $.extend(data, query || {});
$.extend(data, query || this.options.cacheQueryParams); // XXX 使用已保存的查询参数


请查找 cacheQueryParams，查看相关代码。

--------------------------------------------------------------------------------
这不是个问题，因为 bootstrap-table 提供了一个方法 queryParams 来设置查询参数。
data = calculateObjectValue(this.options, this.options.queryParams, [params], data);
示例：
		...
		queryParams: function(params) {
			params.name = 'name';
			params.code = 'code';
			return params;
		},
		...
                                                        liym
                                                        2018-04-12 14:32:45
--------------------------------------------------------------------------------

2.当前有分页参数，但是输入新的查询参数时，应该重置分页参数。
比如，输入某个查询参数，查询结果有三页数据，点击了第二页，则分页参数为
offset=10&limit=10
如果此时输入新的查询参数，则应该将offset与limit的值重置，如下
offset=0&limit=10

关键代码
BootstrapTable.prototype.refresh = function (params) {..}
添加：
params.pageNumber = 1; // XXX 刷新表格时，将表格页码重设为 1（不判断） 2015-11-29 14:49


                                                        liym
                                                        2017-03-03 10:14:01


