/* jQuery */
$(document).ready(function() {
  checkForLocalStore();

  if (store.get("style") === "default")
  {
    $(".navbar-right > li.active").removeClass("active");

    $this = $(".defaultStyleBtn").parent();
    if (!$this.hasClass("active")) {
      $this.addClass("active");
    }

    $(".defaultStyle").attr("rel", "stylesheet");
    $(".greenStyle").attr("rel", "stylesheetGreen");
    $(".redStyle").attr("rel", "stylesheetRed");
    $(".blueStyle").attr("rel", "stylesheetBlue");
  }
  else if (store.get("style") === "green")
  {
    $(".navbar-right > li.active").removeClass("active");

    $this = $(".greenStyleBtn").parent();
    if (!$this.hasClass("active")) {
      $this.addClass("active");
    }

    $(".defaultStyle").attr("rel", "stylesheetDefault");
    $(".greenStyle").attr("rel", "stylesheet");
    $(".redStyle").attr("rel", "stylesheetRed");
    $(".blueStyle").attr("rel", "stylesheetBlue");
  }
  else if (store.get("style") === "red")
  {
    $(".navbar-right > li.active").removeClass("active");

    $this = $(".redStyleBtn").parent();
    if (!$this.hasClass("active")) {
      $this.addClass("active");
    }

    $(".defaultStyle").attr("rel", "stylesheetDefault");
    $(".greenStyle").attr("rel", "stylesheetGreen");
    $(".redStyle").attr("rel", "stylesheet");
    $(".blueStyle").attr("rel", "stylesheetBlue");
  }
  else if (store.get("style") === "blue")
  {
    $(".navbar-right > li.active").removeClass("active");

    $this = $(".blueStyleBtn").parent();
    if (!$this.hasClass("active")) {
      $this.addClass("active");
    }

    $(".defaultStyle").attr("rel", "stylesheetDefault");
    $(".greenStyle").attr("rel", "stylesheetGreen");
    $(".redStyle").attr("rel", "stylesheetRed");
    $(".blueStyle").attr("rel", "stylesheet");
  }

  $(".nav-pills > li").click(function() {
    $(".nav-pills > li.active").removeClass("active");
    var $this = $(this);
    if (!$this.hasClass("active")) {
      $this.addClass("active");
    }
  });

  $(".defaultStyleBtn").click(function() {
    $(".defaultStyle").attr("rel", "stylesheet");
    $(".greenStyle").attr("rel", "stylesheetGreen");
    $(".redStyle").attr("rel", "stylesheetRed");
    $(".blueStyle").attr("rel", "stylesheetBlue");

    store.set("style", "default");
  });

  $(".greenStyleBtn").click(function() {
    $(".defaultStyle").attr("rel", "stylesheetDefault");
    $(".greenStyle").attr("rel", "stylesheet");
    $(".redStyle").attr("rel", "stylesheetRed");
    $(".blueStyle").attr("rel", "stylesheetBlue");

    store.set("style", "green");
  });

  $(".redStyleBtn").click(function() {
    $(".defaultStyle").attr("rel", "stylesheetDefault");
    $(".greenStyle").attr("rel", "stylesheetGreen");
    $(".redStyle").attr("rel", "stylesheet");
    $(".blueStyle").attr("rel", "stylesheetBlue");

    store.set("style", "red");
  });

  $(".blueStyleBtn").click(function() {
    $(".defaultStyle").attr("rel", "stylesheetDefault");
    $(".greenStyle").attr("rel", "stylesheetGreen");
    $(".redStyle").attr("rel", "stylesheetRed");
    $(".blueStyle").attr("rel", "stylesheet");

    store.set("style", "blue");
  });

  $(".carousel").carousel({
    interval: 6000
  });
});

function checkForLocalStore() {
  if (!store.enabled) {
    $(".alert-section").append(
      "<div class='alert alert-danger alert-dismissable'>" +
      "<button type='button' class='close' data-dismiss='alert'>" +
      "<span aria-hidden='true'>&times;</span>" +
      "<span class='sr-only'>Close</span>" +
      "</button>" +
      "Site error: Local storage is not supported by your browser." +
      "</div>");
  }
}
