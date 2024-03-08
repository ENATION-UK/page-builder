
var accumulatedHtml = ''; // 初始化累积的HTML
var iframe ;

function addContent(text) {
    var $div = $('.output');
    $div.text($div.text()+text);
    $div.scrollLeft($div.prop('scrollWidth'));
}
function downloadHtml() {
// 假设你的HTML内容存储在pageHtml变量中


// 创建一个新的blob对象
    var blob = new Blob([accumulatedHtml], {type: "text/html;charset=utf-8"});

// 创建一个新的a元素
    var a = document.createElement("a");

// 使用URL.createObjectURL()方法创建一个指向blob的URL
    a.href = URL.createObjectURL(blob);

// 设置下载的文件名
    a.download = "page.html";

// 添加a元素到文档
    document.body.appendChild(a);

// 模拟点击a元素
    a.click();

// 移除a元素
    document.body.removeChild(a);

}
function receiveHtmlChar(char) {
    accumulatedHtml += char; // 将新字符追加到累积的HTML中
    addContent(char)
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
};

// 监听接收到的消息
socket.onmessage = function(event) {
    // console.log('接收到消息：', event);
    const message = JSON.parse(event.data);
    if (message.type === 'initCodeTypes') {
        handleInitCodeTypes(message)
        return
    }
    if (message.status === 'success') {
        $(".output").show();
        receiveHtmlChar( message.body);
    }
    if (message.status === 'complete') {
        $(".menu").show();
        $('.output').hide();
    }

    if (message.status === 'error') {
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

function handleInitCodeTypes(message) {
    console.log('handleInitCodeTypes:', message)
    const selectEle = document.querySelector('select.code-type')
    const codeTypes = message.data
    if (codeTypes && Object.keys(codeTypes).length > 0) {
        selectEle.innerHTML = ''
        for (const key of Object.keys(codeTypes)) {
            const opt = document.createElement('option')
            opt.value = key
            opt.innerHTML = codeTypes[key]
            selectEle.appendChild(opt);
        }
    }
}

function sendMessage(base64String) {
    const codeType = document.querySelector('select.code-type').value
    if (!codeType) {
        console.log('require code type')
        alert('Please select the output code type')
        return
    }
    const apiKey = localStorage.getItem('openaiKey');
    if (!apiKey) {
        console.log('require OpenAI APIKey')
        alert('Please set the OpenAI APIKey')
        return
    }
    // 创建一个对象
    const obj = {
        base64String: base64String,
        apiKey: apiKey,
        codeType: codeType
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


    const myModalEl = document.getElementById('settingModal')
    myModalEl.addEventListener('show.bs.modal', event => {
        let openaiKey = localStorage.getItem('openaiKey');
        $("#openaiKey").val(openaiKey);
    })


    $("#downloadBtn").click(function (){
        downloadHtml();
    });


    $("#resetBtn").click(function (){
        $(".menu").hide();
        $('.output').hide();

        $(".upload-area").show();
        $(".preview-block").hide()
        $("#imagePreview").hide().attr("src","")
        iframe.src = iframe.src;
    });





});