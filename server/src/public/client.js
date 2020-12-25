let socket = new WebSocket("ws://"+window.location.host+"/plantNotifications");

socket.onopen = function (e) {
    console.log("WebSocket connection established");
};

socket.onmessage = function (event) {
    console.log(`[message] Data received from server: ${event.data}`);
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

function sendMessage() {
    let plant = { "id": 1, "city": "Madrid" };
    //socket.send(plant);

    fetch('http://localhost:3000/eolicPlant', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(plant)
    })
        .then(function(response) {
            if(response.ok) {
                return response.text()
            } else {
                throw "Error en la llamada Ajax";
            }
        })
        .then(function(texto) {
            console.log(texto);
        })
        .catch(function(err) {
            console.log(err);
        });
}
