var util = util || function() {

  var mSource = $("#movie-template").html();
  var mTemplate = Handlebars.compile(mSource);

  return {
    movieindex: 0,
    movielist: [],
    getArticleByIdAndRank: function(id, rank) {
      $.ajax({
        url: "api/v1/article_list?id=" + id,
        cache: false,
        async: false,
        success: function(data) {
          var subset;
          var html = "";
          if (rank == "good")
            subset = data.good;
          else if (rank == "normal")
            subset = data.normal;
          else if (rank == "bad")
            subset = data.bad;

          $.each(subset, function(num, article) {
            html += "<li><a href='" + article.link + "' target='_blank'>" + article.title + "</a></li>";
          });
          $("#article-list").html(html);
        }
      })
    },
    getRankById: function(id) {
      var good_points, normal_points, bad_points, detail;
      $.ajax({
        url: "api/v1/movie_rank?id=" + id,
        cache: false,
        // async: false,
        success: function(data) {
          $.each(data, function(type, num) {
            switch (type) {
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

          $.ajax({
            url: "api/v1/movie_detail?id=" + id,
            cache: false,
            success: function(data) {
              if (data.age == "1")
                data.age = "普通級";
              if (data.age == "2")
                data.age = "保護級";
              if (data.age == "3")
                data.age = "輔導級";
              if (data.age == "4")
                data.age = "限制級";
              detail = data;

              var source = $("#rank-template").html();
              var template = Handlebars.compile(source);
              var html = template({
                "good_points": good_points,
                "normal_points": normal_points,
                "bad_points": bad_points,
                "detail": detail
              });
              $("#rank").html(html);
            }
          })
        }
      })

    },
    getNewMovieList: function(name) {
      $.ajax({
        url: "api/v1/movie_list",
        cache: false,
        dataType: "json",
        success: function(data) {
          var items = "";
          var rightdata = data;
          var leftdata = data;
          util.movielist = data;

          $.each(rightdata, function(index, movie) {
            items += mTemplate(movie);
          })
          $("#movie-list-right").html(items);

          leftdata.reverse();
          leftdata.unshift(leftdata[leftdata.length - 1]);
          leftdata.pop();
          console.log(leftdata);
          items = "";
          $.each(leftdata, function(index, movie) {
            items += mTemplate(movie);
          })
          $("#movie-list-left").html(items);

          util.getRankById(util.movielist[0].id);
        }
      })
    },
    renderMovieBlock: function(index) {
      return mTemplate(this.movielist[index]);
    }
  }
}();

var init = function() {
  var list = util.getNewMovieList();
  var index = 0;
  var base = 150;
  var pos = base;
  var go = "";
  $(".sense-area").scroll(function(e) {
    if (!shifting) {
      if ($(".sense-area").scrollTop() > pos) {
        go = "down";
        index++;
      } else if ($(".sense-area").scrollTop() < pos) {
        go = "up";
        index--;
      }
      pos = base + index;
      $(".sense-area").scrollTop(pos);
      if (go == "down")
        right();
      if (go == "up")
        left();

      $(".sense-area").scrollTop(base);
      go = "";
    }
  });
  $(".sense-area").scrollTop(pos);
}();