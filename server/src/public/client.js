let socket = new WebSocket("ws://" + window.location.host + "/plantNotifications");
const baseUrlPath = "http://localhost:3000/eolicplants";
const topographyUrlPath = "http://localhost:8080/api/topographicdetails/cityLandscapes";
let plantsCreated = [];
let availableCitiesCreated = [];
let socketId = null;
let localPlant = { id: -1 };

loadAvailableCities();
loadEolicPlants();

socket.onopen = function (e) {
    console.log("WebSocket connection established");
};

socket.onmessage = function (event) {
    let content = JSON.parse(event.data);
    console.log(`Message from socket: ${JSON.stringify(content)}`);
    if (content.socketId) {
        socketId = content.socketId;
    } else {
        updateProgress(content);
    }
};

socket.onclose = function (event) {
    if (event.wasClean) {
        console.log(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);
    } else {
        console.log('[close] Connection died');
    }
};

socket.onerror = function (error) {
    console.log(`[error] ${error.message}`);
};

function updateProgress(plant) {
    let progressText = document.getElementById("progressText");

    if (plant.progress === 100) {
        progressText.innerText = "";
        manageCreatingPlantButton(plant);
        addPlantToList(plant);
    } else {
        progressText.innerText = `Creating plant in ${plant.city}, progress:  ${plant.progress}%`;
    }
}

function manageCreatingPlantButton(plant) {
    if (plant.id == localPlant.id) {
        let creationButton = document.getElementById("creationButton");
        creationButton.disabled ? creationButton.disabled = false : creationButton.disabled = true;
    }
}

function createPlant() {
    let city = document.getElementById("city").value;
    let plant = {"city": city, "progress": 0};

    if (city == "" || !isCityAvailable(city)) {
        alert("You must enter a valid city");
    } else {
        fetch(baseUrlPath, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'socketid': socketId
            },
            body: JSON.stringify(plant)
        })
            .then(function (response) {
                if (response.ok) {
                    return response.json();
                } else {
                    throw "Error en la llamada Ajax";
                }
            })
            .then(function (plant) {
                console.log(`Message from post: ${JSON.stringify(plant)}`);
                localPlant = plant;
                manageCreatingPlantButton(plant);
                updateProgress(plant);
            })
            .catch(function (err) {
                console.log(err);
            });
    }
}

function addPlantToList(plant) {
    plantsCreated.push({id: plant.id, city: plant.city});
    let ul = document.getElementById("plants");
    let li = document.createElement("li");
    li.appendChild(document.createTextNode(plant.city));
    ul.appendChild(li);
}

function addCityLandscapeToList(cityLandscape) {
    availableCitiesCreated.push({city: cityLandscape.id});
    let ul = document.getElementById("availableCities");
    let li = document.createElement("li");
    li.appendChild(document.createTextNode(cityLandscape.id));
    ul.appendChild(li);
}

function loadEolicPlants() {
    fetch(baseUrlPath, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                throw "Error getting eolic plants created";
            }
        })
        .then(function (plants) {
            for (let plant of plants) {
                addPlantToList(plant);
            }
        })
        .catch(function (err) {
            console.log(err);
        });
}

function loadAvailableCities() {
    fetch(topographyUrlPath, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                throw "Error getting eolic plants created";
            }
        })
        .then(function (landscapes) {
            for (let landscape of landscapes) {
                addCityLandscapeToList(landscape);
            }
        })
        .catch(function (err) {
            console.log(err);
        });
}

function isCityAvailable(nameCity){
    for (var i=0; i < availableCitiesCreated.length; i++) {
        if (availableCitiesCreated[i].city.toLowerCase() === nameCity.toLowerCase()) {
            return true;
        }
    }
    return false;
}

