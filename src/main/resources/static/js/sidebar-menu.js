fetch('system/menu/tree/getByUser?systemId=ijleex-mgmt', {
    cache: 'no-cache',
    credentials: 'same-origin',
    mode: 'cors',
    method: 'GET'
}).then(function(rsp) {
    if (rsp.ok) {
        // https://developer.mozilla.org/en-US/docs/Web/API/Body/json
        return rsp.json();
    }
    throw new Error('Network response was not ok.');
}).then(function(data) {
    if (data.code !== 0) {
        console.error(`获取菜单失败：${data.msg}`);
    }

    const children = data.msg.children;
    let menu = '';
    for (let [i, node] of children.entries()) { // Array Iterator
        const children = node.children;
        if (children != null) { // 分支节点
            menu = menu + `<li class="nav-item dropdown"><a class="dropdown-toggle" href="javascript:void(0);"><span class="icon-holder"><i class="${node.icon}"></i></span><span class="title">${node.name}</span><span class="arrow"><i class="ti-angle-right"></i></span></a>`;
            menu = menu + `<ul class="dropdown-menu">`;
            for (let [j, leaf] of children.entries()) { // 遍历子菜单
                menu = menu + `<li><a class="sidebar-link" href="#${leaf.uri}" title="${leaf.name}">${leaf.name}</a></li>`;
            }
            menu = menu + `</ul></li>`;
        } else { // 叶子节点
            menu = menu + `<li><a class="sidebar-link"  href="#${node.uri}" title="${node.name}">${node.name}</a></li>`;
        }
    }
    $('#sidebar-menu').append(menu);
}).catch(function(e) {
    console.error(`解析菜单失败：${e}`);
});
