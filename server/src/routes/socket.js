let express = require('express');
const router = express.Router();
const { plantNotificationWebSocket } = require('../endpoints/socket');

router.ws('/plantNotifications', plantNotificationWebSocket);

module.exports = {
    webSocketRouter: router
};