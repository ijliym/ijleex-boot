/**
 * 下载文件
 * @param url 下载文件地址
 * @param params 下载文件参数
 * @returns {Promise<void>}
 */
async function download(url, params) {
    const request = {
        body: JSON.stringify(params),
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        },
        method: 'POST'
    }

    try {
        const response = await fetch(url, request)
        if (response.ok) {
            const fileName = response.headers.get('Content-Disposition')
                    .split(';')[1].split('=')[1]
            const blob = await response.blob()

            const link = document.createElement('a')
            link.style.display = 'none'
            link.download = decodeURIComponent(fileName)
            link.href = URL.createObjectURL(blob)
            document.body.appendChild(link)
            link.click()
            URL.revokeObjectURL(link.href)
            document.body.removeChild(link)
        } else {
            console.error('Error: ', response.status, response.statusText);
        }
    } catch (error) {
        console.error('Error: ', error.message);
    }
}

const url = "";
const params = {}

download(url, params).then(r => {
})

