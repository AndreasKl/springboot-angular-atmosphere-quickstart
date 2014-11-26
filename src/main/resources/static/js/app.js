'use strict';

(function() {
  var app = angular.module('toast', [ 'ngRoute' ]);
  app.config([ '$routeProvider', function($routeProvider) {
    $routeProvider.when('/', {
      controller : 'HomeController',
      templateUrl : 'templates/default.html'
    }).otherwise({
      redirectTo : '/'
    });
  } ]);

  app.controller('HomeController', [ '$scope', 'ToastService', function($scope, toastService) {
    $scope.version = angular.version.full;
    $scope.messages = toastService.messages;
  } ]);

  app.factory('ToastService', [ '$rootScope', function($rootScope) {
    var messages = [];
    var websocketSocket = atmosphere;
    var websocketSubSocket;
    var websocketTransport = 'websocket';

    var websocketRequest = {
      url : '/websocket/toast',
      contentType : "application/json",
      transport : websocketTransport,
      trackMessageLength : true,
      withCredentials : true,
      reconnectInterval : 5000,
      enableXDR : true,
      timeout : 60000
    };

    websocketRequest.onOpen = function(response) {
      console.log('Trying to use transport: ' + response.transport);
      websocketTransport = response.transport;
    };

    websocketRequest.onClientTimeout = function(r) {
      setTimeout(function() {
        websocketSubSocket = websocketSocket.subscribe(websocketRequest);
      }, websocketRequest.reconnectInterval);
    };

    websocketRequest.onClose = function(response) {
      console.log('Server closed websocket connection. Changing transport to: '+ response.transport);
    };

    websocketRequest.onMessage = function(data) {
      $rootScope.$apply(function() {
        messages.push(data.responseBody);
      });
    };

    websocketSubSocket = websocketSocket.subscribe(websocketRequest);

    return {
      messages : messages
    };
  } ]);
})();