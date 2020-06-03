
window.onload = function() {

    $("#userIdMessage").keyup(function(){
        update();
    });
    function update() {
        $("#userMessageIdDid").val($('#userIdMessage').val());
    }

    $("#userIdTransfer").keyup(function(){
        update();
    });
    function update() {
        $("#userTransferIdDid").val($('#userIdTransfer').val());
    }

};






