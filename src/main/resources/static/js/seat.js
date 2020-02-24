function toOrderSeat(e) {
    var seatId=e.getAttribute("data-id");
    var orderTime=$("#datetime").val();
debugger;
    $.ajax({
        type: "POST",
        url: "/toOrderSeat",
        contentType: 'application/json',
        data: JSON.stringify({//将json对象转换成字符串
            "seatId": seatId,
            "orderTime":orderTime,
            "action": "order"
        }),
        success: function (response) {
            if (response.code == 200) {//预约成功

                Swal.fire({
                    icon: 'success',
                    title: '编号为'+seatId+'的位置预约成功！',
                    text:'请于30分钟内点好菜品',
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: '预约出错！',
                    text: '请检查是否已预约！',
                });
            }
        }
    });
}