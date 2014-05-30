
function updateScreen(messages) {

    var json = $.parseJSON(messages);
    var machineType = json.machineType;
    var order = json.order;
    var message = json.message;


    if (order === 'BRWS_INFO') {
        var dv = document.getElementById("show-message").textContent = message;
    }
    if (order === 'BRWS_WARN') {
        var dv = document.getElementById("show-warn-message").textContent = message;
    }
}     