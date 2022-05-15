$(document).ready(function() {

	$("#alertSuccess").hide();

	$("#alertError").hide();
});

//SAVE ============================================
$(document).on("click", "#btnSave", function(event) {
	// Clear alerts---------------------
	$("#alertSuccess").text("");
	$("#alertSuccess").hide();
	$("#alertError").text("");
	$("#alertError").hide();
	// Form validation-------------------
	var status = validateItemForm();
	if (status != true) {
		$("#alertError").text(status);
		$("#alertError").show();
		return;
	}
	// If valid------------------------
	var type = ($("#hidItemIDSave").val() == "") ? "POST" : "PUT";
	$.ajax({
		url : "CustomerAPI",
		type : type,
		data : $("#formItem").serialize(),
		dataType : "text",
		complete : function(response, status) {
			onItemSaveComplete(response.responseText, status);
		}
	});
});

function onItemSaveComplete(response, status) {
	if (status == "success") {
		var resultSet = JSON.parse(response);
		if (resultSet.status.trim() == "success") {
			$("#alertSuccess").text("Successfully saved.");
			$("#alertSuccess").show();
			$("#divItemsGrid").html(resultSet.data);
		} else if (resultSet.status.trim() == "error") {
			$("#alertError").text(resultSet.data);
			$("#alertError").show();
		}
	} else if (status == "error") {
		$("#alertError").text("Error while saving.");
		$("#alertError").show();
	} else {
		$("#alertError").text("Unknown error while saving..");
		$("#alertError").show();
	}
	$("#hidItemIDSave").val("");
	$("#formItem")[0].reset();
}

//UPDATE==========================================
$(document).on("click", ".btnUpdate", function(event) {
	$("#hidItemIDSave").val($(this).data("itemid"));
	$("#empName").val($(this).closest("tr").find('td:eq(0)').text());
	$("#empNIC").val($(this).closest("tr").find('td:eq(1)').text());
	$("#empAddress").val($(this).closest("tr").find('td:eq(2)').text());
	$("#empDOB").val($(this).closest("tr").find('td:eq(3)').text());
	$("#empContact").val($(this).closest("tr").find('td:eq(4)').text());

});

//Delete=============================================
$(document).on("click", ".btnRemove", function(event) {
	$.ajax({
		url : "CustomerAPI",
		type : "DELETE",
		data : "empID=" + $(this).data("itemid"),
		dataType : "text",
		complete : function(response, status) {
			onItemDeleteComplete(response.responseText, status);
		}
	});
});

function onItemDeleteComplete(response, status) {
	if (status == "success") {
		var resultSet = JSON.parse(response);
		if (resultSet.status.trim() == "success") {
			$("#alertSuccess").text("Successfully deleted.");
			$("#alertSuccess").show();
			$("#divItemsGrid").html(resultSet.data);
		} else if (resultSet.status.trim() == "error") {
			$("#alertError").text(resultSet.data);
			$("#alertError").show();
		}
	} else if (status == "error") {
		$("#alertError").text("Error while deleting.");
		$("#alertError").show();
	} else {
		$("#alertError").text("Unknown error while deleting..");
		$("#alertError").show();
	}
}

//CLIENTMODEL=============================================================================================================================================
function validateItemForm() {

	// CODE
	if ($("#empName").val().trim() == "") {
		return "Insert Name.";
	}
	if ($("#empNIC").val().trim() == "") {
		return "Insert NIC.";
	}
	if ($("#empAddress").val().trim() == "") {
		return "Insert Address.";
	}
	if ($("#empDOB").val().trim() == "") {
		return "Insert Date of birth.";
	}
	if ($("#empContact").val().trim() == "") {
		return "Insert Contact No.";
	}

	return true;
}