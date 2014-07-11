'use strict';

(function(){
  var app = angular.module('toast', ['ngRoute']);
  app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/', { controller: 'HomeController', templateUrl: 'templates/default.html' })
      .otherwise({ redirectTo: '/' });  
  }]);

  app.controller('HomeController', ['$scope', function($scope){
    $scope.version = angular.version.full;
  }]);

})();