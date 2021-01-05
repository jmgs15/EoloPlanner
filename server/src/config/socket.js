const app = require('../app');

let id = 0;
function createUniqueId() {
    id++;
    return id;
}
let users = [];

function plantNotificationWebSocket(ws, req) {
    let newId = createUniqueId();
    ws.id = newId;
    //sendCreatedId(wss, newId);
    console.log(`User ${ws.id} connected`);
    ws.send(JSON.stringify({socketId: ws.id}))

    ws.on('close', function (ws) {
        const removeIndex = users.indexOf(ws);
        users.splice(removeIndex, 1);
    });
};

module.exports = {
    plantNotificationWebSocket
};