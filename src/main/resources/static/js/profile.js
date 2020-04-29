
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
            "action": "order"
        }),
        success: function (response) {
            if (response.code == 200) {//预约成功
                Swal.fire({
                    icon: 'success',
                    title: '结算成功！',
                    text: '祝您用餐愉快☺',
                });
                // 3秒后自动刷新页面
                var t = setTimeout(function(){window.location.reload();},3000);
            } else {
                Swal.fire({
                    icon: 'error',
                    title: '结算失败！',
                    text: '请检查是否已经预约过位置！',
                });
            }
        }
    });
}


function deleteFood(e) {
    Swal.fire({
        title: '您确定吗?',
        text: "您即将删除该菜品!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes!'
    }).then((result) => {
        if (result.value) {

        var basketDetailId=e.getAttribute("data-id");

        // 2秒后自动刷新页面
        setTimeout(function(){window.location.href="/basket/deleteFood/?basketDetailId="+basketDetailId;},2000);

        Swal.fire(
            '已删除',
            '该菜品已移除.',
            'success'
        );





    }
})

}


