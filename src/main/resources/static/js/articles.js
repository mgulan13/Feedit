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
	    
	    page=0;
	    getArticles();
	});
	
	$("#sortVotes").click(function() {
		sort("votes");
	}); 
	
	$("#sortHeadline").click(function() {
		sort("headline");
	});
	
	$("#sortAuthor").click(function() {
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
        url: `/api/articles/search?size=10&page=${page}&sort=${sortField},${sortDirection}&query=${query}`,
        dataType: 'json',
        success: function (data) {
        	setButtons(data);

        	data = data.content;
        	if (data.length === 0) {
        		$("#articles").html('<div class="text-center">Trenutno nema članaka za prikaz.</div>');
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
	$("#page").html("Stranica " + (page + 1) + "/" + (data.totalPages === 0 ? 1 : data.totalPages));
}

function articleToHTML(article) {
	return '<div class="row" style="margin: 10px">' + 
    	'<div class="col votes-field text-center">' +
    		'<h1>' + article.votes + '</h1>' +
    	'</div>' +
    	'<div class="col">' +
	    	'<a target="_blank" href="' + article.link + '" style="font-size: 1.5rem; color: black; margin-top: 0.5rem">' + article.headline + '</a>' +
	    	'<p style="margin-bottom: 0.5rem">' + article.author + '</p>' +
	    '</div>' +
	    '<div class="col-auto">' + 
	      	'<button type="button" class="btn btn-info vertically-centered" id="upvote-' + article.id + '">' +
	      		'<i class="far fa-thumbs-up fa-lg"></i>' +
	      	'</button>\n' +
	      	'<button type="button" class="btn btn-info vertically-centered" id="downvote-' + article.id + '">' +
	      		'<i class="far fa-thumbs-down fa-lg"></i>' +
	      	'</button>' +
	   '</div>' + 
	'</div>';
}
