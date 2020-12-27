const amqp = require('amqplib/callback_api');
const CONN_URL = 'amqp://guest:guest@localhost';
const createPlantRequestQueue = 'eoloplantCreationRequests';
const notificationsQueue = 'eoloplantCreationProgressNotifications';

let notificationChannel = null;
let creationChannel = null;

process.on('exit', (code) => {
    notificationChannel.close();
    creationChannel.close();
    console.log(`Closing rabbitmq channel`);
});

amqp.connect(CONN_URL, async function (err, conn) {

    notificationChannel = await conn.createChannel(function(error, channel) {
        if (error) {
            throw error;
        }
        channel.assertQueue(notificationsQueue, {
            durable: true
        });

        channel.consume(notificationsQueue, function (msg) {

                console.log("Message:", msg.content.toString());
                //TODO: Enviar por socket el progreso

            }, { noAck: true }
        );
    });

    creationChannel = await conn.createChannel(function(error, channel) {
        if (error) {
            throw error;
        }
        channel.assertQueue(createPlantRequestQueue, {
            durable: true
        });
    });
});


const sendMessage = (message) => {
	
	console.log("publishToQueue: '" + message + "'");
    creationChannel.sendToQueue(createPlantRequestQueue, Buffer.from(message));
};

exports.sendMessage = sendMessage;