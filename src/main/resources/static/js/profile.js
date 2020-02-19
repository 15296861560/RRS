
function apply(e) {
    var orderId=e.getAttribute("data-id");
    window.location.href="/profile/apply/"+orderId;
}

function settle(e) {
    $.ajax({
        type: "POST",
        url: "/Settle",
        contentType: 'application/json',
        data: JSON.stringify({//将json对象转换成字符串
            "seatId": 2,
            "gmtOrder": 1580553242827,
            "action": "order"
        }),
        success: function (response) {
            if (response.code == 200) {//预约成功
                window.location.href="/profile/basket";
                Swal.fire({
                    icon: 'success',
                    title: '结算成功！',
                    text: '祝您用餐愉快☺',
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: '结算失败！',
                    text: '请重新尝试！',
                });
            }
        }
    });
}

