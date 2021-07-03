$(window).on('load', function() {
    'use strict';

    // Enable tooltips on seat icons
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map((tooltipTriggerElement) => {
        return new bootstrap.Tooltip(tooltipTriggerElement);
    });

    // Add the listener used to track the available/selected state to every seat icon
    $('.seat').on('click', function(event) {
        // Toggle the seat status between available and selected
        if (event.target.status == 'selezionato') {
            event.target.status = 'disponibile';
            event.target.src = '/static/img/seat-available.svg';
        } else {
            event.target.status = 'selezionato';
            event.target.src = '/static/img/seat-selected.svg';
        }

        // Enable/disable the button used to go to the checkout form based on the number of selected seats
        if (getSelectedSeats() == 0) {
            $('#go-to-checkout').addClass('disabled');
            $('#go-to-checkout').removeClass('invisible');

            // Reset the age discount inputs
            $('#age-under').val(0);
            $('#age-over').val(0);
            $('#age-under').removeClass('is-invalid');
            $('#age-under').addClass('is-valid');
            $('#age-over').removeClass('is-invalid');
            $('#age-over').addClass('is-valid');
            $('.discount-entry').removeClass('text-danger');
            $('.discount-entry').addClass('text-success');

            // If there are no seats selected, hide the collapsible checkout form
            checkoutDetailsCollapse.hide();
        } else {
            $('#go-to-checkout').removeClass('disabled');
        }

        // Update the status of the current seat in the backend
        $.ajax({
            type: 'POST',
            url: 'update-seat-status',
            data: { 
                'reservation-id': $('#reservation-id-buy').val(),
                'seat-id': event.target.id,
                'seat-status': event.target.status
            }
        }).done(function(response) {
            if (response == 'ok') {
                // Trigger the validation of age discount inputs with the new selected seats and update the shopping cart
                updateAgeDiscountInputsAndShoppingCart();
            } else {
                // Alert the spectator if the seat status couldn't be updated
                showAlert(false, true, 'Errore si sincronizzazione', 'Si &egrave; verificato un errore nell\'aggiornare lo stato del posto selezionato. Riprova pi&ugrave; tardi.');
            }
        }).fail(function() {
            // Alert the spectator if something went wrong during the update
            showNetworkErrorAlert();
        });
    });

    // Create the object that represents the collapsible checkout form
    var checkoutDetails = $('#checkout-details')[0];
    var checkoutDetailsCollapse = new bootstrap.Collapse(checkoutDetails, {
        toggle: false
    });

    // Show the checkout form only when the buyer has chosen at least one seat
    $('#go-to-checkout').on('click', function() {
        $('#go-to-checkout').addClass('invisible');
        checkoutDetailsCollapse.show();
    });

    // Move the href to the checkout details section as soon as its transition finishes
    $('#checkout-details')[0].addEventListener('shown.bs.collapse', function() {
        document.location.href = '#go-to-checkout';
    });

    // Set the text shown when a form field is invalid
    $('.default-feedback').html('Questo campo &egrave; obbligatorio');

    // Trigger the validation of age discount inputs when they get modified by the spectator
    $('#age-under,#age-over').on('change', function() {
        updateAgeDiscountInputsAndShoppingCart();
    });

    // Apply the coupon and update the shopping cart when a coupon code is inserted
    $('#coupon-code-form')[0].addEventListener('submit', function(event) {
        event.preventDefault();
        event.stopPropagation();

        // Try to apply the coupon only if the form is filled up
        if (event.target.checkValidity()) {
            // Update the backend with the coupon code and refresh the shopping cart
            $.ajax({
               type: 'POST',
               url: $('#coupon-code-form').attr('action'),
               data: $('#coupon-code-form').serialize()
            }).done(function(response) {
                if (response == 'ok') {
                    // Update the shopping cart with the new entry
                    updateShoppingCart();
                    $('#coupon-code').attr('disabled', true);
                    $('#coupon-code-button').attr('disabled', true);
                } else {
                    // Alert the spectator if the coupon couldn't be used
                    showAlert(false, false, 'Errore durante l\'inserimento del coupon', 'Errore durante l\'inserimento del coupon. Controlla che il codice del coupon sia corretto e riprova.');
                }
            }).fail(function() {
                // Alert the spectator if something went wrong during the request
                showNetworkErrorAlert();
            });
        }
        event.target.classList.add('was-validated');
    }, false);

    // Send the checkout payment request to the backend and display a modal with its outcome
    $('#buy-form')[0].addEventListener('submit', function(event) {
        event.preventDefault();
        event.stopPropagation();

        // Try to conclude the reservation only if the form is filled up
        if (event.target.checkValidity()) {
            $.ajax({
                type: 'POST',
                url: $('#buy-form').attr('action'),
                data: $('#buy-form').serialize()
            }).done(function(response) {
                if (response == 'ok') {
                    // Show the success modal if the request had a positive outcome
                    showAlert(true, true, 'Acquisto completato. Grazie!', 'Acquisto effettuato correttamente.<br>Riceverai un\'e-mail a breve con la ricevuta di prenotazione da presentare all\'ingresso.');
                } else {
                    // Show the error modal
                    showAlert(false, true, 'Errore durante l\'acquisto', 'Errore durante il completamento dell\'acquisto.');
                }
            }).fail(function() {
                // Alert the spectator if something went wrong during the request
                showNetworkErrorAlert();
            });
        }
        event.target.classList.add('was-validated');
    }, false);

    // Keep track of the open/closed state of the modal alert (do not show two modals at the same time)
    window.alertOpened = false;

    // Set the modal alert as closed when it gets hidden
    var modalAlert = document.getElementById('modal-alert');
    modalAlert.addEventListener('hidden.bs.modal', function() {
        window.alertOpened = false;
    });

    /**
     * Shows a modal dialog to the spectator.
     *
     * If an alert is already opened (and not closed yet by the spectator), no other alerts can be shown.
     *
     * @param boolean success dialog type (true = success, false = failure).
     * @param boolean goBack  modal type (true = go back to the movie details page, false = stay on the checkout page).
     * @param string  title   title of the modal.
     * @param string  body    body content of the modal.
     */
    function showAlert(success, goBack, title, body) {
        // If an alert is already opened, do not open a new one
        if (window.alertOpened) {
            return;
        }

        // Set the modal alert as opened when it gets shown
        window.alertOpened = true;

        // Set the dialog type and content
        $('#modal-alert-component').removeClass(!success ? 'alert-success' : 'alert-warning');
        $('#modal-alert-component').addClass(success ? 'alert-success' : 'alert-warning');
        $('#modal-alert-icon').removeClass(!success ? 'fa-check-circle' : 'fa-exclamation-circle');
        $('#modal-alert-icon').addClass(success ? 'fa-check-circle' : 'fa-exclamation-circle');
        $('#modal-alert-label').html(title);
        $('#modal-alert-body').html(body);

        // Create the alert object
        var alert = new bootstrap.Modal($('#modal-alert')[0], {
            backdrop: goBack ? 'static' : true,
            keyboard: !goBack
        });

        // Show the appropriate close button (if goBack is true the spectator must go back to the movie details page)
        if (goBack) {
            $('#dismiss-modal-button').addClass('d-none');
            $('#go-to-movie-details-button').removeClass('d-none');
        } else {
            $('#dismiss-modal-button').removeClass('d-none');
            $('#go-to-movie-details-button').addClass('d-none');
        }

        // Show the alert
        alert.show();
    }

    /**
     * Shows a modal dialog to the spectator stating a network error occurred.
     */
    function showNetworkErrorAlert() {
        // Alert the spectator if something went wrong during the request
        showAlert(false, true, 'Errore di rete', 'Si &egrave; verificato un errore di rete: impossibile contattare il server. Riprova pi&ugrave; tardi.');
    }

    /**
     * Updates the shopping cart with the info retrieved from the backend.
     */
    function updateShoppingCart() {
        // Make the checkout info request to the backend and refresh the shopping cart
        $.ajax({
            type: 'POST',
            url: 'get-checkout-info',
            data: { 
                'reservation-id': $('#reservation-id-buy').val()
            }
        }).done(function(response) {
            // Parse the response from the backend (prices' dot decimal separator is converted to a comma, token separator is '\n')
            var splittedResponse = response.replaceAll('.', ',').split('\n');
            if (splittedResponse[0] == 'ok') {
                // Update the shopping cart entries
                $('#n-seats').html(splittedResponse[1] + ' x Biglietto cinema');
                $('#full-price').html(splittedResponse[2] + ' &euro;');
                switch (splittedResponse[3]) {
                    case 'AGE':
                        $('#type-of-discount').html('Sconto per et&agrave;');
                        break;
                    case 'NUMBER':
                        $('#type-of-discount').html('Sconto per numero di spettatori');
                        break;
                    case 'DAY':
                        $('#type-of-discount').html('Sconto per data di proiezione');
                        break;
                }
                $('#discount-amount').html('- ' + splittedResponse[4] + ' &euro;');
                $('#coupon-code-shopping-cart-entry').html(splittedResponse[5]);
                $('#coupon-discount').html('- ' + splittedResponse[6] + ' &euro;');
                $('#total-amount').html(splittedResponse[7] + ' &euro;');

                // Show the coupon entry only if a coupon was applied to the shopping cart
                if (splittedResponse[5] != 'no coupon') {
                    $('#coupon-shopping-cart-entry').removeClass('d-none');
                }
            } else {
                // Alert the spectator if something went wrong during the update to the shopping cart
                showAlert(false, true, 'Errore durante l\'aggiornamento del carrello', 'Si &egrave; verificato un errore durante l\'aggiornamento del carrello.');
            }
        }).fail(function() {
            // Alert the spectator if something went wrong during the request
            showNetworkErrorAlert();
        });
    }

    /**
     * Validates the age discount input fields in the shopping cart and synchronizes them with the backend, then updates the shopping cart.
     *
     * The inputs are valid only if the sum of the two does not exceed the number of selected seats at the moment of the update.
     * In this case, the inputs are marked as invalid and the backend is synchronized using 0 as the value of both inputs.
     */
    function updateAgeDiscountInputsAndShoppingCart() {
        // Difference between the number of selected seats necessary to consider the user inputs valid and the currently selected seats
        var extraSeats = parseInt($('#age-under').val()) + parseInt($('#age-over').val()) - getSelectedSeats();

        // Update the validation status of the inputs
        $('#age-under').removeClass((extraSeats > 0) ? 'is-valid' : 'is-invalid');
        $('#age-under').addClass((extraSeats > 0) ? 'is-invalid' : 'is-valid');
        $('#age-over').removeClass((extraSeats > 0) ? 'is-valid' : 'is-invalid');
        $('#age-over').addClass((extraSeats > 0) ? 'is-invalid' : 'is-valid');

        $('.discount-entry').removeClass((extraSeats > 0) ? 'text-success' : 'text-danger');
        $('.discount-entry').addClass((extraSeats > 0) ? 'text-danger' : 'text-success');

        // Update the backend with the current values of the inputs (both get set to 0 if the inputs are invalid)
        $.ajax({
            type: 'POST',
            url: $('#age-discount-form').attr('action'),
            data: {
                'reservation-id': $('#reservation-id-buy').val(),
                'age-under': (extraSeats > 0) ? '0' : $('#age-under').val(),
                'age-over': (extraSeats > 0) ? '0' : $('#age-over').val()
            }
        }).done(function(response) {
            if (response == 'ok') {
                // Update the shopping cart with the new discount amount
                updateShoppingCart();
            } else {
                // Show the error modal
                showAlert(false, true, 'Errore durante l\'aggiornamento del carrello', 'Si &egrave; verificato un errore durante l\'aggiornamento del carrello.');
            }
        }).fail(function() {
            // Alert the spectator if something went wrong during the request
            showNetworkErrorAlert();
        });
    }

    /**
     * Returns the number of currently selected seats.
     *
     * @returns number number of currently selected seats.
     */
    function getSelectedSeats() {
        var selectedSeats = 0;
        var seats = $('.seat').toArray();

        // Count the number of selected seats and return it
        for (var i = 0; i < seats.length - 1; i++) {
            if (seats[i].status == 'selezionato') {
                selectedSeats++;
            }
        }
        return selectedSeats;
    }
});
