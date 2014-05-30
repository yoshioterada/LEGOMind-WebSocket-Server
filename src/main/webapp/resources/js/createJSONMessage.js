/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//["machineType":"LEGO", "order":"ORDER_NUM","message":"MESSAGE"]
function createJSONMessage(orderNUM, msg) {
    var jsonObj = {};
    jsonObj.machineType = "BROWSER";
    jsonObj.order = orderNUM;
    jsonObj.message = msg;

    var jsonString = window.JSON.stringify(jsonObj);

    return jsonString;
}