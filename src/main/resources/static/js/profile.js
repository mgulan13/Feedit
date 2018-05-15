let page = 0;
let sortField = "";
let query = "";

let articles = [];

$(document).ready(function () {
	getArticles();
	
	sortField = $("#sortState").val();
	sortDirection = "asc";
	
	$('#inputFilter').on('keyup', function() {
		let length = this.value.length;
	    if (length < 3 && query.length > 0) {
	    	query = "";
	    } else if (length >= 3) {
	    	query = this.value;
	    } else {
	    	return;
	    }
	    
	    getArticles();
	});
	
	$("#sortState").change(function() {
		sort($("#sortState").val());
	}); 
	
	$("#previous").click(function() {
		page--;
		getArticles();  
	}); 
	
	$("#next").click(function() {
		page++;
		getArticles();  
	}); 
	
	$("#deleteButton").click(function() {
		let deleteable = [];
		$.each(articles, function(index) {
			let element = articles[index];
			
			if ($(`#checkbox-${element.id}`).is(':checked')) {
				deleteable.push(element);
			}
		});
		
		if (deleteable.length == 0) {
			showWarningMessage();
			return;
		}
		
		$.ajax({
			type: "DELETE",
	        url: "/api/articles",
	        contentType: "application/json",
	        data: JSON.stringify(deleteable),
	        success: function () {
	        	getArticles();
	            showSuccessMessage(deleteable.length);
	        },
	        error: function (e) {
	        	console.log(e);
	        }
	    });
	});
});

function showWarningMessage() {
	let message='<p>Nijedan članak nije označen za brisanje.</p>' + 
	'<i class="fas fa-exclamation-circle fa-10x" style="color: orange"></i>';
	showMessage(message);
}

function showSuccessMessage(deleted) {
	let message='<p>Obrisano je ' + deleted + ' članaka.</p>' + 
	'<i class="far fa-check-circle fa-10x" style="color: yellowgreen"></i>';
	showMessage(message);
}


function showMessage(message) {
	$("#deleteModalMessage").html(message);
	$("#deleteModal").modal("toggle");
    setTimeout(function () {
        $("#deleteModal").modal("hide");
    }, 3000);
}

function getArticles() {
    $.ajax({
        url: `/api/articles/profile-search?size=10&page=${page}&sort=${sortField}&query=${query}`,
        dataType: 'json',
        success: function (data) {
        	setButtons(data);
        	
        	data = data.content;
        	articles = data;
        	if (data.length === 0) {
        		$("#articles").html("Trenutno nema članaka za prikaz.");
        		return;
        	}
        	
        	let html = "";
        	$.each(data, function(index) {
                html += articleToHTML(data[index]);
            });
        	$("#articles").html(html);
        },
        error: function (e) {
        	console.log(e);
        }
    });
}

function sort(field) {
	page = 0;
	sortField = field;
	getArticles();
}

function setButtons(data) {
	$("#previous").attr("disabled", data.first);
	$("#next").attr("disabled", data.last);
	$("#page").html("Stranica " + (page + 1) + "/" + (data.totalPages === 0 ? 1 : data.totalPages));
}

function articleToHTML(article) {
	return '<div class="row" style="margin: 10px">' + 
	'<div class="col votes-field text-center">' +
		'<h1 id="votes-'+ article.id + '">' + article.votes + '</h1>' +
	'</div>' +
	'<div class="col">' +
    	'<a target="_blank" href="' + article.link + '" style="font-size: 1.5rem; color: black; margin-top: 0.5rem">' + article.headline + '</a>' +
    	'<p style="margin-bottom: 0.5rem">' + article.author + '</p>' +
    '</div>' +
    '<div class="col-auto">' + 
    	'<div class="form-check vertically-centered">'+
    		'<input class="form-check-input" type="checkbox" id="checkbox-' + article.id + '">' +
    	'</div>' + 
   '</div>' + 
'</div>';
}
