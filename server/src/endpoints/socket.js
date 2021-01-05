let id = 0;
function createUniqueId() {
    id++;
    return id;
}
let connectedUsers = [];

const plantNotificationWebSocket = (ws, req) => {
    let newId = createUniqueId();
    ws.id = newId;
    connectedUsers[newId] = ws;
    console.log(`User ${ws.id} connected`);
    ws.send(JSON.stringify({socketId: ws.id}))

    ws.on('close', function (wss) {
        console.log(`User ${ws.id} disconnected`);
        const removeIndex = connectedUsers.indexOf(ws.id);
        connectedUsers.splice(removeIndex, 1);
    });
};

module.exports = {
    connectedUsers,
    plantNotificationWebSocket
};