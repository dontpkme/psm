var util = util || function() {

    return {
        getRankByName: function(name) {
        	$.ajax({
        		  url: "api/v1/movie_rank?name="+name,
        		  cache: false,
        		  success: function(data) {
        			  var good_points, normal_points, bad_points;
        			  $.each(data, function(type, num) {
        				  switch(type) {
        				  case "good": 
        					  good_points = num;
        					  break;
        				  case "normal": 
        					  normal_points = num;
        					  break;
        				  case "bad": 
        					  bad_points = num;
        					  break;
        				  }        				  
        			  });
        			  
        			  var source = $("#rank-template").html();
        			  var template = Handlebars.compile(source);
        			  var html = template({
        				  "good_points": good_points,
        				  "normal_points": normal_points,
        				  "bad_points": bad_points
        			  });
        			  $("#rank").html(html);
        		  }
        	})
        },
        getNewMovieList: function(name) {
        	$.ajax({
        		  url: "api/v1/movie_list",
        		  cache: false,
        		  success: function(data) {
        			  var items = "";
        			  var source = $("#movie-template").html();
        			  var template = Handlebars.compile(source);
        			  $.each(data, function(index, movie) {
            			  items += template(movie);
        			  })
        			  $("#movielist").html(items);
        		  }
        	})
        }
    }
}();

var init = function() {
	var list = util.getNewMovieList();
}();