
// 校验是否按照要求输入数据
function checkout() {

    // Swal.fire({
    //     title: 'Submit your Github username',
    //     input: 'text',
    //     inputAttributes: {
    //         autocapitalize: 'off'
    //     },
    //     showCancelButton: true,
    //     confirmButtonText: 'Look up',
    //     showLoaderOnConfirm: true
    // });

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

    var code=check();
    console.log(code);

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
    var phone=document.getElementById("phone").value;
    var send=document.getElementById("send");
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
function check() {
    var phone=document.getElementById("phone").value;
    var verifyCode=document.getElementById("verifyCode").value;
    var code;
    $.ajax({
        type: "POST",
        url: "/register/phone/verify",
        async: false,//将ajax改为同步执行
        contentType: 'application/json',
        data: JSON.stringify({//将json对象转换成字符串
            "phone": phone,
            "verifyCode": verifyCode
        }),
        success: function (response) {
            code= response.code;
            return code;
        }
    });
    return code;
}



