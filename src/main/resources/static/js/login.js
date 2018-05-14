$(document).ready(function() {
	let query = location.search.slice(1);
	if (query === "error") {
		showMessage("danger", "Korisničko ime ili lozinka su neispravni.");
	} else if (query === "logout") {
		showMessage("info", "Uspješno ste se odjavili.");
	} else {
		hideMessage();
	}	
});

function validate() {
	if ($("#username").val().length === 0 ||
			$("#password").val().length === 0) {
		
		showMessage("warning", "Lozinka i korisničko ime su obavezna polja.");
		return false;
	}
	
	return true;
}

function hideMessage() {
	$("#message").attr("hidden", true);
}

function showMessage(infoLevel, message) {
	$("#message").attr("hidden", false);
	$("#message").html(
			`<div class="alert alert-${infoLevel}">${message}</div>`
	);
}