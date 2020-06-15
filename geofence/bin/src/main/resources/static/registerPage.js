
function onRegisterClicked() {
    let username = document.getElementById('register').value;
    let password = document.getElementById('password').value;
    let autoCompleteProviderRadio= document.getElementsByName("AutocompleteProvider");
    let autoFlag=false;
    let autocompleteProvider;

    for (let i = 0; i < autoCompleteProviderRadio.length; i++) {
        if (autoCompleteProviderRadio[i].checked) {
            autocompleteProvider = autoCompleteProviderRadio[i].value;
            autoFlag = true;
        }
    }
    if (username === '' || password === '') {
        alert('Please enter valid username and password');
        return;
    }
    if(!autoFlag) {
        alert('Please set an Autocomplete provider');
        return;
    }
    let loginRequest = {
        "name": username,
        "password": password,
        "autocompleteProvider":autocompleteProvider
    };
    let requestOptions = {
        method: 'POST',
        redirect: 'follow',
        headers: new Headers({'content-type': 'application/json'}),
        body: JSON.stringify(loginRequest)
    };
    fetch('tenant/register', requestOptions)
       .then(response => {
            if (response.status === 400) {
                let errorMessage = 'Username already exists';
                alert(errorMessage);
                throw(errorMessage);
            }
            return response.json();
        })

    .then(result => {
        console.log(result);
       window.location.href = 'index.html';
    })
    .catch(error => {
        console.error(error);
    })

}
