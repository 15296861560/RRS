function delete_user(e) {
    var userId=e.getAttribute("data-id");
    window.location.href="/manageUser/delete/"+userId;
}

function agree(e) {
    var orderId=e.getAttribute("data-id");
    window.location.href="/sendback/"+"agree/"+orderId;
}
function reject(e) {
    var orderId=e.getAttribute("data-id");
    window.location.href="/sendback/"+"reject/"+orderId;
}

function agreeBorrow(e) {
    var orderId=e.getAttribute("data-id");
    window.location.href="/manage/"+"agree/"+orderId;
}
function rejectBorrow(e) {
    var orderId=e.getAttribute("data-id");
    window.location.href="/manage/"+"reject/"+orderId;
}

//添加新管理员
function addAdmin() {
    var adminId=document.getElementById("admin_id").value;
    var adminName=document.getElementById("admin_name").value;
    var phone=document.getElementById("phone").value;
    $.ajax({
        type: "POST",
        url: "/manage/addAdmin",
        async: false,//将ajax改为同步执行
        contentType: 'application/json',
        data: JSON.stringify({//将json对象转换成字符串
            "adminId": adminId,
            "adminName": adminName,
            "phone": phone
        }),
        success: function (response) {
            if (response.code == 200) {//添加新管理员成功
                Swal.fire({
                    icon: 'success',
                    title: '添加新管理员成功！',
                    text: '2秒后将自动重新加载页面页面！',
                });
                // 2秒后重新加载页面页面
                var t = setTimeout(function(){window.location.href='/manage/admin';},2000);
            } else {//添加新管理员失败
                Swal.fire({
                    icon: 'error',
                    title: '添加新管理员失败！',
                    text: '请检查是否输入有误！',
                });
                // 2秒后重新加载页面页面
                var t = setTimeout(function(){window.location.href='/manage/admin';},2000);
            }
        }
    });
}

