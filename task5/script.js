let userBalance = 10000;
let merchantBalance = 5000;

function makePayment(){

let amount = parseInt(document.getElementById("amount").value);
let message = document.getElementById("message");

if(isNaN(amount) || amount <= 0){
message.innerHTML = "Enter valid amount";
return;
}

let tempUser = userBalance;
let tempMerchant = merchantBalance;

try{

if(amount > userBalance){
throw "Insufficient Balance";
}

/* Deduct from user */
tempUser = tempUser - amount;

/* Simulate failure randomly */
let failure = Math.random();

if(failure < 0.3){
throw "Transaction Failed";
}

/* Add to merchant */
tempMerchant = tempMerchant + amount;

/* COMMIT */
userBalance = tempUser;
merchantBalance = tempMerchant;

document.getElementById("userBalance").innerHTML = userBalance;
document.getElementById("merchantBalance").innerHTML = merchantBalance;

message.innerHTML = "Payment Successful (COMMIT)";
message.style.color = "green";

}

catch(error){

/* ROLLBACK */
message.innerHTML = error + " (ROLLBACK)";
message.style.color = "red";

}

}