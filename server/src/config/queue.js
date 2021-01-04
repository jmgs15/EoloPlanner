const amqp = require('amqplib/callback_api');
const db = require('../config/database');
const CONN_URL = 'amqp://guest:guest@localhost';
const createPlantRequestQueue = 'eoloplantCreationRequests';
const notificationsQueue = 'eoloplantCreationProgressNotifications';
const relatedCityClients = require('../models/relatedCityClients');

let notificationChannel = null;
let creationChannel = null;

async function initialize(wss) {
    amqp.connect(CONN_URL, async function (err, conn) {

        notificationChannel = await conn.createChannel(function (error, channel) {
            if (error) {
                throw error;
            }
            channel.assertQueue(notificationsQueue, {
                durable: false
            });

            channel.consume(notificationsQueue, function (msg) {
                    console.log("Message:", msg.content.toString());
                    let plant = JSON.parse(msg.content.toString());
                    wss.clients.forEach(function (client) {
                        if (plant.progress == 100 || client.id == relatedCityClients.getClient(plant.id).client) {
                            client.send(msg.content.toString());
                            // if (plant.progress == 100) {
                            //     relatedCityClients.remove(plant.id);
                            // }
                        }
                    });
                    updateDatabase(JSON.parse(msg.content))
                }, {noAck: true}
            );
        });

        creationChannel = await conn.createChannel(function (error, channel) {
            if (error) {
                throw error;
            }
            channel.assertQueue(createPlantRequestQueue, {
                durable: false
            });
        });
    });
}

process.on('exit', (code) => {
    notificationChannel.close();
    creationChannel.close();
    console.log(`Closing rabbitmq channel`);
});

const sendMessage = (message) => {
    console.log("publishToQueue: '" + message + "'");
    creationChannel.sendToQueue(createPlantRequestQueue, Buffer.from(message));
};

function updateDatabase(plantInfo) {
    db.Plant.update(
        {progress: plantInfo.progress},
        {where: {id: plantInfo.id}})
}

module.exports.initialize = initialize;
module.exports.sendMessage = sendMessage;