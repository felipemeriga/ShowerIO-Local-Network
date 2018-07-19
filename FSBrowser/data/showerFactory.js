app.factory('showerFactory', ['$q', function ($q) {
    this.initialized = deferred.promise;
    this.user = {
        access: false
    };
    this.isAuthenticated = function () {

        var deferred;
        deferred = $q.defer();

        this.user = {
            first_name: 'First',
            last_name: 'Last',
            email: 'email@address.com',
            access: 'institution'
        };
        return deferred.resolve();
    };
}
]);