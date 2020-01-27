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

