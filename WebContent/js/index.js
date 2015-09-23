var util = util || function() {

  var mSource = $("#movie-template").html();
  var mTemplate = Handlebars.compile(mSource);
  var details = {};
  var articles = {};
  var nowGood, nowNormal, nowBad;
  var good_points, normal_points, bad_points;

  function renderMovieDetail(id) {
    $.each(eval("details.m" + id + ".rank"), function(type, num) {
      switch (type) {
        case "good":
          good_points = num;
          break;
        case "normal":
          normal_points = num;
          break;
        case "bad":
          bad_points = num * -1;
          break;
      }
    });

    if (eval("details.m" + id + ".detail").age == "1")
      eval("details.m" + id + ".detail").age = "普通級";
    if (eval("details.m" + id + ".detail").age == "2")
      eval("details.m" + id + ".detail").age = "保護級";
    if (eval("details.m" + id + ".detail").age == "3")
      eval("details.m" + id + ".detail").age = "輔導級";
    if (eval("details.m" + id + ".detail").age == "4")
      eval("details.m" + id + ".detail").age = "限制級";
    var detail = eval("details.m" + id + ".detail");

    var source = $("#rank-template").html();
    var template = Handlebars.compile(source);
    var html = template({
      // "good_points": good_points,
      // "normal_points": normal_points,
      // "bad_points": bad_points,
      // "detail": detail
    });

    nowGood = 0;
    nowNormal = 0;
    nowBad = 0;
    util.timerId = setInterval(function() {
      if (nowGood == good_points &&
        nowBad == bad_points &&
        nowNormal == normal_points) {
        clearInterval(util.timerId);
      }

      $(".rank-good").html(nowGood).css({
        "width": 50 + nowGood * 3 + "px",
        "height": 50 + nowGood * 3 + "px",
        "line-height": 50 + nowGood * 3 + "px",
        "margin": (0 + ((70 - nowGood * 3) / 2)) + "px " + (15 + ((70 - nowGood * 3) / 2)) + "px"
      });
      $(".rank-normal").html(nowNormal).css({
        "width": 50 + nowNormal * 3 + "px",
        "height": 50 + nowNormal * 3 + "px",
        "line-height": 50 + nowNormal * 3 + "px",
        "margin": (0 + ((70 - nowNormal * 3) / 2)) + "px " + (15 + ((70 - nowNormal * 3) / 2)) + "px"
      });
      $(".rank-bad").html(nowBad).css({
        "width": 50 + nowBad * 3 + "px",
        "height": 50 + nowBad * 3 + "px",
        "line-height": 50 + nowBad * 3 + "px",
        "margin": (0 + ((70 - nowBad * 3) / 2)) + "px " + (15 + ((70 - nowBad * 3) / 2)) + "px"
      });

      if (nowGood < good_points)
        nowGood++;
      if (nowNormal < normal_points)
        nowNormal++;
      if (nowBad < bad_points)
        nowBad++;

      if (nowGood > good_points)
        nowGood = good_points;
      if (nowNormal > normal_points)
        nowNormal = normal_points;
      if (nowBad > bad_points)
        nowBad = bad_points;
    }, 30);
    $("#rank").html(html);
  }

  function renderArticles(data) {
    var rank = util.nowFilter;
    var subset;
    var html = "";
    if (rank == "good")
      subset = data.good;
    else if (rank == "normal")
      subset = data.normal;
    else if (rank == "bad")
      subset = data.bad;
    if (subset != undefined) {
      $.each(subset, function(num, article) {
        html += "<li><a href='" + article.link + "' target='_blank'>" + article.title + "</a></li>";
      });
      $("#article-list").html(html);
    }
  }

  return {
    movieindex: 0,
    movielist: [],
    movieId: 0,
    nowFilter: "good",
    timerId: 0,
    getArticleByIdAndRank: function(id, rank) {
      util.nowFilter = rank;
      if (articles[id] == undefined) {
        $.ajax({
          url: "api/v1/article_list?id=" + id,
          cache: false,
          // async: false,
          success: function(data) {
            articles[id] = data;
            renderArticles(data);
          }
        });
      } else {
        renderArticles(articles[id]);
      }
    },
    getRankById: function(id) {
      util.movieId = id;
      util.getArticleByIdAndRank(id, util.nowFilter);
      if (eval("details.m" + id) == undefined) {
        $.ajax({
          url: "api/v1/movie_rank?id=" + id,
          cache: false,
          success: function(data) {
            $.ajax({
              url: "api/v1/movie_detail?id=" + id,
              cache: false,
              success: function(data2) {
                details["m" + id] = {
                  rank: data,
                  detail: data2
                };
                renderMovieDetail(id);
              }
            })
          }
        })
      } else {
        renderMovieDetail(id);
      }
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
          items = "";
          $.each(leftdata, function(index, movie) {
            items += mTemplate(movie);
          })
          $("#movie-list-left").html(items);
          $("#cube .two .poster").css("background-image", "url(" + util.movielist[0].image.replace("mpost2", "mpost") + ")");
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
  var base = 5000;
  $(".sense-area").bind("swipeleft", function(e) {
    if (!shifting) {
      left();
    }
  });
  $(".sense-area").bind("swiperight", function(e) {
    if (!shifting) {
      right();
    }
  });
  $(".sense-area").bind("mousewheel", function(e) {
    if (!shifting) {
      if (e.originalEvent.wheelDelta > 0) {
        left();
      } else {
        right();
      }
      $(".sense-area").scrollTop(base);
    }
  });
  $(".sense-area").scrollTop(base);
}();