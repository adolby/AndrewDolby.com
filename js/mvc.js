/* Angular */
(function() {
  var app = angular.module("app", []);

  app.directive("kryvosDownloads", function() {
    return {
      restrict: "E",
      templateUrl: "pages/kryvos-downloads.html"
    };
  });

  app.directive("contact", function() {
    return {
      restrict: "E",
      templateUrl: "pages/contact.html"
    };
  });

  app.controller("KryvosDownloadController", ["$http", "$log", function($http, $log, $scope) {
    var downloads = this;
    this.list = [];

    $http.get("../data/downloads.json").success(function(data) {
      downloads.list = data;
    });
  }]);


})();
