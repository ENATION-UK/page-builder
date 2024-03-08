
var accumulatedHtml = ''; // 初始化累积的HTML
var iframe ;

function receiveHtmlChar(char) {
    accumulatedHtml += char; // 将新字符追加到累积的HTML中
    if (accumulatedHtml.endsWith('>')) {
        updateIframeContent();
    }
}
// 更新iframe中的HTML内容
function updateIframeContent() {
    // 由于直接操作 DOM 可能导致重绘和性能问题，此处我们更新整个文档内容
    var doc;

    if (iframe.contentDocument) {
        doc = iframe.contentDocument; // For NS6
    } else if (iframe.contentWindow) {
        doc = iframe.contentWindow.document; // For IE5.5 and IE6
    }
    doc.open();
    doc.write(accumulatedHtml ); // 确保文档结构完整
    doc.close();
}

const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
const host = window.location.host; // 包括域名和端口（如果有）

// 构建WebSocket的URL
const wsUrl = `${protocol}//${host}/ws`;

// 创建WebSocket连接
const socket = new WebSocket(wsUrl);


// 当连接成功建立的回调函数
socket.onopen = function(event) {
    console.log('连接已开启：', event);
    // 你可以在这里发送消息给服务器
    // socket.send('你好，服务器！');
};

// 监听接收到的消息
socket.onmessage = function(event) {
    // console.log('接收到消息：', event.data);
    var message = JSON.parse(event.data);
    if (message.status === 'success') {
        receiveHtmlChar( message.body);
    }else {
        alert(message.body);
    }

};

// 监听错误事件
socket.onerror = function(event) {
    console.error('WebSocket错误：', event);
};

// 监听连接关闭事件
socket.onclose = function(event) {
    console.log('连接已关闭：', event);
};


function sendMessage(base64String) {
    const apiKey = localStorage.getItem('openaiKey');
    // 创建一个对象
    const obj = {
        base64String: base64String,
        apiKey: apiKey
    };
    const jsonString = JSON.stringify(obj);
    socket.send(jsonString);

    $(".upload-area").hide();
    $(".preview-block").show()

}


$(function (){

    iframe = document.getElementById('preview');

    // 当点击 uploadArea 时触发 fileInput 的 click 事件
    $('.upload-area').on('click', function() {
        $('#fileInput').click();
    });


    document.getElementById('fileInput').addEventListener('change', function() {
        var file = this.files[0];

        if (file) {
            let reader = new FileReader();
            reader.onload = function(event) {
                let base64String = event.target.result;
                sendMessage(base64String);
            };
            reader.readAsDataURL(file);

            var imgUrl = URL.createObjectURL(file);
            $("#imagePreview").show().attr("src", imgUrl);

        }
    });

    $("#settingSaveBtn").click(function (){
        let openaiKey = $("#openaiKey").val();
        localStorage.setItem('openaiKey', openaiKey);

    });


});