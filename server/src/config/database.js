const { Sequelize } = require('sequelize');
const database = "eoloplant";

const sequelize = new Sequelize(database, "root", "pass", {dialect: 'mysql'});

function initialize() {
    return sequelize.sync({ force: false });
}

module.exports = {
    initialize,
    sequelize
};