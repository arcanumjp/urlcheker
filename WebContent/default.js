function onOff(){

	var strUA = navigator.userAgent.toLowerCase();
 	var checkedval = null;
 	if(strUA.indexOf("firefox") != -1){
 	
 		// how to get value in Fire Fox?? Fackin browser odd other 
    	checkedval = document.getElementById("checkfile").checked
    }
    else{
    	checkedval = document.getElementById("checkfile").checked
	}    



	if(checkedval){
		document.getElementById("exceltemplate").disabled=false;
		document.getElementById("propertyfile").disabled=false;
	}
	else{
		document.getElementById("exceltemplate").disabled=true;
		document.getElementById("propertyfile").disabled=true;
	}

}