var util = util || function() {

    return {
        getRankByName: function(name) {
        	$.ajax({
        		  url: "api/v1/movie_rank",
        		  cache: false
        		})
        		  .done(function( html ) {
        		    $( "#results" ).html( html );
        		  });
            return;
        }
    }
}();