localStorage.setItem("plusreadyStatus","0");
function permissioninit(){
	localStorage.setItem("plusreadyStatus","1");
	console.log("plus.navigator.getUserAgent() = "+plus.navigator.getUserAgent());
}
document.addEventListener('plusready',permissioninit);
   		