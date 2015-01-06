var xAngle = -20;
var yAngle = 0;
var shifting = false;
var face = 0;

function changePoster() {
  var filename = util.movielist[util.movieindex].image.replace("mpost2", "mpost");
  if (face < 0)
    face = 3;
  if (face > 3)
    face = 0;

  if (face == 0)
    $("#cube .two .poster").css("background-image", "url(" + filename + ")");
  if (face == 1)
    $("#cube .five .poster").css("background-image", "url(" + filename + ")");
  if (face == 3)
    $("#cube .three .poster").css("background-image", "url(" + filename + ")");
  if (face == 2)
    $("#cube .four .poster").css("background-image", "url(" + filename + ")");
}

function left() {
  if (!shifting) {
    shifting = true;

    clearInterval(util.timerId);
    yAngle -= 90;
    $('#cube').css("webkitTransform", "rotateX(" + xAngle + "deg) rotateY(" + yAngle + "deg)");

    $("#movie-list-right span:first-child").remove();
    $("#movie-list-right")
      .css("margin-left", 122)
      .append(util.renderMovieBlock(util.movieindex))
      .animate({
        "margin-left": "0",
      }, 300, function() {
        shifting = false;
      });

    util.movieindex--;
    if (util.movieindex < 0)
      util.movieindex = util.movielist.length - 1;

    $("#movie-list-left span:last-child").remove();
    $("#movie-list-left")
      .css("margin-right", -122)
      .prepend(util.renderMovieBlock(util.movieindex))
      .animate({
        "margin-right": "0",
      }, 300, function() {});

    util.getRankById(util.movielist[util.movieindex].id);

    face--;
    changePoster();
  }
}

function right() {
  if (!shifting) {
    shifting = true;

    clearInterval(util.timerId);
    yAngle += 90;
    $('#cube').css("webkitTransform", "rotateX(" + xAngle + "deg) rotateY(" + yAngle + "deg)");

    $("#movie-list-left span:first-child").remove();
    $("#movie-list-left")
      .css("margin-right", 122)
      .append(util.renderMovieBlock(util.movieindex))
      .animate({
        "margin-right": "0",
      }, 300, function() {
        shifting = false;
      });

    util.movieindex++;
    if (util.movieindex == util.movielist.length)
      util.movieindex = 0;

    $("#movie-list-right span:last-child").remove();
    $("#movie-list-right")
      .css("margin-left", -122)
      .prepend(util.renderMovieBlock(util.movieindex))
      .animate({
        "margin-left": "0",
      }, 300, function() {});

    util.getRankById(util.movielist[util.movieindex].id);

    face++;
    changePoster();
  }
}

document.addEventListener('keydown', function(e) {
  switch (e.keyCode) {
    case 37:
      left();
      break;
    case 39:
      right();
      break;
  };
}, false);
$('#cube').css("webkitTransform", "rotateX(" + xAngle + "deg) rotateY(" + yAngle + "deg)");



function rand(from, to) {
  return from + Math.floor(Math.random() * (to - from));
}

function go(name) {
  $("#container").empty();
  var timeoutId = 0;
  var params = [];
  var i = 0;
  var num = rand(0, 70);

  if (name == undefined)
    name = "popcorn";

  timeoutId = setInterval(function() {
    if (i < num)
      i++;
    else {
      clearInterval(timeoutId);
      return;
    }

    var param = {
      ho: rand(-60, 60),
      he: rand(-70, 70),
      v: rand(-250, -170),
      d: rand(5, 7) / 10,
      r: rand(0, 360)
    };
    params[i] = param;
    $("#container").append("<div id='popcorn-layer" + i + "' class='popcorn-layer'><div class='" + name + "' pid='" + i + "'></div></div>");
    $("#popcorn-layer" + i).css("z-index", 100 + i);
    $("#popcorn-layer" + i + " ." + name).css("left", param.ho + "px");
    $("#popcorn-layer" + i + " ." + name).css("top", "-5px");

    /* vertical moving */
    TweenMax.to("#popcorn-layer" + i + " ." + name, param.d, {
      top: param.v + "px",
      ease: Sine.easeOut,
      onComplete: function() {
        var j = parseInt($(this.target[0]).attr("pid"));
        $("#popcorn-layer" + j).css("z-index", (2000 + j));
        TweenMax.to(this.target[0], params[j].d * 2, {
          top: rand(450, 520) + "px",
          ease: Sine.easeIn,
        });
      }
    });

    /* horizontal moving */
    TweenMax.to("#popcorn-layer" + i + " ." + name, param.d, {
      left: param.he + "px",
      ease: Linear.easeNone,
      onComplete: function() {
        var j = parseInt($(this.target[0]).attr("pid"));
        TweenMax.to(this.target[0], params[j].d * 1.5, {
          left: params[j].ho + ((params[j].he - params[j].ho) * 3) + "px",
          ease: Linear.easeNone,
        });
      }
    });

    /* rotating */
    TweenMax.to("#popcorn-layer" + i + " ." + name, param.d * 2.2, {
      directionalRotation: (param.r + rand(300, 420)) + "_ccw",
      ease: Linear.easeNone
    });
  }, 100);
}