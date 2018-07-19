app.config(function ($stateProvider) {
    $stateProvider
        .state('mainShower', {
            url: '/mainShower',
            views: {
                'content@': {
                    templateUrl: 'mainShower.html',
                    controller: 'showerController'
                }
            }
        });
});
