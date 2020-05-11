
// 校验是否按照要求输入数据
function checkout() {

    var userName = $("#userName").val();
    var phone = $("#phone").val();
    var password = $("#password").val();
    var confirmPassword = $("#confirmPassword").val();
    var checkbox = $("#checkbox");

    if (userName == "")
    {
        $("#userName").focus();
        Swal.fire({
            icon: 'error',
            title: '会员名不能为空！',
            text: '请输入您的会员名！',
        });
        return false;

    };
    if (phone == "")
    {
        $("#phone").focus();
        Swal.fire({
            icon: 'error',
            title: '手机号码不能为空！',
            text: '请输入您的手机号码！',
        });
        return false;
    };
    if (password == "")
    {
        $("#password").focus();
        Swal.fire({
            icon: 'error',
            title: '您的登录密码不能为空！',
            text: '请输入您的登录密码！',
        });
        return false;
    };
    if (confirmPassword== "")
    {
        $("#confirmPassword").focus();
        Swal.fire({
            icon: 'error',
            title: '您的确认密码不能为空！',
            text: '请确认您的密码！',
        });
        return false;
    };
    if (password!= confirmPassword)
    {
        $("#confirmPassword").focus();
        Swal.fire({
            icon: 'error',
            title: '登录密码与确认密码不同！',
            text: '请检查您的确认密码！',
        });
        return false;
    };
    if (!checkbox.is(':checked'))
    {
        Swal.fire({
            icon: 'error',
            title: '请阅读并同意协议！',
        });
        return false;
    };
    var code;
    $.ajax({
        type: "POST",
        url: "/register/phone/binding",
        async: false,//将ajax改为同步执行
        contentType: 'application/json',
        data: JSON.stringify({//将json对象转换成字符串
            "phone": phone,
            "verifyCode": verifyCode
        }),
        success: function (response) {
            code= response.code;
        }
    });


    if (code!=200) {
        if (code==1002) {
            Swal.fire({
                icon: 'error',
                title: '注册失败！',
                text: '该手机号已注册，请直接登录！',
            });
        }else {
            Swal.fire({
                icon: 'error',
                title: '验证码错误！',
                text: '请检查您的验证码！',
            });
        }

        return false;
    }

    return true;

}

//发送验证码
function verify() {
    var phone=document.getElementById("phone").innerText;
    if (phone=="")phone=document.getElementById("phone").value;
    var send=document.getElementById("send");
    debugger
    $.ajax({
        type: "POST",
        url: "/register/phone",
        contentType: 'application/json',
        data: JSON.stringify({//将json对象转换成字符串
            "phone": phone,
            "action": "verify"
        }),
        success: function (response) {
            if (response.code == 200) {//发送验证码成功
                send.innerText = "验证码发送成功请进行验证";
                send.classList.add("btn-success");
            } else {
                send.innerText = "验证码发送失败请重新尝试";
                send.classList.add("btn-danger");
            }
        }
    });
}

//检查验证码
function check(){
    var phone=document.getElementById("phone").innerText;
    if (phone=="")phone=document.getElementById("phone").value;
    var verifyCode=document.getElementById("verifyCode").value;
    var code;
    $.ajax({
        type: "POST",
        url: "/register/phone/binding",
        async: false,//将ajax改为同步执行
        contentType: 'application/json',
        data: JSON.stringify({//将json对象转换成字符串
            "phone": phone,
            "verifyCode": verifyCode
        }),
        success: function (response) {
            code= response.code;
        }
    });


    if (code==200) {
        Swal.fire({
            icon: 'success',
            title: '绑定成功！',
            text: '即将跳转回个人中心页面！',
        });
        // 3秒后跳转到修改个人中心页面
        var t = setTimeout(function(){window.location.href="/profile";},3000);
    }else if (code==1002){
        Swal.fire({
            icon: 'error',
            title: '请检查您的手机号码！',
            text: '该手机号码已经被其它账号绑定过了！',
        });
    } else {

        Swal.fire({
            icon: 'error',
            title: '请检查您的验证码！',
            text: '验证码错误！',
        });
    }
}

//校验验证码
function confirmPhone() {
    var phone=document.getElementById("phone").innerText;
    if (phone=="")phone=document.getElementById("phone").value;
    var verifyCode=document.getElementById("verifyCode").value;
    var nextUrl=document.getElementById("nextUrl").value;
    var code;
    $.ajax({
        type: "POST",
        url: "/profile/phone/verify",
        async: false,//将ajax改为同步执行
        contentType: 'application/json',
        data: JSON.stringify({//将json对象转换成字符串
            "phone": phone,
            "verifyCode": verifyCode
        }),
        success: function (response) {
            code= response.code;
        }
    });
    if (code!=200) {
        Swal.fire({
            icon: 'error',
            title: '验证失败！',
            text: '请检查您的验证码！',
        });
    }else {
        Swal.fire({
            icon: 'success',
            title: '验证成功！',
            text: '即将跳转到修改页面！',
        });
        // 3秒后跳转到修改页面
        var t = setTimeout(function(){window.location.href=nextUrl+"/?phone="+phone;},3000);
    }

}

//修改密码
function changePassword(){
    var password=document.getElementById("password").value;
    var confirmPassword=document.getElementById("confirmPassword").value;
    var phone=document.getElementById("phone").value;
    var code;

    if(password==confirmPassword){//如果密码与确认密码相同
        $.ajax({
            type: "POST",
            url: "/profile/changePassword",
            async: false,//将ajax改为同步执行
            contentType: 'application/json',
            data: JSON.stringify({//将json对象转换成字符串
                "password": password,
                "phone": phone
            }),
            success: function (response) {
                code= response.code;
            }
        });

        if (code==200) {
            Swal.fire({
                icon: 'success',
                title: '修改密码成功！',
                text: '即将跳转回个人中心页面！',
            });
            // 3秒后跳转到修改个人中心页面
            var t = setTimeout(function(){window.location.href="/profile";},3000);
        }else {

            Swal.fire({
                icon: 'error',
                title: '修改密码失败！',
                text: '请重新尝试！',
            });
        }
    }else {
        Swal.fire({
            icon: 'error',
            title: '修改密码失败！',
            text: '新的登录密码与确认密码不相同！',
        });
    }


}

//修改管理员密码
function adminChangePassword(){
    var password=document.getElementById("password").value;
    var confirmPassword=document.getElementById("confirmPassword").value;
    // var phone=document.getElementById("phone").value;
    var code;

    if(password==confirmPassword){//如果密码与确认密码相同
        $.ajax({
            type: "POST",
            url: "/admin/changePassword",
            async: false,//将ajax改为同步执行
            contentType: 'application/json',
            data: JSON.stringify({//将json对象转换成字符串
                "password": password
            }),
            success: function (response) {
                code= response.code;
            }
        });

        if (code==200) {
            Swal.fire({
                icon: 'success',
                title: '修改密码成功！',
                text: '即将跳转回管理页面！',
            });
            // 3秒后跳转到修改个人中心页面
            var t = setTimeout(function(){window.location.href="/manage";},3000);
        }else {

            Swal.fire({
                icon: 'error',
                title: '修改密码失败！',
                text: '请重新尝试！',
            });
        }
    }else {
        Swal.fire({
            icon: 'error',
            title: '修改密码失败！',
            text: '新的登录密码与确认密码不相同！',
        });
    }


}





