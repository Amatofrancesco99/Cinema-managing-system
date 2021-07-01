$(window).on('load', () => {
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

        // Check if at least one seat is selected
        var atLeastOneSeatIsSelected = false;
        $('.seat').toArray().some(function(item) {
            if (item.status == 'selezionato') {
                atLeastOneSeatIsSelected = true;
            }
            return atLeastOneSeatIsSelected;
        });

        // Enable/disable the button used to go to the checkout form based on the number of selected seats
        if (!atLeastOneSeatIsSelected) {
            $('#go-to-checkout').addClass('disabled');
            $('#go-to-checkout').removeClass('invisible');

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
                'reservation-id': $('#reservation-id')[0].value,
                'seat-id': event.target.id,
                'seat-status': event.target.status
            }
        }).done(function(response) {
            if (response == 'ok') {
                // Update the shopping cart if the seat status is updated
                updateShoppingCart();
            } else {
                // Alert the user if the seat status couldn't be updated
                showAlert(false, 'Errore si sincronizzazione', 'Si &egrave; verificato un errore nell\'aggiornare lo stato del posto selezionato. Riprova pi&ugrave; tardi.');
            }
        }).fail(function() {
            // Alert the user if something went wrong during the update
            showAlert(false, 'Errore di rete', 'Si &egrave; verificato un errore di rete: impossibile contattare il server. Riprova pi&ugrave; tardi.');
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
                    // Alert the user if the coupon couldn't be used
                    showAlert(false, 'Errore durante l\'inserimento del coupon', 'Errore durante l\'inserimento del coupon. Controlla che il codice del coupon sia corretto e riprova.');
                }
            }).fail(function() {
                // Alert the user if something went wrong during the request
                showAlert(false, 'Errore di rete', 'Si &egrave; verificato un errore di rete: impossibile contattare il server. Riprova pi&ugrave; tardi.');
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
                    showAlert(true, 'Acquisto completato. Grazie!', 'Acquisto effettuato correttamente.<br>Riceverai un\'e-mail a breve con la ricevuta di prenotazione da presentare all\'ingresso.');
                } else {
                    // Show the error modal
                    showAlert(false, 'Errore durante l\'acquisto', 'Errore durante il completamento dell\'acquisto.');
                }
            }).fail(() => {
                // Alert the user if something went wrong during the request
                showAlert(false, 'Errore di rete', 'Si &egrave; verificato un errore di rete: impossibile contattare il server. Riprova pi&ugrave; tardi.');
            });
        }
        event.target.classList.add('was-validated');
    }, false);

    /**
     * Shows a modal dialog.
     *
     * @@param boolean success dialog type.
     * @@param string title title of the modal.
     * @@param string body body content of the modal.
     */
    function showAlert(success, title, body) {
        // Set the dialog type and content
        $('#modal-alert-component').addClass(success ? 'alert-success' : 'alert-warning');
        $('#modal-alert-icon').addClass(success ? 'fa-check-circle' : 'fa-exclamation-circle');
        $('#modal-alert-label').html(title);
        $('#modal-alert-body').html(body);

        // Show the alert
        var alert = new bootstrap.Modal($('#modal-alert')[0]);
        alert.show();
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
                'reservation-id': $('#reservation-id').val()
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
                // Alert the user if something went wrong during the update to the shopping cart
                showAlert(false, 'Errore durante l\'aggiornamento del carrello', 'Si &egrave; verificato un errore durante l\'aggiornamento del carrello.');
            }
        }).fail(function() {
            // Alert the user if something went wrong during the request
            showAlert(false, 'Errore di rete', 'Si &egrave; verificato un errore di rete: impossibile contattare il server. Riprova pi&ugrave tardi.');
        });
    }
});
