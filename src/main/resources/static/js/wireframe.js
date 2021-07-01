$(window).on('load', function() {
    'use strict';

    // Enable the search button only if the search field is filled up
    $('#query').on('input', function() {
        $('#submit-query')[0].disabled = (this.value.trim() == '');
    });
});
