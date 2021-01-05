const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/database');

const Plant = sequelize.define('plant', {
    id: {
        primaryKey: true,
        type: DataTypes.INTEGER,
        autoIncrement: true
    },
    city: {
        type: DataTypes.STRING,
        allowNull: false
    },
    progress: {
        type: DataTypes.INTEGER,
    }
});

module.exports = {
    sequelize,
    Plant
}