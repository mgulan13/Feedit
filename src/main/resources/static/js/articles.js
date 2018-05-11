let page = 0;
let sortField = "";
let sortDirection = "";
let query = "";

$(document).ready(function () {
	getArticles();
	
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
	
	$("#sort_votes").click(function() {
		sort("votes");
	}); 
	
	$("#sort_headline").click(function() {
		sort("headline");
	});
	
	$("#sort_author").click(function() {
		sort("author");
	}); 
	
	$("#previous").click(function() {
		page--;
		getArticles();  
	}); 
	
	$("#next").click(function() {
		page++;
		getArticles();  
	}); 
});

function getArticles() {
    $.ajax({
        url: `/api/articles/search?size=2&page=${page}&sort=${sortField},${sortDirection}&query=${query}`,
        dataType: 'json',
        success: function (data) {
        	setButtons(data);

        	data = data.content;
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
	
	if (sortField === field) {
		if (sortDirection === "asc") {
			sortDirection = "desc";
		} else {
			sortDirection = "asc";
		}
	} else {
		sortField = field;
		sortDirection = "asc";
	}
	
	getArticles();
}

function setButtons(data) {
	$("#previous").attr("disabled", data.first);
	$("#next").attr("disabled", data.last);
	$("#page").html("Page " + (page + 1) + "/" + data.totalPages);
}

function articleToHTML(article) {
	return '<div class="row">' + 
    '<div class="col">' +
      '<p style="background: lime">' + article.votes + '</p>' +
	  '</div>' +
	  '<div class="col">' +
	    '<h3>' + article.headline + '</h3>' +
		'<p>' + article.author + '</p>' +
	  '</div>' +
	  '<div class="col">' + 
	    '<a class="btn btn-success">' +
		  '<i class="far fa-thumbs-up fa-lg" style="color: white"></i>' +
		'</a>' +
		'<a class="btn btn-success">' +
		  '<i class="far fa-thumbs-down fa-lg" style="color: white"></i>' +
		'</a>' +
	  '</div>' + 
	'</div>';
}
