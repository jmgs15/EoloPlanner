const { Sequelize } = require('sequelize');
const database = "eoloplant";
const sequelize = new Sequelize(database, "root", "pass", {dialect: 'mysql'});

module.exports = {
    sequelize
};