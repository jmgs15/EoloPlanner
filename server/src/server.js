const { app } = require('./app');
const PORT = 3000;

const { sequelize } = require('./config/sequelize');
const queue = require('./config/queue');

sequelize.sync({ force: false })
    .then(() => queue.initialize())
    .then(() => app.listen(PORT, () => console.log(`Server listening on port: ${PORT}`)))
    .catch(error => console.error(error));