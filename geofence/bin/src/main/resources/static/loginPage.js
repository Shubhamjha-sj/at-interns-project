
function onLoginClicked() {
    let username = document.getElementById('login').value;
    let password = document.getElementById('password').value;

    if (username === '' || password === '') {
        alert('Please enter valid username and password');
        return;
    }

    let loginRequest = {
        "name": username,
        "password": password
    };
    let requestOptions = {
        method: 'POST',
        redirect: 'follow',
        headers: new Headers({'content-type': 'application/json'}),
        body: JSON.stringify(loginRequest)
    };
    fetch('tenant/login', requestOptions)
        .then(response => {
            if (response.status === 401) {
                let errorMessage = 'Invalid Credentials';
                alert(errorMessage);
                throw(errorMessage);
            }
            return response.json();
        })
        .then(result => {
            if (result.success) {
                let jwtToken = result.token;
                // window.location.href = 'geofenceList.html';
                $.ajax({
                    url: "geofenceList.html",
                    contentType: 'application/json',
                    headers: {
                        "Authorization": jwtToken
                    },
                });
                document.cookie = 'jwt=' + jwtToken;
                window.location.href = 'geofenceList.html';
                return;
            }
            throw('Could not process request. Please try again.')
        })
        .catch(error => {
            console.error(error);
        })
}
