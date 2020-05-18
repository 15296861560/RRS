function toOrder(e) {
    // var number=document.getElementById("num").value;
    var foodId=e.getAttribute("data-id");
    var number=$("#num-"+foodId).val();
    var foodName=e.getAttribute("data-name");
    $.ajax({
        type: "POST",
        url: "/toOrder",
        contentType: 'application/json',
        data: JSON.stringify({//将json对象转换成字符串
            "foodId": foodId,
            "number": number,
            "action": "order"
        }),
        success: function (response) {
            if (response.code == 200) {//预约成功

                Swal.fire({
                    icon: 'success',
                    title: foodName+'x'+number+'加入订单成功！',
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: '出现了点小错误！',
                    text: '请重新尝试！',
                });
            }
        }
    });
}

function submitForm(){
//获取排序表单对象
    var form = document.getElementById("sortForm");
    form.submit();//form表单提交
}