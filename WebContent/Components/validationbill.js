$(document).ready(function() 
{  
		$("#alertSuccess").hide();  
	    $("#alertError").hide(); 
}); 
 
 
// SAVE ============================================ 
$(document).on("click", "#btnSave", function(event) 
{  
	// Clear alerts---------------------  
	$("#alertSuccess").text("");  
	$("#alertSuccess").hide();  
	$("#alertError").text("");  
	$("#alertError").hide(); 
 
	// Form validation-------------------  
	var status = validateBillingForm();  
	if (status != true)  
	{   
		$("#alertError").text(status);   
		$("#alertError").show();   
		return;  
	} 
 
	// If valid------------------------  
	var type = ($("#hidBillingIDSave").val() == "") ? "POST" : "PUT"; 

	$.ajax( 
	{  
			url : "BillingService",   
			type : type,  
			data : $("#formBilling").serialize(),  
			dataType : "text",  
			complete : function(response, status)  
			{   
				onBillingSaveComplete(response.responseText, status);  
			} 
	}); 
}); 


function onBillingSaveComplete(response, status) 
{  
	if (status == "success")  
	{   
		var resultSet = JSON.parse(response); 

		if (resultSet.status.trim() == "success")   
		{    
			$("#alertSuccess").text("Successfully saved.");    
			$("#alertSuccess").show(); 

			$("#divBillingGrid").html(resultSet.data);   
		} else if (resultSet.status.trim() == "error")   
		{    
			$("#alertError").text(resultSet.data);    
			$("#alertError").show();   
		} 

	} else if (status == "error")  
	{   
		$("#alertError").text("Error while saving.");   
		$("#alertError").show();  
	} else  
	{   
		$("#alertError").text("Unknown error while saving..");   
		$("#alertError").show();  
	} 

	$("#hidBillingIDSave").val("");  
	$("#formBilling")[0].reset(); 
} 

 
// UPDATE========================================== 
$(document).on("click", ".btnUpdate", function(event) 
{     
	$("#hidBillingIDSave").val($(this).closest("tr").find('#hidBillingIDUpdate').val());     
	$("#bAccountNo").val($(this).closest("tr").find('td:eq(0)').text());     
	$("#bUnits").val($(this).closest("tr").find('td:eq(1)').text()); 
	$("#bUnitPrice").val($(this).closest("tr").find('td:eq(2)').text());
	$("#bAmount").val($(this).closest("tr").find('td:eq(3)').text());   
	$("#bRemark").val($(this).closest("tr").find('td:eq(4)').text()); 
}); 




//REMOVE===========================================
$(document).on("click", ".btnRemove", function(event) 
{  
	$.ajax(  
	{   
		url : "BillingService",   
		type : "DELETE",   
		data : "billID=" + $(this).data("billid"),   
		dataType : "text",   
		complete : function(response, status)   
		{    
			onBillingDeleteComplete(response.responseText, status);   
		}  
	}); 
}); 

function onBillingDeleteComplete(response, status) 
{  
	if (status == "success")  
	{   
		var resultSet = JSON.parse(response); 

		if (resultSet.status.trim() == "success")   
		{    
			
			$("#alertSuccess").text("Successfully deleted.");    
			$("#alertSuccess").show(); 
		
			$("#divBillingGrid").html(resultSet.data); 
			
		} else if (resultSet.status.trim() == "error")   
		{    
			$("#alertError").text(resultSet.data);    
			$("#alertError").show();   
		}
		

	} else if (status == "error")  
	{   
		$("#alertError").text("Error while deleting.");   
		$("#alertError").show();  
	} else  
	{   
		$("#alertError").text("Unknown error while deleting..");   
		$("#alertError").show();  
	}
}
 
// CLIENT-MODEL========================================================================= 
function validateBillingForm() 
{  
	// ACCOUNT NO-----------------------
	 var tmpAcc = $("#bAccountNo").val().trim();
	if (!$.isNumeric(tmpAcc)) 
		{
		return "Insert Account No.";
		} 
	
	// UNIT---------------------------  
	var tmpUsage = $("#bUnits").val().trim();
	if (!$.isNumeric(tmpUsage)) 
	 {
	 return "Insert Unit.";
	 }
	
	// UNITPRICE-------------------------------
	 var tmpUsage = $("#bUnitPrice").val().trim();
	if (!$.isNumeric(tmpUsage)) 
	 {
	 return "Insert Unit Price.";
	 }
	
	// AMOUNT-------------------------------
	 var tmpAmount = $("#bAmount").val().trim();
	if (!$.isNumeric(tmpAmount)) 
	 {
	 return "Insert Amount.";
	 }
	
	// REMARK---------------------------  
	if ($("#bRemark").val().trim() == "")  
	{   
		return "Insert Remark.";  
	}

	return true; 
}