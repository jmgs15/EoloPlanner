const amqp = require('amqplib/callback_api');
const { Plant } = require('../models/plant');
const CONN_URL = 'amqp://guest:guest@localhost';
const createPlantRequestQueue = 'eoloplantCreationRequests';
const notificationsQueue = 'eoloplantCreationProgressNotifications';
const relatedCityClients = require('../models/relatedCityClients');

let rabbitChannel = null;

async function initialize() {
    amqp.connect(CONN_URL, async function (err, conn) {

        rabbitChannel = await conn.createChannel(function (error, channel) {
            if (error) {
                throw error;
            }
            channel.assertQueue(notificationsQueue, {
                durable: false
            });

            channel.assertQueue(createPlantRequestQueue, {
                durable: false
            });

            channel.consume(notificationsQueue, function (msg) {
                    console.log("Message:", msg.content.toString());
                    let plant = JSON.parse(msg.content.toString());
                    const { connectedUsers } = require('../endpoints/socket');
                    connectedUsers.forEach(function (client) {
                        if (plant.progress == 100 || client.id == relatedCityClients.getClient(plant.id).client) {
                            client.send(msg.content.toString());
                        }
                    });
                    updateDatabase(JSON.parse(msg.content))
                }, {noAck: true}
            );
        });
    });
}

process.on('exit', (code) => {
    rabbitChannel.close();
    console.log(`Closing rabbitmq channel`);
});

const sendMessage = (message) => {
    console.log("publishToQueue: '" + message + "'");
    rabbitChannel.sendToQueue(createPlantRequestQueue, Buffer.from(message));
};

function updateDatabase(plantInfo) {
    Plant.update(
        {progress: plantInfo.progress},
        {where: {id: plantInfo.id}})
}

module.exports = {
    initialize,
    sendMessage
};