let currentPage = 1;
let pageSize = 10;
let sortBy = 'name';


let jwt ;


$(document).ready(generateTenantId());

async function generateTenantId() {
    if (document.cookie.length === 0) {
        console.error('Could not find jwt token');
    } else {
        let cookieValue = document.cookie;
        jwt = await cookieValue.split(";")[0].split("=")[1].trim();
        console.log(jwt);

        loadGeofenceList();
    }
}


function loadGeofenceList(pageNumber = 1) {

    fetch("geofence/getPaginated?pageNo=" + (pageNumber - 1) + "&pageSize=" + pageSize, {
        headers: {
            Authorization: jwt
        }
    })
        .then(response => response.text())
        .then(result => {
            const geofenceList = JSON.parse(result);
            let geofenceDetails = geofenceList.map(function (fence) {
                return {id: fence.id, name: fence.name};
            });
            populateGeofenceList(geofenceDetails);
            addGeofenceListeners(geofenceDetails);
            populatePageNumbers();
            addPageNumberListeners();
        });
}

function populateGeofenceList(geofenceDetails) {
    let listBody = document.getElementById('geofence-list-group');
    for (let i = 0; i < geofenceDetails.length; i++) {
        let item = document.createElement('button');
        item.setAttribute('type', 'button');
        item.classList.add('list-group-item');
        item.classList.add('list-group-item-action');
        item.classList.add('geofence-list-item');
        item.id = 'geofence-item-id-' + i;
         item.appendChild(document.createTextNode(geofenceDetails[i].name));
        listBody.appendChild(item);
        item = addDeleteButtonToGeofenceRow(item, i);
        listBody.appendChild(item);
    }
}



function addDeleteButtonToGeofenceRow(item, i) {
    let deleteButton = document.createElement('div');
    deleteButton.id = 'delete-button-' + i;
    deleteButton.classList.add('geofence-delete-button');
    deleteButton.appendChild(document.createTextNode('Delete'));
    item.appendChild(deleteButton);
    return item;
}


function addGeofenceListeners(geofenceDetails) {
    let geofenceList = document.getElementsByClassName('list-group-item');
    for (let geofenceItem of geofenceList) {
        geofenceItem.addEventListener('click', function (event) {
            let clickedElement = document.getElementById(event.target.id);

            // if Delete button is clicked
            if (clickedElement.innerText === 'Delete') {
                onDeleteButtonClicked(clickedElement, geofenceDetails);
                return;
            }
            else if(clickedElement.innerText === 'Update'){
                console.log("Update");
                let geofenceName = clickedElement.parentNode.innerText.split('\n')[0];
                let geofenceId = geofenceDetails.filter(fence => fence.name === geofenceName);

                if (geofenceId.length < 1 || geofenceName === '') {
                    alert('Could not load geofence');
                    return;
                }
                geofenceId = geofenceId[0].id;
                window.location.href = 'geofenceUpdate.html?id=' + geofenceId + '&name=' + geofenceName;
                return;
            }
            else{
                // if geofence is clicked
                let geofenceName = clickedElement.innerText.split('\n')[0];
                let geofenceId = geofenceDetails.filter(fence => fence.name === geofenceName);

                if (geofenceId.length < 1 || geofenceName === '') {
                    alert('Could not load geofence');
                    return;
                }
                geofenceId = geofenceId[0].id;
                window.location.href = 'geofence.html?id=' + geofenceId + '&name=' + geofenceName;}

        });
    }
}

function onDeleteButtonClicked(clickedElement, geofenceDetails) {
    let deleteButtonIndex = clickedElement.getAttribute('id').split('-')[2];
    let geofenceToBeDeletedName = document.getElementById('geofence-item-id-' + deleteButtonIndex).innerText;
    geofenceToBeDeletedName = geofenceToBeDeletedName.split('\n')[0];

    if (!confirm('Are you sure you want to delete ' + geofenceToBeDeletedName + ' geofence?')) {
        return;
    }

    let geofenceToBeDeletedId = geofenceDetails.filter(fence => fence.name === geofenceToBeDeletedName);
    if (geofenceToBeDeletedId.length < 1) {
        alert('Could not delete geofence');
        return;
    }
    geofenceToBeDeletedId = geofenceToBeDeletedId[0].id;

    let requestOptions = {
        method: 'DELETE',
        redirect: 'follow',
        headers: {
            Authorization: jwt
        }
    };

    fetch('geofence/delete?id=' + geofenceToBeDeletedId, requestOptions)
        .then(response => response.text())
        .then(result => {
            clearPageNumberList();
            clearGeofenceList();

            if (geofenceDetails.length === 1) {
                currentPage -= 1;
            }
            loadGeofenceList(currentPage);
        });

}

function populatePageNumbers() {
    let pageCount = 1;
    let listBody = document.getElementById('page-numbers-list-group');
    let geofenceCount = 1;
    fetch("geofence/getAllNames",
        {
            headers: {
                Authorization: jwt
            }
        })
        .then(response => response.text())
        .then(result => {
            geofenceCount = JSON.parse(result).length;
            pageCount = Math.ceil(geofenceCount / pageSize);

            for (let i = 0; i < pageCount; i++) {
                let item = createPageNumberElement(i + 1);
                listBody.appendChild(item);
            }
        });

}

function createPageNumberElement(pageNumber) {
    let item = document.createElement('li');
    item.setAttribute('type', 'button');
    item.setAttribute('id', 'page-item-id-' + pageNumber);
    item.classList.add('page-item');

    let itemLink = document.createElement('a');
    itemLink.setAttribute('id', 'page-link-' + pageNumber);
    itemLink.setAttribute('href', '#');
    itemLink.classList.add('page-link');
    itemLink.appendChild(document.createTextNode(pageNumber.toString()));
    item.appendChild(itemLink);
    return item;
}

function clearGeofenceList() {
    const currentList = document.getElementById('geofence-list-group');
    currentList.textContent = '';
}

function clearPageNumberList() {
    const currentList = document.getElementById('page-numbers-list-group');
    currentList.textContent = '';
}

function addPageNumberListeners() {
    let pageList = document.getElementById('page-numbers-list-group');
    pageList.addEventListener('click', function (event) {
        let pageNumber = document.getElementById(event.target.id).innerText;
        if (pageNumber === currentPage) {
            return;
        }

        fetch("geofence/getPaginated?pageNo=" + (pageNumber - 1) + "&pageSize=" + pageSize + "&sortBy=" + sortBy, {
            headers: {
                Authorization: jwt
            }
        })
            .then(response => response.text())
            .then(result => {
                const geofenceList = JSON.parse(result);
                let geofenceDetails = geofenceList.map(function (fence) {
                    return {id: fence.id, name: fence.name};
                });
                currentPage = pageNumber;

                clearGeofenceList();
                populateGeofenceList(geofenceDetails);
                addGeofenceListeners(geofenceDetails);
            });
    })
}

function deleteAllPolygons() {
    let requestOptions = {
        method: 'DELETE',
        redirect: 'follow',
        headers: {
            Authorization: jwt
        }
    };
    fetch("geofence/deleteAll", requestOptions);

}

function createNewGeofence() {
    window.location.href = 'geofence.html';
}

