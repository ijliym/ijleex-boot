/**
 * 初始化表格
 *
 * @param tableId 表格id
 */
function initTable(tableId) {
    $('#' + tableId).bootstrapTable(); // 初始化表格
}

/**
 * 刷新表格
 *
 * 用于表格添加、删除或修改修改数据等操作后，重新加载数据。
 * 或根据查询参数，重新查询数据，并刷新表格。
 *
 * @param tableId 表格id
 * @param params 查询参数/表单数据
 */
function refreshTable(tableId, params) {
    if (params) {
        $('#' + tableId).bootstrapTable('refresh', {
            'query': params
        });
    } else {
        $('#' + tableId).bootstrapTable('refresh');
    }
}

/**
 * 删除表格一行数据
 *
 * @param uri URI
 * @param tableId 表格id，用于删除成功之后，刷新表格
 * @param row 一行数据
 */
function deleteTableRow(uri, tableId, row) {
    var confirmMessage = "您确认要删除选中的记录吗？";
    if (row === undefined) {
        // bootbox.alert('请选择你要删除的记录！');
        Alert.warn('请选择你要删除的记录！');
    } else {
        bootbox.confirm({
            // title : "请确认…",
            message: confirmMessage,
            buttons: {
                confirm: {
                    label: '确定'
                },
                cancel: {
                    label: '取消'
                }
            },
            callback: function(result) {
                if (result) {
                    $.ajax(uri, {
                        async: true,
                        cache: false,
                        data: row,
                        dataType: 'json',
                        method: 'POST'
                    }).done(function(result, textStatus, xhr) {
                        if (result.code === 0) {
                            refreshTable(tableId);
                            Alert.succ('删除成功。');
                        } else {
                            let msg = result.message;
                            if (!msg || '操作失败' === msg) {
                                Alert.error('删除表格数据失败！');
                            } else {
                                if (msg.indexOf("Exception") > -1) {
                                    msg = "删除表格数据失败：数据不能删除！";
                                }
                                Alert.error(msg);
                            }
                        }
                    }).fail(function(xhr, textStatus, errorThrown) {
                        console.error('error');
                    }).always(function(result, textStatus, xhr) {
                        console.info(`result ${result}, textStatus ${textStatus}, xhr ${xhr}`);
                    });
                } else {
                    // alert('点击取消按钮了');
                }
            }
        });
    }
}

/**
 * 一次删除表格中选中的多行（或一行）
 *
 * @param tableId 表格id
 * @param uri URI
 * @param confirmMessage 确认信息
 */
function deleteRowsById(tableId, uri, confirmMessage) {
    if (null == confirmMessage || typeof (confirmMessage) === "undefined" || "" === confirmMessage) {
        confirmMessage = "您确认删除选中的记录吗？";
    }
    var rows = $('#' + tableId).bootstrapTable('getSelections');
    var num = rows.length;
    var ids = null;
    if (num < 1) {
        bootbox.alert('请选择你要删除的记录！');
    } else {
        bootbox.confirm({
            title: "请确认…",
            message: confirmMessage,
            buttons: {
                confirm: {
                    label: '确定'
                },
                cancel: {
                    label: '取消'
                }
            },
            callback: function(result) {
                if (result) {
                    $.each(rows, function(i, row) {
                        var key;
                        var id = row.id; // id或serial
                        if (id !== undefined) {
                            key = id;
                        } else {
                            key = row.serial;
                        }
                        if (null == ids || i === 0) {
                            ids = key;
                        } else {
                            ids = ids + ',' + key;
                        }
                    });

                    $.getJSON(uri, {
                        "ids": ids
                    }, function(data) {
                        if (null != data && null != data.msg && "" !== data.message) {
                            bootbox.alert(data.message);
                            flashTable(tableId);
                        } else {
                            bootbox.alert('删除失败！');
                        }
                        // clearSelect(tableId); // 移除选中的行
                    });
                } else {
                    // alert('点击取消按钮了');
                }
            }
        });
    }
}
