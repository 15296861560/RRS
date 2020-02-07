
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
    debugger;



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

    return true;

}



